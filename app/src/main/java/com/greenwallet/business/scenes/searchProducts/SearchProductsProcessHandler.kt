package com.greenwallet.business.scenes.searchProducts

import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BaseProcessHandler

interface SearchProductsProcessHandler : BaseProcessHandler {

    fun onBackPressed()

    fun showSearchProductsScreen()

    fun showLoadingScreen()

    fun onItemClicked(product: ProductResponseModel)

    fun onItemReviewClicked(productID: String, reviews: ArrayList<ProductReviewsResponseModel>)

    fun shoeCartScreen()
}