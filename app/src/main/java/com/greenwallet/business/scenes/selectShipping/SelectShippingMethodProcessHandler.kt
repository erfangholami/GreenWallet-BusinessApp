package com.greenwallet.business.scenes.selectShipping

import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.scenes.base.BaseProcessHandler

interface SelectShippingMethodProcessHandler : BaseProcessHandler {

    fun showShippingMethodSelectionScreen()

    fun dismiss(product: CartProduct)
}