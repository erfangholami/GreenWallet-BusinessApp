package com.greenwallet.business.scenes.basket

import com.greenwallet.business.scenes.base.BaseProcessHandler

interface BasketProcessHandler : BaseProcessHandler {

    fun onBackPressed()

    fun showBasketFeatureScreen()

    fun onProceedCheckoutCLick()
}