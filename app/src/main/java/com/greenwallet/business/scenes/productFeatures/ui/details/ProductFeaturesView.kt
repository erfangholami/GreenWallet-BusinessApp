package com.greenwallet.business.scenes.productFeatures.ui.details

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.product.response.FileMode
import com.greenwallet.business.network.product.response.ProductShipmentsResponseModel
import com.greenwallet.business.network.product.response.ProductVariantsResponseModel
import com.greenwallet.business.network.product.response.ProductVariationsResponseModel
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel

interface ProductFeaturesView {

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: ProductFeaturesView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: ProductFeaturesView)

        fun onFindSizeClicked(sizeMapFileId: String)

        fun onShowMoreClicked(data: String)

        fun onSeeAllReviewsClicked(
            productID: String,
            reviews: ArrayList<ProductReviewsResponseModel>?
        )

        fun onBuyNowClicked()

        fun onAddToCartClicked()

        fun onBasketClicked()

        fun onBackPressed()

        fun onZoomClicked()

        fun fetchImage(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>?,
            reduceQuality: Boolean
        )

        fun getProductShipments(
            merchantId: String,
            productId: String,
            listener: CallbackListener<Array<ProductShipmentsResponseModel>>
        )

        fun fetchReviews(
            productId: String,
            listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
        )

        fun getProductVariants(
            merchantId: String,
            productId: String,
            listener: CallbackListener<Unit>
        )

        fun hasFetchedVariantsBefore(): Boolean

        fun setVariant(variant: ProductVariantsResponseModel): ProductVariationsResponseModel?

        fun setCurrentPicturePosition(currentPagePosition: Int)

        fun getShowingImagePosition(): Int

        fun getAvailableColors(): ArrayList<ProductVariantsResponseModel>

        fun getAvailableSizes(): ArrayList<ProductVariantsResponseModel>

        fun getOtherVariables(): ArrayList<ProductVariantsResponseModel>

        fun getSelectedVariant(variantType: String): ProductVariantsResponseModel?

        fun getVariation(): ProductVariationsResponseModel?

        fun getProductFiles(): Pair<FileMode, ArrayList<String>>
    }
}