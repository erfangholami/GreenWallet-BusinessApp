package com.greenwallet.business.network.restapi

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.NetworkException
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.campaings.response.CampaingsResponseModel
import com.greenwallet.business.network.login.request.LoginRequestModel
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import com.greenwallet.business.network.login.response.LoginResponseModel
import com.greenwallet.business.network.product.response.ProductResponseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RestApi(private val api: RestApiConnector) : IRestApi {

    override fun login(requestModel: LoginRequestModel): IRestApi.NetworkCall<LoginResponseModel> {
        return RetrofitNetworkCall(api.login(requestModel))
    }

    override fun categories(merchantId: String): IRestApi.NetworkCall<Array<CategoriesResponseModel>> {
        return RetrofitNetworkCall(api.categories(merchantId))
    }

    override fun productsHotDeals(
        merchantId: String,
        offset: Int,
        size: Int,
    ): IRestApi.NetworkCall<Array<ProductResponseModel>> {
        return RetrofitNetworkCall(
            api.productsHotDeals(
                offset = offset.toString(),
                size = size.toString(),
                query = merchantId
            )
        )
    }

    override fun campaigns(): IRestApi.NetworkCall<Array<CampaingsResponseModel>> {
        return RetrofitNetworkCall(api.campaigns())
    }

    override fun file(id: String): IRestApi.NetworkCall<ResponseBody> {
        return RetrofitNetworkCall(api.file(id))
    }

    private val gson = GsonBuilder().create()

    private inner class RetrofitNetworkCall<V>(private val call: Call<V>) : IRestApi.NetworkCall<V> {

        override fun executeSynchronous(subscriber: Subscriber<V>) {
            this.subscriber = subscriber
            try {
                handleResponse(call.execute())
            } catch (ex: IOException) {
                subscriber.onRequestFailure(ex)
            } catch (ex: RuntimeException) {
                subscriber.onRequestFailure(ex)
            }
        }

        private var disposed: Boolean = false

        private lateinit var subscriber: Subscriber<V>

        override fun execute(subscriber: Subscriber<V>): Disposable {
            this.subscriber = subscriber

            call.enqueue(object : Callback<V> {
                override fun onResponse(call: Call<V>, response: Response<V>) {
                    if (!disposed) {
                        handleResponse(response)
                    }
                }

                override fun onFailure(call: Call<V>, t: Throwable) {
                    if (!disposed) {
                        subscriber.onRequestFailure(t)
                    }
                }
            })
            return object : Disposable {
                override fun dispose() {
                    disposed = true
                    call.cancel()
                }

            }
        }

        fun handleResponse(response: Response<V>) {
            when {
                response.raw() == null -> subscriber.onRequestFailure(IOException("Unknown response"))
                response.raw().code() == 400 -> {
                    if (response.errorBody() == null) {
                        subscriber.onRequestFailure(IOException("" + response.raw().code()))
                    } else {
                        try {
                            subscriber.onRequestFailure(NetworkException(NetworkException.REASON_400))
                        } catch (ex: JsonSyntaxException) {
                            subscriber.onRequestFailure(IOException("While trying to handle a " + response.raw().code() + " response a JsonSyntaxException occurred: " + ex.message))
                        }

                    }
                }
                response.raw().code() >= 500 -> subscriber.onRequestFailure(
                    NetworkException(
                        NetworkException.REASON_500)
                )
                response.raw().code() == 401 -> {
                    try {
                        subscriber.onRequestFailure(NetworkException(NetworkException.REASON_401))
                    } catch (ex: JsonSyntaxException) {
                        subscriber.onRequestFailure(NetworkException(NetworkException.REASON_401))
                    }
                }
                response.raw().code() == 403 -> {
                    try {
                        subscriber.onRequestFailure(NetworkException(NetworkException.REASON_403))

                    } catch (ex: JsonSyntaxException) {
                        subscriber.onRequestFailure(NetworkException(NetworkException.REASON_403))
                    }
                }
                response.raw().code() != 200 -> subscriber.onRequestFailure(IOException("" + response.raw().code()))
                response.raw().code() == 200 && response.body() == null -> subscriber.onRequestFailure(
                    NetworkException(NetworkException.REASON_500)
                )
                else -> subscriber.onRequestSuccess(response.body()!!)
            }
        }
    }
}