package com.greenwallet.business.network.login.request

data class LoginRequestModel(
    var email: String? = null,
    var password: String? = null,
)