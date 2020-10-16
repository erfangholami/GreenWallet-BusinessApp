package com.greenwallet.business.scenes.base

interface BaseViewModel<in T> {
    fun subscribeView(view: T)
    fun disposeView(view: T)
}