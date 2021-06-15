package com.greenwallet.business.scenes.shopgreen.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.greenwallet.business.R
import com.greenwallet.business.databinding.FragmentShopGreenBinding
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.helper.kotlin.hideKeyboard
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.ProductItemListener
import java.util.*
import kotlin.math.abs

class ShopGreenFragment : Fragment(), ShopGreenView, ProductItemListener {

    private lateinit var presenter: ShopGreenView.Presenter
    private lateinit var binding: FragmentShopGreenBinding

    private lateinit var categoriesAdapter: ShopGreenCategoriesAdapter

    private lateinit var campaignsAdapter: ShopGreenCampaignsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopGreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)

        updateCartCount()

        categoriesAdapter.items = presenter.getCategoryList()

        (binding.rvBestSellers.adapter as ShopGreenBestSellersAdapter).items =
            presenter.getBestSellerItems()

        (binding.rvRedeemOptions.adapter as ShopGreenRedeemOptionsAdapter).items =
            presenter.getRedeemOptions()

        campaignsAdapter.items = presenter.getCampaigns()
    }

    override fun onStop() {
        presenter.disposeView(this)
        super.onStop()
    }

    private fun setup() {

        initSearch()
        initOnboarding()
        initCategories()
        initBestSellers()
        initRedeemOptions()
        initCampaigns()

        binding.ivBuy.setOnClickListener {
            presenter.onCartListClicked()
        }

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) > 200) {
                updateToolbar(false)
            } else {
                updateToolbar(true)
            }
        })
    }

    private fun updateToolbar(isExpanded: Boolean) {
        if (isExpanded) {
            binding.toolbar.setBackgroundColor(Color.TRANSPARENT)
            binding.etSearch.background = GradientDrawable().apply {
                cornerRadius = resources.getDimensionPixelSize(R.dimen.corner_radius_big).toFloat()
                setColor(resources.getColor(android.R.color.white))
            }
            binding.ivBuy.setColorFilter(
                resources.getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_ATOP
            )
            binding.ivBadge.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_badge)
            )
        } else {
            binding.toolbar.setBackgroundColor(resources.getColor(R.color.colorBackgroundContent))
            binding.etSearch.background = GradientDrawable().apply {
                cornerRadius = resources.getDimensionPixelSize(R.dimen.corner_radius_big).toFloat()
                setColor(resources.getColor(R.color.colorDisable))
            }
            binding.ivBuy.setColorFilter(
                resources.getColor(R.color.colorAccent),
                android.graphics.PorterDuff.Mode.SRC_ATOP
            )
            binding.ivBadge.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_badge_green)
            )
        }

    }

    private fun initSearch() {
        binding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                //todo
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
        }
    }

    private fun initOnboarding() {
        val onboardingItems = presenter.createOnboardingItems()

        binding.vpCarousel.isUserInputEnabled = false
        binding.vpCarousel.adapter = OnboardingAdapter(onboardingItems)
    }

    private fun initCategories() {
        categoriesAdapter = ShopGreenCategoriesAdapter().apply {
            itemClickListener = {
                presenter.onCategoryItemClicked(it)
            }
        }
        binding.rvCategories.layoutManager = GridLayoutManager(context, 3)
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun initBestSellers() {
        binding.tvSeeAllBestSellers.setOnClickListener { presenter.onShowAllBestSellersClicked() }
        binding.rvBestSellers.adapter = ShopGreenBestSellersAdapter(this)
    }

    private fun initRedeemOptions() {
        binding.tvSeeAllRedeemOptions.setOnClickListener {
            presenter.onShowAllRedeemOptionsClicked()
        }
        binding.rvRedeemOptions.adapter = ShopGreenRedeemOptionsAdapter(this)
    }

    private fun initCampaigns() {
        binding.tvSeeAllCampaigns.setOnClickListener {
            presenter.onShowAllCampaignsClicked()
        }
        campaignsAdapter = ShopGreenCampaignsAdapter().apply {
            itemClickListener = {
                presenter.onCampaignClicked(it)
            }

            imageLoaderListener = { id, listener, sizes ->
                presenter.fetchImage(id, listener, sizes, true)
            }
        }

        binding.rvCampaigns.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCampaigns.adapter = campaignsAdapter

        binding.rvCampaigns.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildAdapterPosition(view) > 0) {
                    outRect.left = 88
                }
            }
        })
    }

    private fun updateCartCount() {
        binding.ivBadge.visibility = View.VISIBLE
        binding.tvBadgeCount.visibility = View.VISIBLE

        when {
            User.shared.cartProducts.size == 0 -> {
                binding.ivBadge.visibility = View.INVISIBLE
                binding.tvBadgeCount.visibility = View.INVISIBLE
            }
            User.shared.cartProducts.size > 9 -> {
                binding.tvBadgeCount.text = "+9"
            }
            else -> {
                binding.tvBadgeCount.text = User.shared.cartProducts.size.toString()
            }
        }
    }

    override fun onItemClicked(product: ProductResponseModel) {
        presenter.onProductClicked(product)
    }

    override fun onItemReviewClicked(
        productID: String,
        reviews: ArrayList<ProductReviewsResponseModel>
    ) {
        presenter.onProductReviewClicked(productID, reviews)
    }

    override fun fetchImage(
        id: String,
        listener: ImageLoaderListener,
        sizes: Pair<Int, Int>
    ) {
        presenter.fetchImage(id, listener, sizes, true)
    }

    override fun fetchReviews(
        id: String,
        listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
    ) {
        presenter.fetchReviews(id, listener)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ShopGreenPresenterProvider) {
            this.presenter = context.getShopGreenPresenter()
        } else {
            throw RuntimeException("$context must implement ShopGreenPresenterProvider")
        }
    }

    interface ShopGreenPresenterProvider {
        fun getShopGreenPresenter(): ShopGreenView.Presenter
    }
}
