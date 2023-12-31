package com.greenwallet.business.scenes.shopgreen

import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.scenes.base.BaseProcessHandler

interface ShopGreenProcessHandler : BaseProcessHandler {

    fun showShopGreenScreen()

    fun showLoadingScreen()

    fun showErrorMessage()

    fun showSearchProductsScreen(searchQuery: String)

    fun showCategoryProductListScreen(category: CategoriesResponseModel)

    fun showRedeemListScreen()

    fun showBestSellerListScreen()

    fun onProductClicked(productModel: ProductResponseModel)

    fun showBasketScreen()

    fun showCampaignListScreen()

    fun showCampaignDetails(campaignsResponseModel: CampaignsResponseModel)
}