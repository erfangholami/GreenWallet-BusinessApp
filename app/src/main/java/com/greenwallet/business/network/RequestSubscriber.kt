package com.greenwallet.business.network

abstract class RequestSubscriber<T> : Subscriber<T> {

    override fun onRequestFailure(t: Throwable) {
        if (t is NetworkException) {
            //handle the error
            if (t.message == NetworkException.REASON_400) {
                onExpectedError(t.message)
            } else if (t.message == NetworkException.REASON_500 || t.message == NetworkException.REASON_403) {
                onServerError()
            } else if (t.message == NetworkException.REASON_401) {
                onUnauthorizedError()
            } else {
                onUnexpectedError(t)
            }
        } else {
            if (t.message == NetworkException.REASON_422 || t.message == NetworkException.REASON_404) {
                onUnprocessableEntity()
            } else {
                onUnexpectedError(t)
            }
        }
    }

    final override fun onRequestSuccess(response: T) {
        onSuccess(response)
    }

    override fun onUserUnauthorized() {}

    abstract fun onSuccess(response: T)
    abstract fun onExpectedError(response: String)
    abstract fun onServerError()
    abstract fun onUnprocessableEntity()
    abstract fun onUnauthorizedError()
    abstract fun onUnexpectedError(t: Throwable)
}