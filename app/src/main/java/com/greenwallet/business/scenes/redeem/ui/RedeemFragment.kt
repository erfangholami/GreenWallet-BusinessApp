package com.greenwallet.business.scenes.redeem.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.greenwallet.business.R
import com.greenwallet.business.databinding.FragmentRedeemBinding
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.helper.kotlin.hideKeyboard
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter.Companion.ITEM_VIEW_TYPE_EMPTY
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter.Companion.ITEM_VIEW_TYPE_LOADING
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter.Companion.ITEM_VIEW_TYPE_PRODUCT
import com.greenwallet.business.scenes.base.LoadMoreCallBack
import com.greenwallet.business.scenes.base.ProductItemListener
import kotlin.math.abs

class RedeemFragment : Fragment(), RedeemView, ProductItemListener {

    companion object {
        const val SPAN_COUNT = 2
        const val VISIBLE_THRESHOLD = 4
    }

    private lateinit var presenter: RedeemView.Presenter
    private lateinit var binding: FragmentRedeemBinding

    private var listState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRedeemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)

        updateCartListCount()

        if (listState != null) {
            binding.rvRedeem.layoutManager?.onRestoreInstanceState(listState)
        }

        val biggestMargin = context?.resources?.getDimensionPixelSize(R.dimen.margin_small_x)!! * 13
        val smallestMargin = context?.resources?.getDimensionPixelSize(R.dimen.margin_big)!!

        binding.appToolbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val newStartMarginOfSearch = smallestMargin + ((-1 * verticalOffset * (biggestMargin - smallestMargin)).toFloat().div( binding.clTitle.height)).toInt()

            val lp = (binding.etSearch.layoutParams as ConstraintLayout.LayoutParams)

            if (abs(lp.marginStart - newStartMarginOfSearch) > 6) {
                lp.marginStart = newStartMarginOfSearch
                binding.etSearch.layoutParams = lp
            }
        })
    }

    override fun onStop() {
        listState = binding.rvRedeem.layoutManager?.onSaveInstanceState()
        presenter.disposeView(this)

        super.onStop()
    }

    private fun setup() {
        binding.ivBackButton.setOnClickListener { presenter.dismiss() }

        binding.viewBadgeContainer.root.setOnClickListener { presenter.onCartListClicked() }
        initSearch()
        initList()

        loadMore()
    }

    private fun initSearch() {
        binding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                val adapter = binding.rvRedeem.adapter as RedeemAdapter
                adapter.items = ArrayList()
                adapter.showLoading(true)

                presenter.onSearchTextChanged(textView.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

        binding.etSearch.doOnTextChanged { text, start, before, count ->
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

            val adapter = binding.rvRedeem.adapter as RedeemAdapter
            adapter.items = ArrayList()
            adapter.showLoading(true)

            presenter.onResetClicked()
        }
    }

    private fun initList() {
        val layoutManager = GridLayoutManager(
            requireContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false
        ).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (binding.rvRedeem.adapter?.getItemViewType(position)) {
                        ITEM_VIEW_TYPE_PRODUCT -> 1
                        ITEM_VIEW_TYPE_LOADING -> SPAN_COUNT
                        ITEM_VIEW_TYPE_EMPTY -> SPAN_COUNT
                        else -> 1
                    }
                }
            }
        }

        binding.rvRedeem.layoutManager = layoutManager

        binding.rvRedeem.adapter =
            RedeemAdapter(this )

        presenter.setLoadCallBack(object :
            LoadMoreCallBack<ProductResponseModel, BaseRecyclerViewAdapter.Mode> {
            override fun onLoadMoreFinished(
                newList: ArrayList<ProductResponseModel>,
                mode: BaseRecyclerViewAdapter.Mode
            ) {
                val adapter = binding.rvRedeem.adapter as RedeemAdapter
                adapter.showLoading(false)
                adapter.mode = mode
                adapter.addItems(newList)
                adapter.notifyDataSetChanged()
            }

            override fun onLoadMoreFailed() {
                (binding.rvRedeem.adapter as RedeemAdapter).showLoading(false)
            }
        })

        binding.rvRedeem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount: Int =
                    (binding.rvRedeem.adapter as RedeemAdapter).items.size
                val lastVisibleItem: Int =
                    (binding.rvRedeem.layoutManager as GridLayoutManager).findLastVisibleItemPosition()

                if (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    loadMore()
                }
            }
        })
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

    private fun loadMore() {
        if (presenter.hasMore() && !presenter.isLoading()) {
            (binding.rvRedeem.adapter as RedeemAdapter).showLoading(true)

            presenter.onLoadMore()
        }
    }

    override fun onItemClicked(product: ProductResponseModel) {
        presenter.onProductClicked(product)
    }

    override fun onItemReviewClicked(
        productID: String,
        reviews: java.util.ArrayList<ProductReviewsResponseModel>
    ) {}

    override fun fetchImage(
        id: String,
        listener: ImageLoaderListener,
        sizes: Pair<Int, Int>
    ) {
        presenter.fetchImage(id, listener, sizes, true)
    }

    override fun fetchReviews(
        id: String,
        listener: CallbackListener<java.util.ArrayList<ProductReviewsResponseModel>>
    ) {}

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is RedeemPresenterProvider) {
            this.presenter = context.getRedeemPresenter()
        } else {
            throw RuntimeException("$context must implement RedeemPresenterProvider")
        }
    }

    interface RedeemPresenterProvider {
        fun getRedeemPresenter(): RedeemView.Presenter
    }
}