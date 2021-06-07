package com.greenwallet.business.helper.keystore

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.greenwallet.business.app.Application.Companion.context
import com.greenwallet.business.network.product.response.CartProduct

class User {

    private val encryptedPreference =
        context.let { EncryptedPreference.getInstance(it, "ENCRYPTED_PREFERENCE_USER") }

    enum class Keys {
        MERCHANT_ID,
        EMAIL,
        PASSWORD,
        CART_PRODUCT_LIST,
        BUY_NOW_CART_PRODUCT_LIST,

    }

    companion object {
        val shared = User()
    }

    var merchantId: String? = null
        get() {
            return encryptedPreference.getString(Keys.MERCHANT_ID.toString(), "")
        }
        set(value) {
            field = value

            encryptedPreference.putString(Keys.MERCHANT_ID.toString(), value)
        }

    var email: String? = null
        get() {
            return encryptedPreference.getString(Keys.EMAIL.toString(), "")
        }
        set(value) {
            field = value

            encryptedPreference.putString(Keys.EMAIL.toString(), value)
        }

    var password: String? = null
        get() {
            return encryptedPreference.getString(Keys.PASSWORD.toString(), "")
        }
        set(value) {
            field = value

            encryptedPreference.putString(Keys.PASSWORD.toString(), value)
        }

    var cartProducts: ArrayList<CartProduct> = arrayListOf()
        get() {
            return GsonBuilder().create().fromJson(
                encryptedPreference.getString(Keys.CART_PRODUCT_LIST.toString(), "[]"),
                object : TypeToken<ArrayList<CartProduct>>() {}.type
            )
        }
        set(value) {
            field = value

            encryptedPreference.putString(
                Keys.CART_PRODUCT_LIST.toString(),
                GsonBuilder().create().toJson(value)
            )
        }

    var buyNowCartProducts: ArrayList<CartProduct> = arrayListOf()
        get() {
            return GsonBuilder().create().fromJson(
                encryptedPreference.getString(Keys.BUY_NOW_CART_PRODUCT_LIST.toString(), "[]"),
                object : TypeToken<ArrayList<CartProduct>>() {}.type
            )
        }
        set(value) {
            field = value

            encryptedPreference.putString(
                Keys.BUY_NOW_CART_PRODUCT_LIST.toString(),
                GsonBuilder().create().toJson(value)
            )
        }


    //todo: after adding variations
    /*
    fun addProductToCart(cartProduct: CartProduct): Boolean {

        val currentItems = cartProducts
        var isNew = true

        for (item in currentItems) {
            if (item.product!!.productID == cartProduct.product!!.productID) {

                if (item.variants.size == cartProduct.variants.size) {

                    var sameVariantCount = 0
                    for (itemVariant in item.variants) {
                        for (cartVariant in cartProduct.variants) {
                            if (itemVariant.type == cartVariant.type &&
                                itemVariant.value == cartVariant.value
                            ) {
                                sameVariantCount++
                            }
                        }
                    }

                    if (sameVariantCount == item.variants.size) {
                        item.product = cartProduct.product
                        item.quantity += cartProduct.quantity
                        item.timestamp = cartProduct.timestamp

                        isNew = false
                        break
                    } else {
                        isNew = true
                    }
                }
            }
        }

        var returnValue = true
        if (isNew) {
            returnValue = currentItems.add(cartProduct)
        }

        cartProducts = currentItems
        return returnValue
    }

    fun updateProductInCart(cartProduct: CartProduct) {

        val currentItems = cartProducts

        var itemIndex = -1
        for ((index, item) in currentItems.withIndex()) {
            if (item.product!!.productID == cartProduct.product!!.productID) {

                if (item.variants.size == cartProduct.variants.size) {

                    var sameVariantCount = 0
                    for (itemVariant in item.variants) {
                        for (cartVariant in cartProduct.variants) {
                            if (itemVariant.type == cartVariant.type &&
                                itemVariant.value == cartVariant.value
                            ) {
                                sameVariantCount++
                            }
                        }
                    }
                    if (sameVariantCount == item.variants.size) {
                        //item for modification found
                        itemIndex = index
                        break
                    } else {
                        //continue searching
                    }
                }
            }
        }

        if (itemIndex > -1) {
            //item exists
            val item = currentItems[itemIndex]
            if (cartProduct.quantity == 0) {
                currentItems.remove(item)
            } else {
                item.product = cartProduct.product
                item.quantity = cartProduct.quantity
                item.variants = cartProduct.variants
                item.shipments = cartProduct.shipments
                item.selectedShipmentMethod = cartProduct.selectedShipmentMethod
                item.variation = cartProduct.variation
                item.timestamp = cartProduct.timestamp
                item.isBuyingNow = cartProduct.isBuyingNow
            }
        } else {
            //item doesn't exists
        }

        cartProducts = currentItems
    }

    fun addProductToBuyNowCart(cartProduct: CartProduct): Boolean {

        val currentItems = buyNowCartProducts
        var isNew = true

        for (item in currentItems) {
            if (item.product!!.productID == cartProduct.product!!.productID) {

                if (item.variants.size == cartProduct.variants.size) {

                    var sameVariantCount = 0
                    for (itemVariant in item.variants) {
                        for (cartVariant in cartProduct.variants) {
                            if (itemVariant.type == cartVariant.type &&
                                itemVariant.value == cartVariant.value
                            ) {
                                sameVariantCount++
                            }
                        }
                    }

                    if (sameVariantCount == item.variants.size) {
                        item.product = cartProduct.product
                        item.quantity = 1
                        item.timestamp = cartProduct.timestamp

                        isNew = false
                        break
                    } else {
                        isNew = true
                    }
                }
            }
        }

        var returnValue = true
        if (isNew) {
            val newList = arrayListOf<CartProduct>()
            returnValue = newList.add(cartProduct)
            buyNowCartProducts = newList
        } else {
            buyNowCartProducts = currentItems
        }

        return returnValue
    }

    fun updateBuyNowProductInCart(cartProduct: CartProduct) {

        val currentItems = buyNowCartProducts

        var itemIndex = -1
        for ((index, item) in currentItems.withIndex()) {
            if (item.product!!.productID == cartProduct.product!!.productID) {

                if (item.variants.size == cartProduct.variants.size) {

                    var sameVariantCount = 0
                    for (itemVariant in item.variants) {
                        for (cartVariant in cartProduct.variants) {
                            if (itemVariant.type == cartVariant.type &&
                                itemVariant.value == cartVariant.value
                            ) {
                                sameVariantCount++
                            }
                        }
                    }
                    if (sameVariantCount == item.variants.size) {
                        //item for modification found
                        itemIndex = index
                        break
                    } else {
                        //continue searching
                    }
                }
            }
        }

        if (itemIndex > -1) {
            //item exists
            val item = currentItems[itemIndex]
            if (cartProduct.quantity == 0) {
                currentItems.remove(item)
            } else {
                item.product = cartProduct.product
                item.quantity = cartProduct.quantity
                item.variants = cartProduct.variants
                item.shipments = cartProduct.shipments
                item.selectedShipmentMethod = cartProduct.selectedShipmentMethod
                item.variation = cartProduct.variation
                item.timestamp = cartProduct.timestamp
                item.isBuyingNow = cartProduct.isBuyingNow
            }
        } else {
            //item doesn't exists
        }

        buyNowCartProducts = currentItems
    } */
}