package com.greenwallet.business.network.product

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.product.response.ProductShipmentsResponseModel

class ProductShipmentsResponse(
    val result: Result,
    val error: ResponseError = ResponseError.NONE
) {

    var shipments: Array<ProductShipmentsResponseModel>? = null

    enum class Result {
        ERROR,
        SUCCESS
    }
}
