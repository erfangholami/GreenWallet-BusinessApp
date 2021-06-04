package com.greenwallet.business.helper.network.campaings

import android.util.Log
import com.greenwallet.business.helper.network.Disposable
import com.greenwallet.business.helper.network.RequestSubscriber
import com.greenwallet.business.helper.network.ResponseError
import com.greenwallet.business.helper.network.Subscriber
import com.greenwallet.business.helper.network.campaings.response.CampaingsResponseModel
import com.greenwallet.business.helper.network.restapi.IRestApi


open class CampaignsInteractor(private var api: IRestApi?) : ICampaignsInteractor {

    override fun campaigns(
        listener: Subscriber<CampaignsResponse>
    ): Disposable? {
        return api?.campaigns()
            ?.execute(object : RequestSubscriber<Array<CampaingsResponseModel>>() {
                override fun onUnprocessableEntity() {
                    val result = CampaignsResponse(null, CampaignsResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onSuccess(response: Array<CampaingsResponseModel>) {
                    val result = CampaignsResponse(response, CampaignsResponse.Result.SUCCESS)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onUnauthorizedError() {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(
                        CampaignsResponse(
                            null,
                            CampaignsResponse.Result.ERROR,
                            ResponseError.ERROR_SERVER_500
                        )
                    )

                    Log.e("Request", "onServerError")
                }

                override fun onExpectedError(response: String) {
                    val result = CampaignsResponse(null, CampaignsResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onExpectedError")
                }

                override fun onUnexpectedError(t: Throwable) {
                    listener.onRequestFailure(t)

                    Log.e("Request", "onUnexpectedError")
                }
            })
    }
}