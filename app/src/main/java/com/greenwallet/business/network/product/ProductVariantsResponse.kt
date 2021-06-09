package com.greenwallet.business.network.product

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.product.response.ProductVariantsResponseModel

class ProductVariantsResponse(
    val response: Result,
    val error: ResponseError = ResponseError.NONE
) {

    var variants: Array<ProductVariantsResponseModel>? = null

    enum class Result {
        ERROR,
        SUCCESS
    }
}
