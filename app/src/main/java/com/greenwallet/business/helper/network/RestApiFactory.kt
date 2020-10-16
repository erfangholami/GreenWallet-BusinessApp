package com.greenwallet.business.helper.network

import com.greenwallet.business.helper.network.restapi.IRestApi
import okhttp3.logging.HttpLoggingInterceptor

interface RestApiFactory {
    fun getRestApi(): IRestApi

    fun isRestApiReady(): Boolean

    fun initAuthentication(authenticator: IAuthenticator, logger: HttpLoggingInterceptor? = null)
    fun getUnauthenticatedApi(): IRestApi
}