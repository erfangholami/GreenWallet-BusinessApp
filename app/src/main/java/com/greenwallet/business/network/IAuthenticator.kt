package com.greenwallet.business.network

import okhttp3.Request

interface IAuthenticator {

    fun addAuthHeaders(original: Request): Request

    fun refreshAuthentication(): Boolean

    fun getTokenId(): String
}