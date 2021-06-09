package com.greenwallet.business.network.product

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.product.response.ProductVariationsResponseModel

class ProductVariationsResponse(
    val response: Result,
    val error: ResponseError = ResponseError.NONE
) {

    var variations: Array<ProductVariationsResponseModel>? = null

    enum class Result {
        ERROR,
        SUCCESS
    }
}
