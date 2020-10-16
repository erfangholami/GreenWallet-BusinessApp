package com.greenwallet.business.scenes.login

interface LoginProcessHandler {

    fun showLoginScreen()

    fun showShopGreenScreen()

    fun showLoadingScreen()

    fun showErrorMessage()
}