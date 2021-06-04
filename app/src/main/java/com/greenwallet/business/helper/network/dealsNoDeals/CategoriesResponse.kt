package com.greenwallet.business.helper.network.dealsNoDeals

import com.greenwallet.business.helper.network.ResponseError
import com.greenwallet.business.helper.network.dealsNoDeals.response.CategoriesResponseModel

class CategoriesResponse(
    val result: Array<CategoriesResponseModel>?,
    val response: Result,
    val error: ResponseError = ResponseError.NONE
) {

    enum class Result {
        ERROR,
        SUCCESS
    }
}