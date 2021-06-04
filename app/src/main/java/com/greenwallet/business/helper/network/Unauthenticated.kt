package com.greenwallet.business.helper.network

import okhttp3.Request

class Unauthenticated(private val gatewayKey: String) : IAuthenticator {

    override fun getTokenId(): String {
        return ""
    }

    override fun addAuthHeaders(original: Request): Request {
        return original.newBuilder()
            .addHeader("x-api-key", gatewayKey)
            .build()
    }

    override fun refreshAuthentication(): Boolean {
        return false
    }
}