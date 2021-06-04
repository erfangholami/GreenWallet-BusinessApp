package com.greenwallet.business.network

import okhttp3.Request

class Authenticated(private val token: String) : IAuthenticator {

    override fun getTokenId(): String {
        return ""
    }

    override fun addAuthHeaders(original: Request): Request {
        return original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
    }

    override fun refreshAuthentication(): Boolean {
        return false
    }
}