package com.greenwallet.business.network.product

import android.util.Log
import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.RequestSubscriber
import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import com.greenwallet.business.network.restapi.IRestApi
import com.greenwallet.business.network.product.response.ProductResponseModel

open class ProductInteractor(private var api : IRestApi?) :
    IProductInteractor {

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
                    listener.onUserUnauthorized()

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(CategoriesResponse(null, CategoriesResponse.Result.ERROR, ResponseError.ERROR_SERVER_500))

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

    override fun hotDeals(
        merchantId: String,
        offset: Int,
        size: Int,
        listener: Subscriber<ProductResponse>
    ): Disposable? {
        return api?.productsHotDeals(merchantId, offset, size)
            ?.execute(object : RequestSubscriber<Array<ProductResponseModel>>() {
                override fun onSuccess(response: Array<ProductResponseModel>) {
                    val result =
                        ProductResponse(ProductResponse.Result.SUCCESS)

                    result.products = response

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onExpectedError(response: String) {
                    val result =
                        ProductResponse(ProductResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onExpectedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(
                        ProductResponse(
                            ProductResponse.Result.ERROR,
                            ResponseError.ERROR_SERVER_500
                        )
                    )

                    Log.e("Request", "onServerError")
                }

                override fun onUnprocessableEntity() {
                    val result =
                        ProductResponse(ProductResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onUnauthorizedError() {
                    listener.onUserUnauthorized()

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onUnexpectedError(t: Throwable) {
                    listener.onRequestFailure(t)

                    Log.e("Request", "onUnexpectedError")
                }
            })
    }

    override fun bestSellers(
        merchantId: String,
        offset: Int,
        size: Int,
        listener: Subscriber<ProductResponse>
    ): Disposable? {
        return api?.productsBestSellers(merchantId, offset, size)
            ?.execute(object : RequestSubscriber<Array<ProductResponseModel>>() {
                override fun onSuccess(response: Array<ProductResponseModel>) {
                    val result =
                        ProductResponse(ProductResponse.Result.SUCCESS)

                    result.products = response

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onExpectedError(response: String) {
                    val result =
                        ProductResponse(ProductResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onExpectedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(
                        ProductResponse(
                            ProductResponse.Result.ERROR,
                            ResponseError.ERROR_SERVER_500
                        )
                    )

                    Log.e("Request", "onServerError")
                }

                override fun onUnprocessableEntity() {
                    val result =
                        ProductResponse(ProductResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onUnauthorizedError() {
                    listener.onUserUnauthorized()

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onUnexpectedError(t: Throwable) {
                    listener.onRequestFailure(t)

                    Log.e("Request", "onUnexpectedError")
                }
            })
    }
}