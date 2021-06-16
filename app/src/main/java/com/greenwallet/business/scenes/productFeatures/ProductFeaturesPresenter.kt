package com.greenwallet.business.scenes.productFeatures

import android.content.Context
import android.util.Log
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.InteractorFactory
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.product.ProductShipmentsResponse
import com.greenwallet.business.network.product.ProductVariantsResponse
import com.greenwallet.business.network.product.ProductVariationsResponse
import com.greenwallet.business.network.product.response.*
import com.greenwallet.business.network.productReviews.ProductReviewsResponse
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.findMySize.ui.FindMySizeView
import com.greenwallet.business.scenes.productFeatures.ui.aboutThisItem.ProductFeaturesAboutItemView
import com.greenwallet.business.scenes.productFeatures.ui.details.ProductFeaturesView
import com.greenwallet.business.scenes.productFeatures.ui.fullscreen.FullScreenPhotoView

class ProductFeaturesPresenter(context: Context) :
    BasePresenter<ProductFeaturesView, ProductFeaturesProcessHandler>(context),
    ProductFeaturesView.Presenter,
    ProductFeaturesAboutItemView.Presenter,
    FullScreenPhotoView.Presenter,
    FindMySizeView.Presenter {

    enum class State {
        PRODUCT_FEATURES,
        FULL_SCREEN_PHOTO,
        FIND_MY_SIZE,
    }

    var state: State = State.PRODUCT_FEATURES
        set(value) {
            field = value
            when (state) {
                State.PRODUCT_FEATURES -> activityHandler?.showProductFeaturesScreen()
                State.FULL_SCREEN_PHOTO -> activityHandler?.showFullScreenPhotoScreen()
                State.FIND_MY_SIZE -> {
                    activityHandler?.showFindMySizeScreen()
                }
            }
        }

    private val productInteractor = InteractorFactory(context).createProductInteractor()
    private val productReviewInteractor = InteractorFactory(context).createProductReviewInteractor()

    private var variants: ArrayList<ProductVariantsResponseModel> = arrayListOf()
    private var variations: ArrayList<ProductVariationsResponseModel> = arrayListOf()
    private var shipments: ArrayList<ProductShipmentsResponseModel> = arrayListOf()

    private val availableColorVariables = arrayListOf<ProductVariantsResponseModel>()
    private val availableSizeVariables = arrayListOf<ProductVariantsResponseModel>()
    private val availableOtherVariables = arrayListOf<ProductVariantsResponseModel>()

    lateinit var product: ProductResponseModel

    var currentImagePosition = 0

    lateinit var sizeMapFile: String

    override fun onViewSubscribed(view: ProductFeaturesView) {
        state = State.PRODUCT_FEATURES
    }

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun onFindSizeClicked(sizeMapFileId: String) {
        sizeMapFile = sizeMapFileId
        state = State.FIND_MY_SIZE
    }

    override fun onShowMoreClicked(data: String) {
        activityHandler?.showAboutThisItem(data)
    }

    override fun onSeeAllReviewsClicked(
        productID: String,
        reviews: ArrayList<ProductReviewsResponseModel>?
    ) {
        activityHandler?.onSeeAllReviewsClicked(productID, reviews)
    }

    override fun onBuyNowClicked() {
        val cartProduct = CartProduct(
            product = product,
            quantity = 1,
            variants = variants,
            shipments = shipments,
            variation = getVariation()
        ).apply {
            for (method in shipments) {
                if (method.getShippingMethod() == ShippingMethod.STANDARD_DELIVERY) {
                    selectedShipmentMethod = method
                    break
                }
            }
            //TODO(SET OTHER FEATURES)
        }

        if (User.shared.addProductToBuyNowCart(cartProduct)) {
            activityHandler?.showCheckoutScreen()
        }
    }

    override fun onAddToCartClicked() {
        val cartProduct = CartProduct(
            product = product,
            quantity = 1,
            variants = variants,
            shipments = shipments,
            variation = getVariation()
        ).apply {
            for (method in shipments) {
                if (method.getShippingMethod() == ShippingMethod.STANDARD_DELIVERY) {
                    selectedShipmentMethod = method
                    break
                }
            }
            //TODO(SET OTHER FEATURES)
        }

        if (User.shared.addProductToCart(cartProduct)) {
            activityHandler?.showAddToCartSuccessDialog()
        }
    }

    override fun onBasketClicked() {
        activityHandler?.showBasketScreen()
    }

    override fun subscribeView(view: FindMySizeView) {

    }

    override fun disposeView(view: FindMySizeView) {

    }

    override fun onCloseFindMySize() {
        state = State.PRODUCT_FEATURES
    }

    override fun getSizesFiles(): ArrayList<String> {
        val returnList = arrayListOf<String>()

        returnList.add(sizeMapFile)

        return returnList
    }

    override fun onBackPressed() {
        activityHandler?.onBackPressed()
    }

    override fun setVariant(variant: ProductVariantsResponseModel): ProductVariationsResponseModel? {

        for (item in variants) {
            if (item.type == variant.type) {
                item.variantId = variant.variantId
                item.value = variant.value
                item.description = variant.description
                return getVariation()
            }
        }

        variants.add(
            ProductVariantsResponseModel(
                variantId = variant.variantId,
                value = variant.value,
                type = variant.type,
                description = variant.description,
            )
        )
        return getVariation()
    }

    override fun getSelectedVariant(variantType: String): ProductVariantsResponseModel? {
        for (item in variants) {
            if (item.type == variantType) {
                return item
            }
        }
        return null
    }

    override fun getVariation(): ProductVariationsResponseModel? {

        //todo return proper variation
        for (variation in variations) {
            var matchedVariants = 0

            for (variant in variants) {
                when (variant.type) {
                    "SIZE" -> {
                        if (variant.variantId == variation.sizeVariantId) {
                            matchedVariants++
                        }
                    }
                    "COLOUR" -> {
                        if (variant.variantId == variation.colourVariantId) {
                            matchedVariants++
                        }
                    }
                    else -> {
                        if (variant.variantId == variation.otherVariantId) {
                            matchedVariants++
                        }
                    }
                }
            }

            if (matchedVariants == variants.size) {
                return variation
            }
        }

        return null
    }

    override fun setCurrentPicturePosition(currentPagePosition: Int) {
        currentImagePosition = currentPagePosition
    }

    override fun getShowingImagePosition(): Int {
        return currentImagePosition
    }

    override fun getAvailableColors(): ArrayList<ProductVariantsResponseModel> {
        return availableColorVariables
    }

    override fun getAvailableSizes(): ArrayList<ProductVariantsResponseModel> {
        return availableSizeVariables
    }

    override fun getOtherVariables(): ArrayList<ProductVariantsResponseModel> {
        return availableOtherVariables
    }

    override fun onZoomClicked() {
        state = State.FULL_SCREEN_PHOTO
    }

    override fun getProductShipments(
        merchantId: String,
        productId: String,
        listener: CallbackListener<Array<ProductShipmentsResponseModel>>
    ) {

        productInteractor.shipments(
            merchantId = merchantId,
            productId = productId,
            listener = object : Subscriber<ProductShipmentsResponse> {
                override fun onRequestSuccess(response: ProductShipmentsResponse) {
                    if (response.result == ProductShipmentsResponse.Result.SUCCESS) {

                        shipments = (response.shipments ?: arrayOf()).toCollection(ArrayList())
                        listener.onAPICallFinished(response.shipments ?: arrayOf())

                    } else {
                        listener.onAPICallFailed()
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    listener.onAPICallFailed()
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            }
        )
    }

    override fun fetchReviews(
        productId: String,
        listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
    ) {
        productReviewInteractor.reviews(
            productId = productId,
            listener = object :
                Subscriber<ProductReviewsResponse> {
                override fun onRequestSuccess(response: ProductReviewsResponse) {
                    if (response.response == ProductReviewsResponse.Result.SUCCESS) {

                        listener.onAPICallFinished(
                            (response.reviews ?: arrayOf()).toCollection(
                                ArrayList()
                            )
                        )

                        Log.e("ProductReviews", "reviews: ${response.reviews?.size}")

                        Log.e("ProductReviews", "Success")
                    } else {
                        listener.onAPICallFailed()

                        Log.e("ProductReviews", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    listener.onAPICallFailed()

                    Log.e("ProductReviews", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }

    var hasFetchedProductVariantsBefore = false

    override fun hasFetchedVariantsBefore() = hasFetchedProductVariantsBefore

    override fun getProductVariants(
        merchantId: String,
        productId: String,
        listener: CallbackListener<Unit>
    ) {

        hasFetchedProductVariantsBefore = false

        availableSizeVariables.clear()
        availableColorVariables.clear()
        availableOtherVariables.clear()

        productInteractor.variants(
            merchantId,
            productId,
            object : Subscriber<ProductVariantsResponse> {
                override fun onRequestSuccess(response: ProductVariantsResponse) {
                    if (response.response == ProductVariantsResponse.Result.SUCCESS) {

                        for (item in response.variants ?: arrayOf()) {
                            when (item.type) {
                                "SIZE" -> {
                                    availableSizeVariables.add(item)
                                }
                                "COLOUR" -> {
                                    availableColorVariables.add(item)
                                }
                                else -> {
                                    availableOtherVariables.add(item)
                                }
                            }
                        }

                        getVariationDetails(listener)

                    } else {
                        listener.onAPICallFailed()
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    listener.onAPICallFailed()
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            }
        )
    }

    private fun getVariationDetails(listener: CallbackListener<Unit>) {

        val first: ArrayList<ProductVariantsResponseModel>
        val second: ArrayList<ProductVariantsResponseModel>
        val third: ArrayList<ProductVariantsResponseModel>

        if (availableColorVariables.size > availableSizeVariables.size) {
            if (availableColorVariables.size > availableOtherVariables.size) {
                first = availableColorVariables
                if (availableSizeVariables.size > availableOtherVariables.size) {
                    second = availableSizeVariables
                    third = availableOtherVariables
                } else {
                    second = availableOtherVariables
                    third = availableSizeVariables
                }
            } else {
                first = availableOtherVariables
                second = availableColorVariables
                third = availableSizeVariables
            }
        } else {
            if (availableSizeVariables.size > availableOtherVariables.size) {
                first = availableSizeVariables
                if (availableColorVariables.size > availableOtherVariables.size) {
                    second = availableColorVariables
                    third = availableOtherVariables
                } else {
                    second = availableOtherVariables
                    third = availableColorVariables
                }
            } else {
                first = availableOtherVariables
                second = availableSizeVariables
                third = availableColorVariables
            }
        }

        val requestedArray = ArrayList<ArrayList<Pair<String, String>>>()


        for (item1 in first) {
            var combination = arrayListOf<Pair<String, String>>()
            combination.add(Pair(item1.variantId!!, item1.type!!))

            if (second.size > 0) {
                for (item2 in second) {
                    combination = arrayListOf<Pair<String, String>>()
                    combination.add(Pair(item1.variantId!!, item1.type!!))
                    combination.add(Pair(item2.variantId!!, item2.type!!))

                    if (third.size > 0) {
                        for (item3 in third) {
                            combination = arrayListOf<Pair<String, String>>()
                            combination.add(Pair(item1.variantId!!, item1.type!!))
                            combination.add(Pair(item2.variantId!!, item2.type!!))
                            combination.add(Pair(item3.variantId!!, item3.type!!))

                            requestedArray.add(combination)
                        }
                    } else {
                        requestedArray.add(combination)
                    }
                }
            } else {
                requestedArray.add(combination)
            }
        }

        if (requestedArray.isEmpty()) {
            hasFetchedProductVariantsBefore = true
        }

        var responseCounter = 0
        for (requestItem in requestedArray) {

            getProductVariations(
                merchantId = product.merchantID!!,
                productId = product.productID!!,
                variations = requestItem.toTypedArray(),
                object : CallbackListener<Array<ProductVariationsResponseModel>>() {
                    override fun onAPICallFinished(data: Array<ProductVariationsResponseModel>) {
                        responseCounter++

                        variations.addAll(data)

                        if (responseCounter == requestedArray.size) {
                            hasFetchedProductVariantsBefore = true
                            listener.onAPICallFinished(Unit)
                        }
                    }

                    override fun onAPICallFailed() {
                        responseCounter++

                        if (responseCounter == requestedArray.size) {
                            hasFetchedProductVariantsBefore = true
                            listener.onAPICallFinished(Unit)
                        }
                    }
                }
            )
        }
    }

    private fun getProductVariations(
        merchantId: String,
        productId: String,
        variations: Array<Pair<String, String>>,
        listener: CallbackListener<Array<ProductVariationsResponseModel>>
    ) {
        productInteractor.variations(
            merchantId,
            productId,
            variations = variations,
            object : Subscriber<ProductVariationsResponse> {
                override fun onRequestSuccess(response: ProductVariationsResponse) {
                    if (response.response == ProductVariationsResponse.Result.SUCCESS) {

                        if ((response.variations ?: arrayOf()).isNotEmpty()) {
                            listener.onAPICallFinished(response.variations!!)
                        } else {
                            listener.onAPICallFailed()
                        }
                    } else {
                        listener.onAPICallFailed()
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    listener.onAPICallFailed()
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            }
        )
    }

    //region Full Screen Photo
    override fun subscribeView(view: FullScreenPhotoView) {

    }

    override fun disposeView(view: FullScreenPhotoView) {

    }

    override fun getProductFiles(): Pair<FileMode, ArrayList<String>> {

        val variation = getVariation()
        var fileList: ArrayList<String>? = null
        var mode = FileMode.ID
//            ((variation?.fileUrls ?: variation?.files) ?: ((product.fileUrls ?: product.files) ?: arrayOf())).toCollection(ArrayList())

        if (variation != null) {
            if (!variation.fileUrls.isNullOrEmpty()) {
                mode = FileMode.URL
                fileList = variation.fileUrls!!.toCollection(ArrayList())
            } else if (!variation.files.isNullOrEmpty()) {
                mode = FileMode.ID
                fileList = variation.files!!.toCollection(ArrayList())
            }
        } else {
            if (!product.fileUrls.isNullOrEmpty()) {
                mode = FileMode.URL
                fileList = product.fileUrls!!.toCollection(ArrayList())
            } else if (!product.files.isNullOrEmpty()) {
                mode = FileMode.ID
                fileList = product.files!!.toCollection(ArrayList())
            }
        }

        return Pair(mode, fileList ?: arrayListOf())

    }

    override fun onDismissFullScreenPhotoClicked() {
        state = State.PRODUCT_FEATURES
    }
    //endregion

    //region Product Features About Item

    override fun subscribeView(view: ProductFeaturesAboutItemView) {

    }

    override fun disposeView(view: ProductFeaturesAboutItemView) {

    }

    override fun onDismissAboutItemClicked() {
        state = State.PRODUCT_FEATURES
    }
    //endregion
}
