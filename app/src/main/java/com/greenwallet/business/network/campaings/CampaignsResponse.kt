package com.greenwallet.business.network.campaings

import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.campaings.response.CampaingsResponseModel

class CampaignsResponse(val result: Array<CampaingsResponseModel>?, val response : Result, val error: ResponseError = ResponseError.NONE) {

    enum class Result {
        ERROR,
        SUCCESS
    }
}