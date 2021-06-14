package com.greenwallet.business.scenes.basket.model

data class ProductItem(
    val id: Int,
    val imageUrl: String = "",
    val name: String = "CANO Mara Huarache Womens Natural Cognac",
    val price: Float = 180.00f,
    val type: String,
    var productsCount: Int = 1
)