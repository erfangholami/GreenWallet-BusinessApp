package com.greenwallet.business.network.campaings

import android.util.Log
import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.RequestSubscriber
import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.network.restapi.IRestApi


open class CampaignsInteractor(private var api: IRestApi?) : ICampaignsInteractor {

    override fun campaigns(
        listener: Subscriber<CampaignsResponse>
    ): Disposable? {
        return api?.campaigns()
            ?.execute(object : RequestSubscriber<Array<CampaignsResponseModel>>() {
                override fun onUnprocessableEntity() {
                    val result = CampaignsResponse(null, CampaignsResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onSuccess(response: Array<CampaignsResponseModel>) {
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