package com.greenwallet.business.scenes.selectShipping.ui

import com.greenwallet.business.network.product.response.ProductShipmentsResponseModel

interface SelectShippingMethodView {

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: SelectShippingMethodView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: SelectShippingMethodView)

        fun getShippingOptions(): ArrayList<ProductShipmentsResponseModel>

        fun getSelectedShippingOption(): ProductShipmentsResponseModel?

        fun setSelectedShippingOption(selected: ProductShipmentsResponseModel)

        fun dismiss()
    }
}