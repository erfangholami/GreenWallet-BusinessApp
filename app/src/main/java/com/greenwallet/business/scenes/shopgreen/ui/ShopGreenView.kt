package com.greenwallet.business.scenes.shopgreen.ui

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel

interface ShopGreenView {

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: ShopGreenView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: ShopGreenView)

        fun onCartListClicked()

        fun onShowAllRedeemOptionsClicked()

        fun fetchImage(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>?,
            reduceQuality: Boolean
        )

        fun fetchReviews(
            id: String,
            listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
        )

        fun onSearchTextChanged(searchQuery: String)

        fun onProductClicked(productModel: ProductResponseModel)

        fun getBestSellerItems(): ArrayList<ProductResponseModel>

        fun getRedeemOptions(): ArrayList<ProductResponseModel>

        fun getCategoryList(): ArrayList<String>

        fun onCampaignClicked(campaignsResponseModel: CampaignsResponseModel)

        fun onShowAllCampaignsClicked()

        fun onCategoryItemClicked(categoryName: String)

        fun getCampaigns(): ArrayList<CampaignsResponseModel>

        fun onShowAllBestSellersClicked()

        fun onProductReviewClicked(
            productId: String,
            reviews: java.util.ArrayList<ProductReviewsResponseModel>
        )

        fun createOnboardingItems(): List<HomeOnboardingItem>
    }
}