package com.greenwallet.business.network.product.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProductShipmentsResponseModel(
    @SerializedName("product_id") val productId: String? = null,
    val method: String? = null,
    val amount: Int? = null,
    val vat: Int? = null,
    val title: String? = null,
    val shipment_id: String? = null,
    val merchant_id: String? = null,
    @SerializedName("delivery_time_in_hours") val deliveryTimeInHours: String? = null,
    val currency: String? = null,
    val threshold: Int? = null,
) : Serializable

enum class ShippingMethod {
    STANDARD_DELIVERY,
    NEXT_DAY_DELIVERY,
    EXPRESS_DELIVERY,
    INTERNATIONAL_DELIVERY,
    NONE,
}

fun ProductShipmentsResponseModel.getShippingMethod(): ShippingMethod {
    return when (method) {
        "standard_delivery" -> ShippingMethod.STANDARD_DELIVERY
        "next_day_delivery" -> ShippingMethod.NEXT_DAY_DELIVERY
        "express_delivery" -> ShippingMethod.EXPRESS_DELIVERY
        "international_delivery" -> ShippingMethod.INTERNATIONAL_DELIVERY
        else -> ShippingMethod.NONE
    }
}

fun ProductShipmentsResponseModel.getShippingMethodName(): String {
    return when (getShippingMethod()) {
        ShippingMethod.STANDARD_DELIVERY -> "Standard Delivery"
        ShippingMethod.NEXT_DAY_DELIVERY -> "Next Day Delivery"
        ShippingMethod.EXPRESS_DELIVERY -> "Express Delivery"
        ShippingMethod.INTERNATIONAL_DELIVERY -> "International Delivery"
        else -> ""
    }
}