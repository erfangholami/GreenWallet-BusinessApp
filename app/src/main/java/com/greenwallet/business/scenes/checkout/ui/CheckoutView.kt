package com.greenwallet.business.scenes.checkout.ui

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.scenes.basket.model.CheckoutDetailsItem
import com.greenwallet.business.scenes.basket.model.CheckoutItem

interface CheckoutView {

    fun updateValues()

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: CheckoutView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: CheckoutView)

        fun onEditCheckoutItemClicked()

        fun getCheckoutDetails(): CheckoutDetailsItem

        fun getCheckoutItems(): MutableList<CheckoutItem>

        fun updateProductCount(cartProduct: CartProduct)

        fun fetchImage(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>?,
            reduceQuality: Boolean
        )

        fun onBackPressed()

        fun onDismissError()

        fun onItemEditShippingClicked(productItem: CartProduct)
    }
}