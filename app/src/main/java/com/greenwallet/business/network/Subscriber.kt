package com.greenwallet.business.network

interface Subscriber<T> {
    fun onRequestSuccess(response: T)
    fun onRequestFailure(t: Throwable)
}

interface Disposable {
    fun dispose()
}