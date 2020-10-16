package com.greenwallet.business.helper.network.login

import com.greenwallet.business.helper.network.ResponseError

class LoginResponse(val userId: String?, val merchant_id: String?, val response : Result, val error: ResponseError = ResponseError.NONE) {

    enum class Result {
        ERROR,
        SUCCESS
    }
}