package com.greenwallet.business.helper.network.dealsNoDeals

import android.util.Log
import com.greenwallet.business.helper.network.Disposable
import com.greenwallet.business.helper.network.RequestSubscriber
import com.greenwallet.business.helper.network.ResponseError
import com.greenwallet.business.helper.network.Subscriber
import com.greenwallet.business.helper.network.dealsNoDeals.response.CategoriesResponseModel
import com.greenwallet.business.helper.network.restapi.IRestApi

open class DealsNoDealsInteractor(private var api: IRestApi?) : IDealsNoDealsInteractor {

    override fun categories(
        merchantId: String,
        listener: Subscriber<CategoriesResponse>
    ): Disposable? {
        return api?.categories(merchantId)
            ?.execute(object : RequestSubscriber<Array<CategoriesResponseModel>>() {
                override fun onUnprocessableEntity() {
                    val result = CategoriesResponse(null, CategoriesResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onSuccess(response: Array<CategoriesResponseModel>) {
                    val result = CategoriesResponse(response, CategoriesResponse.Result.SUCCESS)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onUnauthorizedError() {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(
                        CategoriesResponse(
                            null,
                            CategoriesResponse.Result.ERROR,
                            ResponseError.ERROR_SERVER_500
                        )
                    )

                    Log.e("Request", "onServerError")
                }

                override fun onExpectedError(response: String) {
                    val result = CategoriesResponse(null, CategoriesResponse.Result.ERROR)

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