package com.greenwallet.business.network.restapi

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.network.login.request.LoginRequestModel
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import com.greenwallet.business.network.login.response.LoginResponseModel
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import okhttp3.ResponseBody

interface IRestApi {

    fun login(requestModel: LoginRequestModel): NetworkCall<LoginResponseModel>

    fun search(
        query: String,
        offset: Int,
        size: Int,
    ): NetworkCall<Array<ProductResponseModel>>

    fun categories(merchantId: String): NetworkCall<Array<CategoriesResponseModel>>

    fun productsByCategory(
        categoryName: String,
        offset: Int,
        size: Int,
    ): NetworkCall<Array<ProductResponseModel>>

    fun productsHotDeals(
        merchantId: String,
        offset: Int,
        size: Int,
    ): NetworkCall<Array<ProductResponseModel>>

    fun productsBestSellers(
        merchantId: String,
        offset: Int,
        size: Int,
    ): NetworkCall<Array<ProductResponseModel>>

    fun campaigns(): NetworkCall<Array<CampaignsResponseModel>>

    fun file(id: String): NetworkCall<ResponseBody>

    fun productReviews(productId: String): NetworkCall<Array<ProductReviewsResponseModel>>

    interface NetworkCall<T> {
        fun execute(s: Subscriber<T>): Disposable
        fun executeSynchronous(s: Subscriber<T>)
    }
}