package com.greenwallet.business.scenes.shopgreen

import com.greenwallet.business.scenes.base.BaseProcessHandler

interface ShopGreenProcessHandler: BaseProcessHandler {

    fun showShopGreenScreen()

    fun showLoadingScreen()

    fun showErrorMessage()
}