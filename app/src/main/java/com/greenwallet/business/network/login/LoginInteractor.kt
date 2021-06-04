package com.greenwallet.business.network.login

import android.util.Log
import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.RequestSubscriber
import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.login.request.LoginRequestModel
import com.greenwallet.business.network.login.response.LoginResponseModel
import com.greenwallet.business.network.restapi.IRestApi

open class LoginInteractor(private var api : IRestApi?) : ILoginInteractor {

    override fun login(
        email: String,
        password: String,
        listener: Subscriber<LoginResponse>
    ): Disposable? {
        val request = LoginRequestModel()

        request.email = email.toLowerCase()
        request.password = password

        return api?.login(request)
            ?.execute(object : RequestSubscriber<LoginResponseModel>() {
                override fun onUnprocessableEntity() {
                    val result = LoginResponse(null, null, LoginResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onSuccess(response: LoginResponseModel) {
                    val result = LoginResponse(response.user_id, response.merchant_id, LoginResponse.Result.SUCCESS)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onUnauthorizedError() {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(LoginResponse(null, null, LoginResponse.Result.ERROR, ResponseError.ERROR_SERVER_500))

                    Log.e("Request", "onServerError")
                }

                override fun onExpectedError(response: String) {
                    val result = LoginResponse(null, null, LoginResponse.Result.ERROR)

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