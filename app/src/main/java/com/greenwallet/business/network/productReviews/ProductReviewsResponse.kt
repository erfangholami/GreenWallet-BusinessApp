package com.greenwallet.business.network.productReviews

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel

class ProductReviewsResponse(
    val response: Result,
    val error: ResponseError = ResponseError.NONE
) {

    var reviews: Array<ProductReviewsResponseModel>? = null

    enum class Result {
        ERROR,
        SUCCESS
    }
}
