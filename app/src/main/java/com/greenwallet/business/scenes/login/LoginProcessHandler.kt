package com.greenwallet.business.scenes.login

import com.greenwallet.business.scenes.base.BaseProcessHandler

interface LoginProcessHandler : BaseProcessHandler {

    fun showLoginScreen()

    fun showShopGreenScreen()

    fun showLoadingScreen()

    fun showErrorMessage()
}