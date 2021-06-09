package com.greenwallet.business.network.productReviews

import android.util.Log
import com.greenwallet.business.network.*
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.network.restapi.IRestApi

open class ProductReviewInteractor(private var api: IRestApi?) : IProductReviewInteractor {

    override fun reviews(
        productId: String,
        listener: Subscriber<ProductReviewsResponse>
    ): Disposable? {
        return api?.productReviews(productId = productId)
            ?.execute(object : RequestSubscriber<Array<ProductReviewsResponseModel>>() {
                override fun onUnprocessableEntity() {
                    val result =
                        ProductReviewsResponse(ProductReviewsResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onSuccess(response: Array<ProductReviewsResponseModel>) {
                    val result =
                        ProductReviewsResponse(ProductReviewsResponse.Result.SUCCESS)

                    result.reviews = response

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onUnauthorizedError() {
                    listener.onUserUnauthorized()

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(
                        ProductReviewsResponse(
                            ProductReviewsResponse.Result.ERROR,
                            ResponseError.ERROR_SERVER_500
                        )
                    )

                    Log.e("Request", "onServerError")
                }

                override fun onExpectedError(response: String) {
                    if (response == NetworkException.REASON_404) {
                        val result =
                            ProductReviewsResponse(ProductReviewsResponse.Result.SUCCESS)

                        result.reviews = arrayOf()

                        listener.onRequestSuccess(result)
                    } else {
                        val result =
                            ProductReviewsResponse(ProductReviewsResponse.Result.ERROR)

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

}