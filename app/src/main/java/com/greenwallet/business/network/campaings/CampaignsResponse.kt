package com.greenwallet.business.network.campaings

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel

class CampaignsResponse(val result: Array<CampaignsResponseModel>?, val response : Result, val error: ResponseError = ResponseError.NONE) {

    enum class Result {
        ERROR,
        SUCCESS
    }
}