package com.greenwallet.business.scenes.basket.ui

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.CartProduct

interface BasketFeatureView {

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: BasketFeatureView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: BasketFeatureView)

        fun onBackPressed()

        fun onProceedCheckoutClicked()

        fun getCartProductList(): MutableList<CartProduct>

        fun fetchImage(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>?,
            reduceQuality: Boolean
        )

        fun deleteAnItem(cartProduct: CartProduct)
    }
}