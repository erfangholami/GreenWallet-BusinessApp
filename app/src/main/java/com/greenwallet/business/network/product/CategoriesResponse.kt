package com.greenwallet.business.network.product

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.product.response.CategoriesResponseModel

class CategoriesResponse(val result: Array<CategoriesResponseModel>?, val response : Result, val error: ResponseError = ResponseError.NONE) {

    enum class Result {
        ERROR,
        SUCCESS
    }
}