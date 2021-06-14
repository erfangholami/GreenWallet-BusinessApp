package com.greenwallet.business.scenes.selectShipping

import android.content.Context
import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.network.product.response.ProductShipmentsResponseModel
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.selectShipping.ui.SelectShippingMethodView

class SelectShippingMethodPresenter(context: Context) :
    BasePresenter<SelectShippingMethodView, SelectShippingMethodProcessHandler>(context),
    SelectShippingMethodView.Presenter {

    enum class State {
        SHIPPING_SELECTION,
        DISMISS,
    }

    var state: State = State.SHIPPING_SELECTION
        set(value) {
            field = value
            activityHandler?.let {
                when (state) {
                    State.SHIPPING_SELECTION -> it.showShippingMethodSelectionScreen()
                    State.DISMISS -> it.dismiss(product)
                }
            }
        }

    lateinit var product: CartProduct

    override fun onViewSubscribed(view: SelectShippingMethodView) {
        state = State.SHIPPING_SELECTION
    }

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun getShippingOptions() = product.shipments

    override fun getSelectedShippingOption() = product.selectedShipmentMethod

    override fun setSelectedShippingOption(selected: ProductShipmentsResponseModel) {
        product.selectedShipmentMethod = selected
    }

    override fun dismiss() {
        state = State.DISMISS
    }
}