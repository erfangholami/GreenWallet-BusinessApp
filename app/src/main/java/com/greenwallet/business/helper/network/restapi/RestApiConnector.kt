package com.greenwallet.business.helper.network.restapi

import com.greenwallet.business.helper.network.campaings.response.CampaingsResponseModel
import com.greenwallet.business.helper.network.dealsNoDeals.response.CategoriesResponseModel
import com.greenwallet.business.helper.network.login.request.LoginRequestModel
import com.greenwallet.business.helper.network.login.response.LoginResponseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RestApiConnector {

    @POST("login")
    fun login(@Body requestModel: LoginRequestModel): Call<LoginResponseModel>

    @GET("merchant/search/products/categories")
    fun categories(@Query("merchant_id") merchantId: String): Call<Array<CategoriesResponseModel>>

    @GET("campaigns/loyalty_reward")
    fun campaigns(): Call<Array<CampaingsResponseModel>>

    @GET("file/{fileId}")
    fun file(@Path("fileId") fileId: String): Call<ResponseBody>
}