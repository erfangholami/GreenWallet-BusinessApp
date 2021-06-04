package com.greenwallet.business.network.product

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.product.response.ProductResponseModel

class ProductResponse(
    val response: Result,
    val error: ResponseError = ResponseError.NONE
) {

    var products: Array<ProductResponseModel>? = emptyArray()

    enum class Result {
        ERROR,
        SUCCESS
    }
}