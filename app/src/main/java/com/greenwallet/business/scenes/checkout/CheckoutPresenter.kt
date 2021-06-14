package com.greenwallet.business.scenes.checkout

import android.content.Context
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.helper.shop.categoriesProductsByMerchants
import com.greenwallet.business.helper.shop.getMaxRedeemAmount
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.InteractorFactory
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.product.ProductShipmentsResponse
import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.network.product.response.ProductShipmentsResponseModel
import com.greenwallet.business.network.product.response.ShippingMethod
import com.greenwallet.business.network.product.response.getShippingMethod
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.basket.model.CheckoutDetailsItem
import com.greenwallet.business.scenes.basket.model.CheckoutItem
import com.greenwallet.business.scenes.checkout.ui.CheckoutView

class CheckoutPresenter(context: Context) :
    BasePresenter<CheckoutView, CheckoutProcessHandler>(context),
    CheckoutView.Presenter {

    enum class State {
        CHECKOUT_FEATURES,
        LOADING,
        ERROR,
    }

    var state: State = State.LOADING
        set(value) {
            field = value
            when (state) {
                State.CHECKOUT_FEATURES -> activityHandler?.showCheckoutScreen()
                State.LOADING -> activityHandler?.showLoadingScreen()
                State.ERROR -> activityHandler?.showErrorMessage()
            }
        }

    var mode: CheckoutActivity.Mode = CheckoutActivity.Mode.ORDINARY

    private val productInteractor = InteractorFactory(context).createProductInteractor()

    var redeemedCoins: Int = 0
    private val checkoutDetail = CheckoutDetailsItem(
        greenCoinsAmount = 0,
        redeemedGreenCoins = redeemedCoins
    )

    private var checkoutView: CheckoutView? = null

    override fun onViewSubscribed(view: CheckoutView) {
        checkoutView = view
    }

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun onBackPressed() {
        activityHandler?.onBackPressed()
    }

    override fun onEditCheckoutItemClicked() {

    }

    override fun getCheckoutDetails(): CheckoutDetailsItem {
        with(checkoutDetail) {
            greenCoinsAmount = 0
            redeemedGreenCoins = redeemedCoins
        }

        return checkoutDetail
    }

    override fun getCheckoutItems(): MutableList<CheckoutItem> {

        chooseDefaultShippingMethod()

        return categoriesProductsByMerchants(
            when (mode) {
                CheckoutActivity.Mode.ORDINARY -> User.shared.cartProducts
                CheckoutActivity.Mode.BUY_NOW -> User.shared.buyNowCartProducts
            }
        )
    }

    fun fetchShipmentInformationFromServer() {
        state = State.LOADING

        val checkoutItems = getCheckoutItems()

        var productCount = 0
        var receivedCount = 0

        for (checkout in checkoutItems) {
            productCount += checkout.products.filter {
                it.shipments.isEmpty()
            }.size
        }

        if (productCount > 0) {
            for (checkout in checkoutItems) {
                for (cartItem in checkout.products) {

                    if (cartItem.shipments.isEmpty()) {

                        getProductShipments(
                            checkout.id,
                            cartItem,
                            object : CallbackListener<Array<ProductShipmentsResponseModel>>() {
                                override fun onAPICallFinished(data: Array<ProductShipmentsResponseModel>) {
                                    cartItem.shipments = (data).toCollection(ArrayList())
                                    when (mode) {
                                        CheckoutActivity.Mode.ORDINARY -> User.shared.updateProductInCart(
                                            cartItem
                                        )
                                        CheckoutActivity.Mode.BUY_NOW -> User.shared.updateBuyNowProductInCart(
                                            cartItem
                                        )
                                    }

                                    receivedCount++

                                    if (receivedCount == productCount) {
                                        state = State.CHECKOUT_FEATURES
                                    }
                                }

                                override fun onAPICallFailed() {
                                    receivedCount++

                                    if (receivedCount == productCount) {
                                        state = State.CHECKOUT_FEATURES
                                    }
                                }
                            }
                        )
                    }
                }
            }
        } else {
            state = State.CHECKOUT_FEATURES
        }
    }

    private fun getProductShipments(
        merchantId: String,
        cardProduct: CartProduct,
        listener: CallbackListener<Array<ProductShipmentsResponseModel>>
    ) {

        productInteractor.shipments(
            merchantId = merchantId,
            productId = cardProduct.product!!.productID!!,
            listener = object : Subscriber<ProductShipmentsResponse> {
                override fun onRequestSuccess(response: ProductShipmentsResponse) {
                    if (response.result == ProductShipmentsResponse.Result.SUCCESS) {

                        listener.onAPICallFinished(response.shipments ?: arrayOf())

                    } else {
                        listener.onAPICallFailed()
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    listener.onAPICallFailed()
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            }
        )
    }

    override fun updateProductCount(cartProduct: CartProduct) {
        when (mode) {
            CheckoutActivity.Mode.ORDINARY -> {
                User.shared.updateProductInCart(cartProduct)
                val maxRedeemAmount = getMaxRedeemAmount(User.shared.cartProducts)
                if (redeemedCoins > maxRedeemAmount.first) {
                    redeemedCoins = maxRedeemAmount.first
                }
            }
            CheckoutActivity.Mode.BUY_NOW -> {
                User.shared.updateBuyNowProductInCart(cartProduct)
                val maxRedeemAmount = getMaxRedeemAmount(User.shared.buyNowCartProducts)
                if (redeemedCoins > maxRedeemAmount.first) {
                    redeemedCoins = maxRedeemAmount.first
                }
            }
        }
    }

    override fun onDismissError() {
        state = State.CHECKOUT_FEATURES
    }

    override fun onItemEditShippingClicked(productItem: CartProduct) {
        activityHandler?.showShippingMethodSelectionScreen(productItem)
    }

    fun itemDeliveryMethodChanged(product: CartProduct) {

        when (mode) {
            CheckoutActivity.Mode.ORDINARY -> User.shared.updateProductInCart(product)
            CheckoutActivity.Mode.BUY_NOW -> User.shared.updateBuyNowProductInCart(product)
        }

        checkoutView?.updateValues()
    }

    private fun chooseDefaultShippingMethod() {
        val productList = when (mode) {
            CheckoutActivity.Mode.ORDINARY -> User.shared.cartProducts
            CheckoutActivity.Mode.BUY_NOW -> User.shared.buyNowCartProducts
        }

        for (cardProduct in productList) {
            if (cardProduct.selectedShipmentMethod == null) {
                var standard: ProductShipmentsResponseModel? = null
                var cheapest: ProductShipmentsResponseModel? = null

                for (shippingMethod in cardProduct.shipments) {
                    when (shippingMethod.getShippingMethod()) {
                        ShippingMethod.STANDARD_DELIVERY -> {
                            standard = shippingMethod
                        }
                        else -> {
                            if (cheapest == null || cheapest.amount!! > shippingMethod.amount!!) {
                                cheapest = shippingMethod
                            }
                        }
                    }
                }

                if (standard != null) {
                    cardProduct.selectedShipmentMethod = standard
                } else if (cheapest != null) {
                    cardProduct.selectedShipmentMethod = cheapest
                }

                when (mode) {
                    CheckoutActivity.Mode.ORDINARY -> User.shared.updateProductInCart(cardProduct)
                    CheckoutActivity.Mode.BUY_NOW -> User.shared.updateBuyNowProductInCart(
                        cardProduct
                    )
                }
            }
        }
    }
}