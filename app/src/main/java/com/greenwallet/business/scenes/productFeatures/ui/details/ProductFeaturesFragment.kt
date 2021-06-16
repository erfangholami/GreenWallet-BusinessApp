package com.greenwallet.business.scenes.productFeatures.ui.details

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.SimpleItemAnimator
import com.greenwallet.business.R
import com.greenwallet.business.databinding.FragmentProductFeaturesBinding
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.helper.shop.getOverallRating
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.helper.ui.widget.SpinnerWidgetAdapter
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.product.response.*
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.ProductItemListener
import com.greenwallet.business.scenes.productFeatures.ui.details.adapter.AvailableColorsAdapter
import com.greenwallet.business.scenes.productFeatures.ui.details.adapter.AvailableSizesAdapter
import com.greenwallet.business.scenes.productFeatures.ui.details.adapter.ImageSliderAdapter
import com.greenwallet.business.scenes.productFeatures.ui.details.adapter.ProductReviewsAdapter
import com.greenwallet.business.scenes.shopgreen.ui.ShopGreenBestSellersAdapter

class ProductFeaturesFragment(val product: ProductResponseModel) : Fragment(), ProductFeaturesView {

    companion object {
        const val SWIPE_SENSETIVITY_MEDIUM = 600
        const val SWIPE_SENSETIVITY_HIGH = 400
    }

    private lateinit var presenter: ProductFeaturesView.Presenter
    private lateinit var binding: FragmentProductFeaturesBinding

    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var colorsAdapter: AvailableColorsAdapter
    private lateinit var sizeAdapter: AvailableSizesAdapter
    private lateinit var variationsAdapter: SpinnerWidgetAdapter<ProductVariantsResponseModel>

    private var currentContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductFeaturesBinding.inflate(inflater, container, false)

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
    }

    override fun onStop() {
        presenter.disposeView(this)

        super.onStop()
    }

    private fun setup() {

        if (product.stock?.available!! > 0) {
            binding.tvOutOfStock.visibility = GONE

            binding.btnBuyNow.isEnabled = true
            binding.btnBuyNow.alpha = 1.0f

            binding.ivBag.isEnabled = true
            binding.ivBag.alpha = 1.0f
        } else {
            binding.tvOutOfStock.visibility = VISIBLE

            binding.btnBuyNow.isEnabled = false
            binding.btnBuyNow.alpha = 0.8f

            binding.ivBag.isEnabled = false
            binding.ivBag.alpha = 0.8f
        }

        binding.tvTitle.text = product.name
        binding.tvSoldBy.text = "Sold by ${product.brand}"

        initAboutThisItem()
        initImageSlider()
        initClickListeners()
        initVariations()
        initShipment()
        initRelatedProducts()
        initReviews()
        initBottomViews()
        initSwipe()
    }

    private fun updateCartListCount() {
        binding.header.ivBadge.visibility = VISIBLE
        binding.header.tvBadgeCount.visibility = VISIBLE

        when {
            User.shared.cartProducts.size == 0 -> {
                binding.header.ivBadge.visibility = View.INVISIBLE
                binding.header.tvBadgeCount.visibility = View.INVISIBLE
            }
            User.shared.cartProducts.size > 9 -> {
                binding.header.tvBadgeCount.text = "+9"
            }
            else -> {
                binding.header.tvBadgeCount.text = User.shared.cartProducts.size.toString()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ProductFeaturesProvider) {
            this.presenter = context.getProductFeaturesProvider()
        } else {
            throw RuntimeException("$context must implement ProductFeaturesProvider")
        }

        currentContext = context
    }

    private fun initAboutThisItem() {
        binding.wvDescription.isScrollContainer = false
        binding.wvDescription.isScrollbarFadingEnabled = true
        binding.wvDescription.isVerticalScrollBarEnabled = false
        binding.wvDescription.isHorizontalScrollBarEnabled = false
        binding.wvDescription.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        binding.wvDescription.setBackgroundColor(Color.TRANSPARENT)

        if (!product.details.isNullOrEmpty()) {
            binding.wvDescription.visibility = VISIBLE

            binding.wvDescription.setBackgroundColor(Color.TRANSPARENT)

            var htmlToShow = product.details

            if (product.details.contains("font-weight: 1")) {
                htmlToShow = product.details.replace("font-weight: 1", "font-weight: 300")
            }

            binding.wvDescription.loadData(htmlToShow, "text/html", "utf-8")
        } else {
            binding.wvDescription.visibility = GONE
        }
    }

    private fun initImageSlider() {
        if (!this::imageSliderAdapter.isInitialized) {
            val files = presenter.getProductFiles()
            imageSliderAdapter = ImageSliderAdapter().apply {
                itemClickListener = {
                    presenter.setCurrentPicturePosition(binding.svImages.currentPagePosition)
                    presenter.onZoomClicked()
                }
                imageListener = { id, listener ->
                    presenter.fetchImage(id, listener, null, false)
                }
                mode = files.first
                items = files.second
            }
        }
        binding.svImages.setOffscreenPageLimit(imageSliderAdapter.items.size)
        binding.svImages.setSliderAdapter(imageSliderAdapter, imageSliderAdapter.items.size > 1)

        updateImagePosition()
    }

    private fun initClickListeners() {
        binding.header.ivBackBtn.setOnClickListener { presenter.onBackPressed() }
        binding.tvSeeAllReviews.setOnClickListener {
            presenter.onSeeAllReviewsClicked(
                product.productID!!,
                product.reviews
            )
        }
        binding.header.ivBasket.setOnClickListener { presenter.onBasketClicked() }
        binding.header.ivZoom.setOnClickListener {
            presenter.setCurrentPicturePosition(binding.svImages.currentPagePosition)
            presenter.onZoomClicked()
        }
        binding.llShowMore.setOnClickListener {
            product.details?.let { it1 -> presenter.onShowMoreClicked(it1) }
        }
        binding.tvFindSize.setOnClickListener { presenter.onFindSizeClicked(product.sizeMapFileId!!) }
        binding.ivBag.setOnClickListener {
            presenter.onAddToCartClicked()
            updateCartListCount()
        }
    }

    private fun initVariations() {

        if (!presenter.hasFetchedVariantsBefore()) {
            presenter.getProductVariants(
                product.merchantID!!,
                product.productID!!,
                object : CallbackListener<Unit>() {
                    override fun onAPICallFinished(data: Unit) {

                        initAvailableColours(presenter.getAvailableColors())
                        initAvailableSizes(presenter.getAvailableSizes())
                        initOtherVariations(presenter.getOtherVariables())
                        checkHavingSizeFile()
                        updateDetailsBasedOnVariants()
                    }

                    override fun onAPICallFailed() {
                        updateDetailsBasedOnVariants()
                    }
                }
            )
        } else {
            initAvailableColours(presenter.getAvailableColors())
            initAvailableSizes(presenter.getAvailableSizes())
            initOtherVariations(presenter.getOtherVariables())
            checkHavingSizeFile()
            updateDetailsBasedOnVariants()
        }
    }

    private fun initAvailableColours(colors: ArrayList<ProductVariantsResponseModel>) {
        if (colors.size > 0) {
            binding.clColorContainer.visibility = VISIBLE
            (binding.rvColours.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            val selectedColorVariant = presenter.getSelectedVariant("COLOUR")
            val selectedPosition = if (selectedColorVariant != null) {
                colors.indexOfFirst {
                    it.variantId == selectedColorVariant.variantId
                }
            } else {
                0
            }

            colorsAdapter = AvailableColorsAdapter(colors).apply {
                selectedItemPosition = selectedPosition
                colorChangeListener = {
                    updateVariant(it)
                    presenter.setCurrentPicturePosition(0)
                    updateDetailsBasedOnVariants()
                    binding.nsContent.fullScroll(View.FOCUS_UP)
                    binding.appBar.setExpanded(true)
                }
            }
            binding.rvColours.adapter = colorsAdapter
            updateVariant(selectedColorVariant ?: colors[0])
        } else {
            binding.clColorContainer.visibility = GONE
            //hide views
        }
    }

    private fun initAvailableSizes(sizes: ArrayList<ProductVariantsResponseModel>) {
        if (sizes.size > 0) {
            binding.clSizeContainer.visibility = VISIBLE
            binding.clSizeContainer.requestLayout()
            binding.tvSizes.visibility = VISIBLE
            binding.cgSizes.visibility = VISIBLE
            context?.let {

                val selectedSizeVariant = presenter.getSelectedVariant("SIZE")
                val selectedSize: ProductVariantsResponseModel = if (selectedSizeVariant != null) {
                    sizes.find {
                        it.variantId == selectedSizeVariant.variantId
                    }!!
                } else {
                    sizes[0]
                }
                sizeAdapter = AvailableSizesAdapter(sizes, binding.cgSizes).apply {
                    selectItem(selectedSize)
                    sizeChangeListener = {
                        updateVariant(it)
                        presenter.setCurrentPicturePosition(0)
                        updateDetailsBasedOnVariants()
                        binding.nsContent.fullScroll(FOCUS_UP)
                        binding.appBar.setExpanded(true)
                    }
                }
                updateVariant(selectedSizeVariant ?: selectedSize)
            }
        } else {
            binding.clSizeContainer.visibility = VISIBLE
            binding.tvSizes.visibility = GONE
            binding.cgSizes.visibility = GONE
            //hide view
        }
    }

    private fun initOtherVariations(others: ArrayList<ProductVariantsResponseModel>) {
        if (others.size > 0) {
            binding.clOtherVariantsContainer.visibility = VISIBLE
            variationsAdapter =
                SpinnerWidgetAdapter(
                    object : SpinnerWidgetAdapter.Mapper<ProductVariantsResponseModel> {
                        override fun getName(item: ProductVariantsResponseModel): String {
                            return item.value ?: ""
                        }
                    })

            binding.spnVariation.setAdapter(variationsAdapter)
            variationsAdapter.setData(others)

            val selectedOtherVariant = presenter.getSelectedVariant("OTHER")
            val selectedPosition = if (selectedOtherVariant != null) {
                others.indexOfFirst {
                    it.variantId == selectedOtherVariant.variantId
                }
            } else {
                0
            }

            variationsAdapter.onItemSelected(selectedPosition)
            updateVariant(selectedOtherVariant ?: others[0])

            binding.spnVariation.setOnItemSelectedListener {
                updateVariant((it as ProductVariantsResponseModel))
                presenter.setCurrentPicturePosition(0)
                updateDetailsBasedOnVariants()
                binding.nsContent.fullScroll(View.FOCUS_UP)
                binding.appBar.setExpanded(true)
            }
        } else {
            binding.clOtherVariantsContainer.visibility = GONE
            //hide view
        }
    }

    private fun checkHavingSizeFile() {
        if (product.sizeMapFileId.isNullOrEmpty()) {
            binding.tvFindSize.visibility = GONE
        } else {
            binding.tvFindSize.visibility = VISIBLE
            binding.tvFindSize.setOnClickListener {
                presenter.onFindSizeClicked(product.sizeMapFileId)
            }
        }
    }

    private fun updateVariant(variant: ProductVariantsResponseModel) {
        presenter.setVariant(variant)
    }

    private fun updateDetailsBasedOnVariants() {
        val variation = presenter.getVariation()

        if (variation != null) {
            updatePrice(variation)
        }

        updateAvailability()

        val items = presenter.getProductFiles()
//        binding.svImages.setOffscreenPageLimit(items.size)
        binding.svImages.setSliderAdapter(imageSliderAdapter, imageSliderAdapter.items.size > 1)
        imageSliderAdapter.mode = items.first
        imageSliderAdapter.items = items.second

        updateImagePosition()
    }

    private fun updateImagePosition() {
        binding.svImages.currentPagePosition = presenter.getShowingImagePosition()
        binding.svImages.pagerIndicator.selection = presenter.getShowingImagePosition()
    }

    private fun initShipment() {
        binding.ivShippingCancel.setOnClickListener {
            binding.clShippingContainer.visibility = GONE
        }

        presenter.getProductShipments(
            product.merchantID!!,
            product.productID!!,
            object : CallbackListener<Array<ProductShipmentsResponseModel>>() {
                override fun onAPICallFinished(data: Array<ProductShipmentsResponseModel>) {

                    var hasAnyThreshold = false
                    data.find { it.getShippingMethod() == ShippingMethod.STANDARD_DELIVERY }
                        ?.let { item ->
                            if (item.threshold ?: 0 > 0) {
                                hasAnyThreshold = true
                                val amount = String.format("%.2f", item.threshold!!.toFloat() / 100)
                                binding.tvShippingInfo.text =
                                    "FREE Shipping for a minimum spend of £$amount"
                            }
                        }

                    if (hasAnyThreshold) {
                        binding.clShippingContainer.visibility = VISIBLE
                    } else {
                        binding.clShippingContainer.visibility = GONE
                    }

                }

                override fun onAPICallFailed() {
                    binding.clShippingContainer.visibility = GONE
                }
            }
        )

    }

    private fun initRelatedProducts() {
//        val relatedProductsItems = presenter.createRelatedProductsList()
        binding.rvRelatedProducts.adapter =
            ShopGreenBestSellersAdapter(object : ProductItemListener {
                override fun onItemClicked(product: ProductResponseModel) {}

                override fun onItemReviewClicked(
                    productID: String,
                    reviews: java.util.ArrayList<ProductReviewsResponseModel>
                ) {
                }

                override fun fetchImage(
                    id: String,
                    listener: ImageLoaderListener,
                    sizes: Pair<Int, Int>
                ) {
                }

                override fun fetchReviews(
                    id: String,
                    listener: CallbackListener<java.util.ArrayList<ProductReviewsResponseModel>>
                ) {
                }

            })
    }

    private fun initReviews() {
        if (product.reviews == null) {
            presenter.fetchReviews(
                product.productID!!,
                object : CallbackListener<ArrayList<ProductReviewsResponseModel>>() {
                    override fun onAPICallFinished(reviews: ArrayList<ProductReviewsResponseModel>) {
                        product.reviews = reviews
                        bindReviews()
                    }

                    override fun onAPICallFailed() {
                        bindReviews()
                    }
                })

        } else {
            bindReviews()
        }
    }

    private fun bindReviews() {
        if (product.reviews.isNullOrEmpty()) {
            binding.tvReviews.visibility = GONE
            binding.tvSeeAllReviews.visibility = GONE
            binding.rvReviews.visibility = GONE
        } else {
            binding.tvReviews.visibility = VISIBLE
            binding.tvSeeAllReviews.visibility = VISIBLE
            binding.rvReviews.visibility = VISIBLE
        }

        val overallRating = getOverallRating(product.reviews)

        binding.rbRating.rating = overallRating
        binding.tvCurrentRating.text =
            context?.getString(R.string.label_current_reviews, overallRating)

        binding.tvReviewers.text =
            context?.getString(R.string.label_reviews_end, product.reviews.let { it?.size ?: 0 })
        binding.tvReviews.text =
            context?.getString(R.string.label_reviews_end, product.reviews.let { it?.size ?: 0 })
        binding.rvReviews.adapter = ProductReviewsAdapter(product.reviews ?: arrayListOf())
        binding.rvReviews.isNestedScrollingEnabled = false

        binding.clRatingContainer.setOnClickListener {
            presenter.onSeeAllReviewsClicked(
                product.productID!!,
                product.reviews
            )
        }
    }

    private fun initBottomViews() {
        updatePrice()
        binding.btnBuyNow.setOnClickListener {
            presenter.onBuyNowClicked()
        }

        binding.ivBag.visibility = if (product.isHotDeal()) INVISIBLE else VISIBLE
    }

    private fun updatePrice(variation: ProductVariationsResponseModel? = null) {
        if (variation == null || variation.price.amount == 0) {
            if (product.isHotDeal()) {
                binding.tvPrice.text =
                    context?.getString(
                        R.string.label_price,
                        product.price?.discount?.amount?.toFloat()?.div(100)
                    )
            } else {
                binding.tvPrice.text =
                    context?.getString(
                        R.string.label_price,
                        product.price?.amount?.toFloat()?.div(100)
                    )
            }
        } else {
            binding.tvPrice.text =
                context?.getString(
                    R.string.label_price,
                    variation.price.amount.toFloat().div(100)
                )
        }
    }

    private fun updateAvailability() {
        val variation = presenter.getVariation()

        if (variation == null) {
            if (product.stock?.available!! > 0) {
                binding.tvOutOfStock.visibility = GONE

                binding.btnBuyNow.isEnabled = true
                binding.btnBuyNow.alpha = 1.0f

                binding.ivBag.isEnabled = true
                binding.ivBag.alpha = 1.0f
            } else {
                binding.tvOutOfStock.visibility = VISIBLE

                binding.btnBuyNow.isEnabled = false
                binding.btnBuyNow.alpha = 0.8f

                binding.ivBag.isEnabled = false
                binding.ivBag.alpha = 0.8f
            }
        } else {
            if (variation.stock.available > 0) {
                binding.tvOutOfStock.visibility = GONE

                binding.btnBuyNow.isEnabled = true
                binding.btnBuyNow.alpha = 1.0f

                binding.ivBag.isEnabled = true
                binding.ivBag.alpha = 1.0f
            } else {
                binding.tvOutOfStock.visibility = VISIBLE

                binding.btnBuyNow.isEnabled = false
                binding.btnBuyNow.alpha = 0.8f

                binding.ivBag.isEnabled = false
                binding.ivBag.alpha = 0.8f
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSwipe() {
        binding.nsContent.setOnTouchListener(object : View.OnTouchListener {
            var x1 = 2000f
            var y1 = 2000f

            var x2 = 0f
            var y2 = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x1 = event.x
                        y1 = event.y
                    }
                    MotionEvent.ACTION_UP -> {
                        x2 = event.x
                        y2 = event.y
                        if (x2 - SWIPE_SENSETIVITY_HIGH > x1) {
                            //swipe right
                            presenter.onBackPressed()
                        }
                    }
                }

                return false
            }
        })
    }

    interface ProductFeaturesProvider {
        fun getProductFeaturesProvider(): ProductFeaturesView.Presenter
    }
}
