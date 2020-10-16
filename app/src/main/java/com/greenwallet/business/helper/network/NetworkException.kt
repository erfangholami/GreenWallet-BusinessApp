package com.greenwallet.business.helper.network


class NetworkException(override var message: String) : Exception(message) {

    companion object {
        const val REASON_UNKNOWN = "Unknown reason"
        const val REASON_401 = "401" //unauthorized error
        const val REASON_403 = "403" //forbidden
        const val REASON_404 = "404" //not found
        const val REASON_422 = "422" //unprocessable entity
        const val REASON_400 = "400" //client error (invalid fields)
        const val REASON_500 = "500"
    }
}