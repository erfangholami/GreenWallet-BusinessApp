package com.greenwallet.business.network

import com.greenwallet.business.network.restapi.IRestApi
import okhttp3.logging.HttpLoggingInterceptor

interface RestApiFactory {
    fun getRestApi(): IRestApi

    fun isRestApiReady(): Boolean

    fun initAuthentication(authenticator: IAuthenticator, logger: HttpLoggingInterceptor? = null)
    fun getUnauthenticatedApi(): IRestApi
}