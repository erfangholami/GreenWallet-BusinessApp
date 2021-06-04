package com.greenwallet.business.network.dealsNoDeals

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.dealsNoDeals.response.CategoriesResponseModel

class CategoriesResponse(val result: Array<CategoriesResponseModel>?, val response : Result, val error: ResponseError = ResponseError.NONE) {

    enum class Result {
        ERROR,
        SUCCESS
    }
}