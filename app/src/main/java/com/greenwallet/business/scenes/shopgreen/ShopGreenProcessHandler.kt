package com.greenwallet.business.scenes.shopgreen

import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.scenes.base.BaseProcessHandler

interface ShopGreenProcessHandler: BaseProcessHandler {

    fun showShopGreenScreen()

    fun showLoadingScreen()

    fun showErrorMessage()

    fun showSearchProductsScreen(searchQuery: String)

    fun showCategoryProductListScreen(categoryName: String)

    fun showRedeemListScreen()

    fun showBestSellerListScreen()

    fun onProductClicked(productModel: ProductResponseModel)
}