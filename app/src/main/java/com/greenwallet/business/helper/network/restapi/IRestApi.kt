package com.greenwallet.business.helper.network.restapi

import com.greenwallet.business.helper.network.Disposable
import com.greenwallet.business.helper.network.Subscriber
import com.greenwallet.business.helper.network.campaings.response.CampaingsResponseModel
import com.greenwallet.business.helper.network.login.request.LoginRequestModel
import com.greenwallet.business.helper.network.dealsNoDeals.response.CategoriesResponseModel
import com.greenwallet.business.helper.network.files.response.FilesResponseModel
import com.greenwallet.business.helper.network.login.response.LoginResponseModel
import okhttp3.Response
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