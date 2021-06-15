package com.greenwallet.business.scenes.basket.model

import com.greenwallet.business.network.product.response.CartProduct

data class CheckoutItem(
    val id: String,
    val type: String,
    var totalAmount: Int = 0,
    var totalVat: Int = 0,
    var shippingAmount: Int = 0,
    var freeShippingAmount: Int = 0,
    val products: MutableList<CartProduct> = arrayListOf(),
    var paymentId: String? = null,
    var isPaying: Boolean = false,
    var payed: Boolean = false,
    var earnedGreenCoins: Int = 0
)

fun CheckoutItem.findProductById(productId: String): Pair<Int, CartProduct?> {

    products.forEachIndexed { index, cartProduct ->
        if (cartProduct.product!!.productID!! == productId) {
            return Pair(index, cartProduct)
        }
    }
    return Pair(-1, null)
}