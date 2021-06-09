package com.greenwallet.business.network.product

import android.util.Log
import com.greenwallet.business.network.*
import com.greenwallet.business.network.product.response.*
import com.greenwallet.business.network.restapi.IRestApi

open class ProductInteractor(private var api : IRestApi?) :
    IProductInteractor {

    override fun search(
        query: String,
        offset: Int,
        size: Int,
        listener: Subscriber<ProductResponse>
    ): Disposable? {
        return api?.search(query, offset, size)
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

    override fun productsByCategory(
        categoryName: String,
        offset: Int,
        size: Int,
        listener: Subscriber<ProductResponse>
    ): Disposable? {
        return api?.productsByCategory(categoryName, offset, size)
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

    override fun shipments(
        merchantId: String,
        productId: String,
        listener: Subscriber<ProductShipmentsResponse>
    ): Disposable? {
        return api?.productShipments(merchantId = merchantId, productId = productId)
            ?.execute(object : RequestSubscriber<Array<ProductShipmentsResponseModel>>() {
                override fun onUnprocessableEntity() {
                    val result =
                        ProductShipmentsResponse(ProductShipmentsResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onSuccess(response: Array<ProductShipmentsResponseModel>) {
                    val result =
                        ProductShipmentsResponse(ProductShipmentsResponse.Result.SUCCESS)

                    result.shipments = response

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onUnauthorizedError() {
                    listener.onUserUnauthorized()

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(
                        ProductShipmentsResponse(
                            ProductShipmentsResponse.Result.ERROR,
                            ResponseError.ERROR_SERVER_500
                        )
                    )

                    Log.e("Request", "onServerError")
                }

                override fun onExpectedError(response: String) {
                    val result =
                        ProductShipmentsResponse(ProductShipmentsResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onExpectedError")
                }

                override fun onUnexpectedError(t: Throwable) {
                    listener.onRequestFailure(t)

                    Log.e("Request", "onUnexpectedError")
                }
            })
    }

    override fun variants(
        merchantId: String,
        productId: String,
        listener: Subscriber<ProductVariantsResponse>
    ): Disposable? {
        return api?.productVariants(merchantId = merchantId, productId = productId)
            ?.execute(object : RequestSubscriber<Array<ProductVariantsResponseModel>>() {
                override fun onUnprocessableEntity() {
                    val result =
                        ProductVariantsResponse(ProductVariantsResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onSuccess(response: Array<ProductVariantsResponseModel>) {
                    val result =
                        ProductVariantsResponse(ProductVariantsResponse.Result.SUCCESS)

                    result.variants = response

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onUnauthorizedError() {
                    listener.onUserUnauthorized()

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(
                        ProductVariantsResponse(
                            ProductVariantsResponse.Result.ERROR,
                            ResponseError.ERROR_SERVER_500
                        )
                    )

                    Log.e("Request", "onServerError")
                }

                override fun onExpectedError(response: String) {
                    if (response == NetworkException.REASON_404) {
                        val result =
                            ProductVariantsResponse(ProductVariantsResponse.Result.SUCCESS)

                        result.variants = arrayOf()

                        listener.onRequestSuccess(result)
                    } else {
                        val result =
                            ProductVariantsResponse(ProductVariantsResponse.Result.ERROR)

                        listener.onRequestSuccess(result)
                    }

                    Log.e("Request", "onExpectedError")
                }

                override fun onUnexpectedError(t: Throwable) {
                    listener.onRequestFailure(t)

                    Log.e("Request", "onUnexpectedError")
                }
            })
    }

    override fun variations(
        merchantId: String,
        productId: String,
        variations: Array<Pair<String, String>>,
        listener: Subscriber<ProductVariationsResponse>
    ): Disposable? {
        return api?.productVariations(
            merchantId = merchantId,
            productId = productId,
            variations = variations
        )
            ?.execute(object : RequestSubscriber<Array<ProductVariationsResponseModel>>() {
                override fun onUnprocessableEntity() {
                    val result =
                        ProductVariationsResponse(ProductVariationsResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onSuccess(response: Array<ProductVariationsResponseModel>) {
                    val result =
                        ProductVariationsResponse(ProductVariationsResponse.Result.SUCCESS)

                    result.variations = response

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onUnauthorizedError() {
                    listener.onUserUnauthorized()

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(
                        ProductVariationsResponse(
                            ProductVariationsResponse.Result.ERROR,
                            ResponseError.ERROR_SERVER_500
                        )
                    )

                    Log.e("Request", "onServerError")
                }

                override fun onExpectedError(response: String) {
                    val result =
                        ProductVariationsResponse(ProductVariationsResponse.Result.ERROR)

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