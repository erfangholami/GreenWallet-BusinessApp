package com.greenwallet.business.helper.shop

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.greenwallet.business.R
import com.greenwallet.business.network.product.response.*
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.basket.model.CheckoutItem

fun getCategoryItem(resources: Resources, itemName: String): Pair<String, Drawable?>? {
    val resourceId = when (itemName) {
        "Accessories" -> R.drawable.category_accessories_icon
        "Apparel" -> R.drawable.category_apparel_icon
        "Ethical Fashion" -> R.drawable.category_ethical_fashion_icon
        "Health & Beauty" -> R.drawable.category_health_and_beauty_icon
        "Health and Beauty" -> R.drawable.category_health_and_beauty_icon
        "Home Goods" -> R.drawable.category_home_goods_icon
        "Plant-based Nutrition" -> R.drawable.category_nutrition_icon
        "Nutrition" -> R.drawable.category_nutrition_icon
        "Food and Drink" -> R.drawable.ic_food_drink
        else -> -1
    }

    val name = when (itemName) {
        "Plant-based Nutrition" -> "Nutrition"
        "Health & Beauty" -> "Health and Beauty"
        else -> itemName
    }

    return if (resourceId == -1) {
        null
    } else {
        Pair(
            name, ResourcesCompat.getDrawable(resources, resourceId, null))
    }
}

fun getOverallRating(reviews: ArrayList<ProductReviewsResponseModel>?): Float {
    if (reviews == null) {
        return 0.0f
    } else if (reviews.size == 0) {
        return 0.0f
    } else {
        var rate5Size = 0
        var rate4Size = 0
        var rate3Size = 0
        var rate2Size = 0
        var rate1Size = 0

        for (review in reviews) {
            when (review.rating) {
                1 -> rate1Size++
                2 -> rate2Size++
                3 -> rate3Size++
                4 -> rate4Size++
                5 -> rate5Size++
            }
        }

        return ((rate1Size) + (rate2Size * 2) + (rate3Size * 3) + (rate4Size * 4) + (rate5Size * 5)).toFloat() / reviews.size
    }
}

fun categoriesProductsByMerchants(products: ArrayList<CartProduct>): ArrayList<CheckoutItem> {

    val returnItems = arrayListOf<CheckoutItem>()

    for (currentProduct in products) {

        var merchantIndex = -1
        for ((index, item) in returnItems.withIndex()) {
            if (currentProduct.product!!.merchantID!! == item.id) {
                merchantIndex = index
                break
            }
        }

        if (merchantIndex != -1) {
            //the merchant added before
            returnItems[merchantIndex].products.add(currentProduct)
        } else {
            //the first time we see this merchant
            val newItem = CheckoutItem(
                id = currentProduct.product!!.merchantID!!,
                type = currentProduct.product!!.brand!!,
            )
            //todo: add other attributes to newItem

            newItem.products.add(currentProduct)
            returnItems.add(newItem)
        }
    }

    return getBasketPrices(returnItems).first
}

fun getFreeShippingAmount(items: MutableList<CartProduct>): Int {

    var maxFreeShipping = 0

    for (item in items) {
        for (shipmentItem in item.shipments) {
            if (shipmentItem.getShippingMethod() == ShippingMethod.STANDARD_DELIVERY &&
                (shipmentItem.threshold ?: 0) > maxFreeShipping
            ) {
                maxFreeShipping = shipmentItem.threshold!!
            }
        }
    }

    return maxFreeShipping
}

fun getShippingPrices(items: MutableList<CartProduct>): Int {

    var shippingPrice = 0

    for (item in items) {

        if (item.selectedShipmentMethod != null) {
            shippingPrice += item.selectedShipmentMethod!!.amount!! * item.quantity
        } else if (item.shipments.size == 0) {
            //add nothing
        } else {
            //todo
        }
    }

    return shippingPrice
}

fun getCheckoutItemPrices(items: MutableList<CartProduct>): Pair<Int, Int> {

    var total = 0
    var vat = 0

    for (item in items) {

        total += item.getPrice()
        vat += item.getVat()
    }

    return Pair(total, vat)

}

fun getBasketPrices(items: ArrayList<CheckoutItem>): Pair<ArrayList<CheckoutItem>, CheckoutPrice> {

    val checkoutPrice = CheckoutPrice()

    for (checkoutItem in items) {

        val checkoutPrices = getCheckoutItemPrices(checkoutItem.products)
        val checkoutFreeShippingMin = getFreeShippingAmount(checkoutItem.products)
        val checkoutShipmentPrices = getShippingPrices(checkoutItem.products)

        val checkoutTotal = checkoutPrices.first
        val checkoutVat = checkoutPrices.second

        checkoutPrice.total += checkoutTotal
        checkoutPrice.vat += checkoutVat
        checkoutPrice.subtotal += (checkoutTotal - checkoutVat)

        checkoutItem.totalAmount = checkoutTotal
        checkoutItem.totalVat = checkoutVat

        checkoutItem.freeShippingAmount = checkoutFreeShippingMin
        if ((checkoutFreeShippingMin == 0) || (checkoutFreeShippingMin > checkoutTotal)) {
            checkoutItem.shippingAmount = checkoutShipmentPrices
            checkoutPrice.shipping += checkoutShipmentPrices
        } else {
            checkoutItem.shippingAmount = 0
        }
    }

    return Pair(items, checkoutPrice)
}

//second parameter is percentage
fun getCheckoutMaxRedeemAmount(items: ArrayList<CheckoutItem>): Pair<Int, Float> {

    var totalPrice = 0
    var totalRedeemValue = 0

    for (checkoutItem in items) {
        for (product in checkoutItem.products) {
            var price = if (product.product!!.isHotDeal()) {
                (product.product!!.price?.discount?.amount!!).times(product.quantity)
            } else {
                (product.product!!.price?.amount!!).times(product.quantity)
            }

            totalPrice += price
            totalRedeemValue +=
                price.times(product.product!!.maxGreenCoinsPercentage ?: 0)
                    .toFloat()
                    .div(100)
                    .toInt()
        }
    }

    return Pair(totalRedeemValue, (totalRedeemValue.toFloat().div(totalPrice)).times(100))

}

//second parameter is percentage
fun getMaxRedeemAmount(items: ArrayList<CartProduct>): Pair<Int, Float> {

    var totalPrice = 0
    var totalRedeemValue = 0

    for (product in items) {
        var price = if (product.product!!.isHotDeal()) {
            (product.product!!.price?.discount?.amount!!).times(product.quantity)
        } else {
            (product.product!!.price?.amount!!).times(product.quantity)
        }

        totalPrice += price
        totalRedeemValue +=
            price.times(product.product!!.maxGreenCoinsPercentage ?: 0)
                .toFloat()
                .div(100)
                .toInt()
    }


    return Pair(totalRedeemValue, (totalRedeemValue.toFloat().div(totalPrice)).times(100))

}

data class CheckoutPrice(
    var total: Int = 0,
    var subtotal: Int = 0,
    var vat: Int = 0,
    var shipping: Int = 0
)