package com.greenwallet.business.network.product.response

import java.util.*

data class CartProduct(
    var product: ProductResponseModel? = null,
    var quantity: Int = 0,
    var variants: ArrayList<ProductVariantsResponseModel> = arrayListOf(),
    var shipments: ArrayList<ProductShipmentsResponseModel> = arrayListOf(),
    var selectedShipmentMethod: ProductShipmentsResponseModel? = null,
    var variation: ProductVariationsResponseModel? = null,
    var timestamp: Long = Date().time,
    var isBuyingNow: Boolean = false
)

fun CartProduct.getOldPrice(): Int {
    return quantity * product!!.getOldPrice()
}

fun CartProduct.getPrice(): Int {
    return quantity *
            if (product!!.isHotDeal()) {
                product!!.getPrice()
            } else {
                if (variation == null || variation!!.price.amount == 0) {
                    product!!.getPrice()
                } else {
                    variation!!.price.amount
                }
            }
}

fun CartProduct.getVat(): Int {
    return quantity *
            if (product!!.isHotDeal()) {
                product!!.getVat()
            } else {
                if (variation == null || variation!!.price.amount == 0) {
                    product!!.getVat()
                } else {
                    variation!!.price.vat
                }
            }
}