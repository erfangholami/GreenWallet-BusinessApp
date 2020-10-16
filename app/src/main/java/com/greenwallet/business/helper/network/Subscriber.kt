package com.greenwallet.business.helper.network

interface Subscriber<T> {
    fun onRequestSuccess(response: T)
    fun onRequestFailure(t: Throwable)
}

interface Disposable {
    fun dispose()
}