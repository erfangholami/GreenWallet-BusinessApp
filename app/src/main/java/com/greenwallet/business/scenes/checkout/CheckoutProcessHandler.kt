package com.greenwallet.business.scenes.checkout

import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.scenes.base.BaseProcessHandler

interface CheckoutProcessHandler : BaseProcessHandler {

    fun showCheckoutScreen()

    fun onBackPressed()

    fun showLoadingScreen()

    fun showErrorMessage()

    fun showShippingMethodSelectionScreen(productItem: CartProduct)
}