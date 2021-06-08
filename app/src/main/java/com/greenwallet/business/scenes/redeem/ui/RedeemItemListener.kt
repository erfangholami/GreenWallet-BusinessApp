package com.greenwallet.business.scenes.redeem.ui

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.ProductResponseModel

interface RedeemItemListener {

    fun onLoadImage(
        id: String,
        listener: ImageLoaderListener,
        sizes: Pair<Int, Int>
    )

    fun onItemClicked(
        product: ProductResponseModel
    )
}