package com.greenwallet.business.network.restapi

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.campaings.response.CampaingsResponseModel
import com.greenwallet.business.network.login.request.LoginRequestModel
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import com.greenwallet.business.network.login.response.LoginResponseModel
import okhttp3.ResponseBody

interface IRestApi {

    fun login(requestModel: LoginRequestModel): NetworkCall<LoginResponseModel>

    fun categories(merchantId: String): NetworkCall<Array<CategoriesResponseModel>>

    fun campaigns(): NetworkCall<Array<CampaingsResponseModel>>

    fun file(id: String): NetworkCall<ResponseBody>

    interface NetworkCall<T> {
        fun execute(s: Subscriber<T>): Disposable
        fun executeSynchronous(s: Subscriber<T>)
    }
}