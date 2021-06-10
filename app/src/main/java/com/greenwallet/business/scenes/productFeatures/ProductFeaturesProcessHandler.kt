package com.greenwallet.business.scenes.productFeatures

import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BaseProcessHandler


interface ProductFeaturesProcessHandler : BaseProcessHandler {

    fun onBackPressed()

    fun showProductFeaturesScreen()

    fun showFullScreenPhotoScreen()

    fun showAboutThisItem(data: String)

    fun onSeeAllReviewsClicked(productID: String, reviews: ArrayList<ProductReviewsResponseModel>?)

    fun showBasketScreen()

    fun showCheckoutScreen()

    fun showFindMySizeScreen()

    fun showAddToCartSuccessDialog()
}