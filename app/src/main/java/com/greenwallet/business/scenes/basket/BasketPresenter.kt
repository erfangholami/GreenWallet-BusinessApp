package com.greenwallet.business.scenes.basket

import android.content.Context
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.network.InteractorFactory
import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.basket.ui.BasketFeatureView

class BasketPresenter(context: Context) :
    BasePresenter<BasketFeatureView, BasketProcessHandler>(context),
    BasketFeatureView.Presenter {

    enum class State {
        BASKET_FEATURE,
    }

    var state: State = State.BASKET_FEATURE
        set(value) {
            field = value
            when (state) {
                State.BASKET_FEATURE -> activityHandler?.showBasketFeatureScreen()
            }
        }

    private val productInteractor = InteractorFactory(context).createProductInteractor()

    override fun onViewSubscribed(view: BasketFeatureView) {
        state = State.BASKET_FEATURE
    }

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun getCartProductList(): MutableList<CartProduct> {
        return User.shared.cartProducts
    }

    override fun deleteAnItem(cartProduct: CartProduct) {
        cartProduct.quantity = 0
        User.shared.updateProductInCart(cartProduct)
    }

    override fun onBackPressed() {
        activityHandler?.onBackPressed()
    }

    override fun onProceedCheckoutClicked() {
        activityHandler?.onProceedCheckoutCLick()
    }
}