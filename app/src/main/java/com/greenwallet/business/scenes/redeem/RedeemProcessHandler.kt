package com.greenwallet.business.scenes.redeem

import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.scenes.base.BaseProcessHandler

interface RedeemProcessHandler : BaseProcessHandler {

    fun showRedeemScreen()

    fun dismiss()

    fun showLoadingScreen()

    fun showErrorMessage()

    fun showProductDetailsScreen(selectedProduct: ProductResponseModel)

    fun shoeCartScreen()
}