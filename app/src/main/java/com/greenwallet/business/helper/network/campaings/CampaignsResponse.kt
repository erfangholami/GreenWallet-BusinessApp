package com.greenwallet.business.helper.network.campaings

import com.greenwallet.business.helper.network.ResponseError
import com.greenwallet.business.helper.network.campaings.response.CampaingsResponseModel

class CampaignsResponse(
    val result: Array<CampaingsResponseModel>?,
    val response: Result,
    val error: ResponseError = ResponseError.NONE
) {

    enum class Result {
        ERROR,
        SUCCESS
    }
}