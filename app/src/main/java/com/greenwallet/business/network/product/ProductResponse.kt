package com.greenwallet.business.network.product

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.product.response.ProductResponseModel

class ProductResponse(
    val result: Result,
    val error: ResponseError = ResponseError.NONE
) {

    var products: Array<ProductResponseModel>? = null

    enum class Result {
        ERROR,
        SUCCESS
    }
}