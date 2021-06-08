package com.greenwallet.business.scenes.searchProducts.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.greenwallet.business.R
import com.greenwallet.business.databinding.FragmentSearchProductsBinding
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.helper.kotlin.hideKeyboard
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.product.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter
import com.greenwallet.business.scenes.base.LoadMoreCallBack
import com.greenwallet.business.scenes.searchProducts.SearchProductsPresenter
import kotlin.math.abs

private const val SPAN_COUNT = 2

class SearchProductsFragment : Fragment(), SearchProductsView, SearchProductsAdapter.CallBack {

    companion object {
        const val VISIBLE_THRESHOLD = 4
    }

    private lateinit var presenter: SearchProductsView.Presenter
    private lateinit var binding: FragmentSearchProductsBinding

    private var currentContext: Context? = null

    private var adapterMode = AdapterMode.GRID
    private var listState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)

        binding.etSearch.setText(presenter.getSearchText())

        updateCartListCount()

        if (listState != null) {
            binding.rvSearchProducts.layoutManager?.onRestoreInstanceState(listState)
        }
    }

    override fun onStop() {
        listState = binding.rvSearchProducts.layoutManager?.onSaveInstanceState()

        presenter.disposeView(this)
        super.onStop()
    }

    private fun setup() {
        binding.viewBadgeContainer.root.setOnClickListener { presenter.onCartListClicked() }

        initSearch()
        initClickListeners()
        initSearchProductsList()

        onListTypeChanged(AdapterMode.LIST)

        val biggestMargin = context?.resources?.getDimensionPixelSize(R.dimen.margin_small_x)!! * 13
        val smallestMargin = context?.resources?.getDimensionPixelSize(R.dimen.margin_big)!!

        val gridListIconsMaxHeight =
            context?.resources?.getDimensionPixelSize(R.dimen.view_icon_default_size)!!

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val newStartMarginOfSearch =
                smallestMargin + ((-1 * verticalOffset * (biggestMargin - smallestMargin)).toFloat()
                    .div(binding.clTitleDescriptionContainer.height)).toInt()

            val lp = (binding.etSearch.layoutParams as ConstraintLayout.LayoutParams)

            if (abs(lp.marginStart - newStartMarginOfSearch) >= 6) {
                lp.marginStart = newStartMarginOfSearch
                binding.etSearch.layoutParams = lp
            }


            //grid list
            val gridLp = (binding.clGridListContainer.layoutParams as AppBarLayout.LayoutParams)
            val newHeight =
                gridListIconsMaxHeight - ((-1 * verticalOffset * gridListIconsMaxHeight).toFloat()
                    .div(binding.clTitleDescriptionContainer.height)).toInt()
            if (abs(gridLp.height - newHeight) >= 4) {
                binding.clGridListContainer.layoutParams =
                    AppBarLayout.LayoutParams(gridLp.width, newHeight)
            }
        })

        loadMore()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SearchProductsPresenterProvider) {
            this.presenter = context.getSearchProductsPresenter()
        } else {
            throw RuntimeException("$context must implement ShopGreenPresenterProvider")
        }

        currentContext = context
    }

    private fun initClickListeners() {
        binding.ivBackButton.setOnClickListener { presenter.onBackPressed() }
        binding.ivGridButton.setOnClickListener { onListTypeChanged(AdapterMode.GRID) }
        binding.ivListButton.setOnClickListener { onListTypeChanged(AdapterMode.LIST) }
    }

    private fun onListTypeChanged(mode: AdapterMode) {
        if (adapterMode != mode) {
            adapterMode = mode

            when (adapterMode) {
                AdapterMode.GRID -> {
                    imagesStateChanged(false)
                    context?.let {
                        binding.rvSearchProducts.layoutManager = GridLayoutManager(
                            it, SPAN_COUNT, GridLayoutManager.VERTICAL, false
                        ).apply {
                            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                                override fun getSpanSize(position: Int): Int {
                                    return when (binding.rvSearchProducts.adapter?.getItemViewType(
                                        position
                                    )) {
                                        BaseRecyclerViewAdapter.ITEM_VIEW_TYPE_PRODUCT -> 1
                                        BaseRecyclerViewAdapter.ITEM_VIEW_TYPE_LOADING -> SPAN_COUNT
                                        BaseRecyclerViewAdapter.ITEM_VIEW_TYPE_EMPTY -> SPAN_COUNT
                                        else -> 1
                                    }
                                }
                            }
                        }
                        binding.rvSearchProducts.adapter =
                            SearchProductsAdapter(AdapterMode.GRID, this).apply {
                                items = presenter.getSearchProductsList()
                            }
                    }
                }
                AdapterMode.LIST -> {
                    imagesStateChanged(true)
                    context?.let {
                        binding.rvSearchProducts.layoutManager = LinearLayoutManager(
                            it, LinearLayoutManager.VERTICAL, false
                        )
                        binding.rvSearchProducts.adapter =
                            SearchProductsAdapter(AdapterMode.LIST, this).apply {
                                items = presenter.getSearchProductsList()
                            }
                    }
                }
            }
        }
    }

    private fun initSearchProductsList() {
//        binding.rvSearchProducts.setItemViewCacheSize(20)
        binding.rvSearchProducts.isDrawingCacheEnabled = true
        binding.rvSearchProducts.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        binding.rvSearchProducts.adapter = SearchProductsAdapter(adapterMode, this).apply {
            items = presenter.getSearchProductsList()
        }
        binding.rvSearchProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount: Int =
                    (binding.rvSearchProducts.adapter as SearchProductsAdapter).items.size
                val lastVisibleItem: Int = when (binding.rvSearchProducts.layoutManager) {
                    is GridLayoutManager -> (binding.rvSearchProducts.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    else -> (binding.rvSearchProducts.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                }

                if (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    loadMore()
                }
            }
        })

        presenter.setLoadCallBack(object :
            LoadMoreCallBack<ProductResponseModel, BaseRecyclerViewAdapter.Mode> {
            override fun onLoadMoreFinished(
                newList: ArrayList<ProductResponseModel>,
                mode: BaseRecyclerViewAdapter.Mode
            ) {
                val adapter = binding.rvSearchProducts.adapter as SearchProductsAdapter
                adapter.mode = mode
                adapter.addItems(newList)
            }

            override fun onLoadMoreFailed() {
                (binding.rvSearchProducts.adapter as SearchProductsAdapter).showLoading(false)
            }

        })
    }

    private fun loadMore() {
        if (presenter.hasMore() && !presenter.isLoading()) {
            (binding.rvSearchProducts.adapter as SearchProductsAdapter).showLoading(true)

            presenter.onLoadMore()
        }
    }

    private fun imagesStateChanged(isListSelected: Boolean) {
        if (isListSelected) {
            binding.ivListButton.setImageResource(R.drawable.ic_list_selected)
            binding.ivGridButton.setImageResource(R.drawable.ic_grid)
        } else {
            binding.ivListButton.setImageResource(R.drawable.ic_list)
            binding.ivGridButton.setImageResource(R.drawable.ic_grid_selected)
        }
    }

    private fun initSearch() {
        hideKeyboard()

        binding.etSearch.setText(presenter.getSearchText())

        binding.etSearch.setOnEditorActionListener(OnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                presenter.onSearchTextChanged(textView.text.toString())
                (binding.rvSearchProducts.adapter as SearchProductsAdapter).items = arrayListOf()
                loadMore()
                return@OnEditorActionListener true
            }
            false
        })

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            if (text?.count() ?: 0 > 0) {
                binding.btnClear.visibility = View.VISIBLE
                binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                binding.btnClear.visibility = View.INVISIBLE
                binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_search,
                    0
                )
            }
        }

        binding.btnClear.setOnClickListener {
            binding.etSearch.setText("")
            binding.etSearch.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }
    }

    private fun updateCartListCount() {
        binding.viewBadgeContainer.ivBadge.visibility = View.VISIBLE
        binding.viewBadgeContainer.tvBadgeCount.visibility = View.VISIBLE

        when {
            User.shared.cartProducts.size == 0 -> {
                binding.viewBadgeContainer.ivBadge.visibility = View.INVISIBLE
                binding.viewBadgeContainer.tvBadgeCount.visibility = View.INVISIBLE
            }
            User.shared.cartProducts.size > 9 -> {
                binding.viewBadgeContainer.tvBadgeCount.text = "+9"
            }
            else -> {
                binding.viewBadgeContainer.tvBadgeCount.text =
                    User.shared.cartProducts.size.toString()
            }
        }
    }

    interface SearchProductsPresenterProvider {
        fun getSearchProductsPresenter(): SearchProductsView.Presenter
    }

    override fun onItemClicked(product: ProductResponseModel) {
        presenter.onItemClicked(product)
    }

    override fun onItemReviewClicked(
        productID: String,
        reviews: ArrayList<ProductReviewsResponseModel>
    ) {
        presenter.onItemReviewClicked(productID, reviews)
    }

    override fun fetchImage(id: String, listener: ImageLoaderListener, sizes: Pair<Int, Int>) {
        presenter.fetchImage(id, listener, sizes)
    }

    override fun fetchReviews(
        id: String,
        listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
    ) {
        presenter.fetchReviews(id, listener)
    }

    override fun initValues(
        mode: SearchProductsPresenter.Mode,
        defaultTitle: String?,
        defaultDescription: String?
    ) {
        var title = ""
        var description = ""

        when (mode) {
            SearchProductsPresenter.Mode.DISCOVER -> {
                title = "Discover"
                description =
                    "Thousands of products from your favourite sustainable businesses at your fingertips."
            }
            SearchProductsPresenter.Mode.CATEGORY -> {
                title = "Category"
                description = ""
            }
            SearchProductsPresenter.Mode.HOT_DEALS -> {
                title = "Hot Deals"
                description =
                    "Thousands of daily hot deals from your favourite sustainable businesses at your fingertips."
            }
            SearchProductsPresenter.Mode.BEST_SELLERS -> {
                title = "Our Products"
                description =
                    "Thousands of products from your favourite sustainable businesses at your fingertips."
            }
        }

        if (!defaultTitle.isNullOrEmpty()) {
            title = defaultTitle
        }

        if (!defaultDescription.isNullOrEmpty()) {
            description = defaultDescription
        }

        binding.tvTitle.text = title
        binding.tvDescription.text = description

        if (description.isEmpty()) {
            binding.tvDescription.visibility = View.GONE
        } else {
            binding.tvDescription.visibility = View.VISIBLE
        }
    }
}
