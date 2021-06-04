package com.greenwallet.business.network

abstract class CallbackListener<T> {
    abstract fun onAPICallFinished(data: T)
    open fun onAPICallFailed() {}
}