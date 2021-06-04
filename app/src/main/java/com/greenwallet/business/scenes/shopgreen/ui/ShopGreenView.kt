package com.greenwallet.business.scenes.shopgreen.ui

import android.graphics.Bitmap
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.product.response.ProductReviewsResponseModel
import java.util.*
import kotlin.collections.ArrayList

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

        fun fetchImage(id: String, listener: ImageLoaderListener, sizes: Pair<Int, Int>)

        fun fetchReviews(
            id: String,
            listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
        )

        fun onProductClicked(productModel: ProductResponseModel)

        fun getRedeemOptions(): ArrayList<ProductResponseModel>

        fun getCategoryList(): ArrayList<String>

        fun onCampaignClicked(campaignsResponseModel: CampaignsResponseModel)

        fun onCategoryItemClicked(categoryName: String)

        fun getCampaignsList(): ArrayList<CampaignsResponseModel>
    }
}