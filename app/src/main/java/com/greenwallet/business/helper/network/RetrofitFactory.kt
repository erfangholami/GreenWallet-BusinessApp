package com.greenwallet.business.helper.network

import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.greenwallet.business.BuildConfig
import com.greenwallet.business.app.Application.Companion.context
import com.greenwallet.business.helper.network.restapi.RestApi
import com.greenwallet.business.helper.network.restapi.RestApiConnector
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit


class RetrofitFactory {
    private var logger: HttpLoggingInterceptor? = null

    companion object {
        private var instance: RetrofitFactory? = null

        fun getInstance(): RetrofitFactory {
            if (instance == null) {
                instance = RetrofitFactory()
                initAPI()
            }
            return instance as RetrofitFactory
        }

        private fun initAPI() {
            val logger = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            } else {
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.NONE
                }
            }

            getInstance().init(
                BuildConfig.GATEWAY_URL + "v1" + "/", Unauthenticated(
                    BuildConfig.GATEWAY_KEY
                ), logger
            )
        }
    }

    fun delete() {
        instance = null
    }

    fun init(url: String, authenticator: IAuthenticator, logger: HttpLoggingInterceptor) {
        this.url = url
        this.logger = logger
        unauthenticatedRetrofit = create(authenticator, logger)
    }

    fun initAuthentication(authenticator: IAuthenticator): RestApi {
        if (this.url == null) {
            throw IllegalStateException("Must call init(String url, Authenticator authenticator, HttpLoggingInterceptor logger) at App start!")
        }

        retrofit = create(authenticator, logger)

        return RestApi(retrofit!!.create(RestApiConnector::class.java))
    }

    private var retrofit: Retrofit? = null
    private var unauthenticatedRetrofit: Retrofit? = null
    private var authenticatedRetrofit: Retrofit? = null

    private val authLock = Any()

    private var url: String? = null

    private fun create(authenticator: IAuthenticator, logger: HttpLoggingInterceptor?): Retrofit {
        val httpClientBuilder = getClientBuilder()

        if (logger != null) {
            httpClientBuilder.addInterceptor(logger)
        } else if (this.logger != null) {
            httpClientBuilder.addInterceptor(this.logger)
        }

        return getRetrofitBuilder(
            addAuthenticator(httpClientBuilder, authenticator)
        )
            .build()
    }

    private fun getClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
    }

    private fun getRetrofitBuilder(clientBuilder: OkHttpClient.Builder): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setDateFormat("yyyy-MM-dd").setLenient().create()
                )
            )
            .baseUrl(url)
            .client(clientBuilder.build())
    }

    private fun addAuthenticator(
        builder: OkHttpClient.Builder,
        authenticator: IAuthenticator
    ): OkHttpClient.Builder {
        builder.addInterceptor(Interceptor { chain ->
            var request = chain.request().newBuilder().build()

            val tokenId = authenticator.getTokenId()
            val response = chain.proceed(authenticator.addAuthHeaders(request))

            if (response.code() === 401) {

                synchronized(authLock) {

                    //check if the access-token was refreshed recently (while Thread was waiting for the authLock)
                    if (tokenId == authenticator.getTokenId()) {
                        //try to refresh the authentication token
                        if (authenticator.refreshAuthentication()) {
                            request = chain.request().newBuilder().build()
                            return@Interceptor chain.proceed(authenticator.addAuthHeaders(request))
                        }
                    }
                }
            }
            response
        })

        val cookieJar: ClearableCookieJar =
            PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
        builder.cookieJar(cookieJar)

        return builder
    }

    fun getUnauthenticatedApi(): RestApi {
        return RestApi(unauthenticatedRetrofit!!.create(RestApiConnector::class.java))
    }

    fun isOpenApiReady(): Boolean {
        return retrofit != null
    }
}