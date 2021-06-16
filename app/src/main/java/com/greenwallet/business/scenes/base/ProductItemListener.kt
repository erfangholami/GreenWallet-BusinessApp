package com.greenwallet.business.scenes.base

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import java.util.*

interface ProductItemListener {
    fun onItemClicked(product: ProductResponseModel)

    fun onItemReviewClicked(productID: String, reviews: ArrayList<ProductReviewsResponseModel>)

    fun fetchImage(id: String, listener: ImageLoaderListener, sizes: Pair<Int, Int>)

    fun fetchReviews(
        id: String,
        listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
    )
}