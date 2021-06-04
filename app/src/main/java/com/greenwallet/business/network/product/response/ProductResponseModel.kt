package com.greenwallet.business.network.product.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class ProductResponseModel(
    val collection: String? = null,
    @SerializedName("sub_category") val subCategory: String? = null,
    val created: Long? = null,
    val sku: String? = null,
    @SerializedName("published_by") val publishedBy: String? = null,
    @SerializedName("website_url") val websiteURL: String? = null,
    val files: Array<String>? = null,
    @SerializedName("file_urls") var fileUrls: Array<String>? = null,
    val brand: String? = null,
    val deals: Boolean? = null,
    val stock: Stock? = null,
    @SerializedName("merchant_id") val merchantID: String? = null,
    val category: String? = null,
    @SerializedName("default_file_id") val defaultFileID: String? = null,
    @SerializedName("default_file_url") var defaultFileUrl: String? = null,
    val updated: Long? = null,
    val name: String? = null,
    val shipments: Array<String>? = null,
    val details: String? = null,
    @SerializedName("product_id") val productID: String? = null,
    @SerializedName("max_green_coins_percentage") val maxGreenCoinsPercentage: Int? = null,
    @SerializedName("commodity_type") val commodityType: String? = null,
    @SerializedName("logo_id") val logoID: String? = null,
    val variants: Array<Variant>? = null,
    val dimensions: Dimensions? = null,
    val price: Price? = null,
    @SerializedName("manage_stock") val manageStock: Boolean? = null,
    @SerializedName("description") val welcomeDescription: String,
    var reviews: ArrayList<ProductReviewsResponseModel>? = null,
    var preview: String? = null,
    @SerializedName("size_map_file_id") val sizeMapFileId: String? = null,
) : Serializable {
    class Price(
        val amount: Int? = null,
        val currency: String? = null,
        val discount: Discount? = null,
        val vat: Int? = null,
    ) : Serializable

    class Discount(
        val amount: Int? = null,
        @SerializedName("end_time") val endTime: Long? = null,
        val percentage: Int? = null,
        val type: String? = null,
        val vat: Int? = null,
        @SerializedName("start_time") val startTime: Long? = null,
    ) : Serializable

    class Variant(
        @SerializedName("OTHER") val other: String? = null,
        @SerializedName("SIZE") val size: String? = null,
        @SerializedName("COLOUR") val colour: String? = null,
    ) : Serializable

    class Stock(
        val allocated: Int? = null,
        val type: String? = null,
        val total: Int? = null,
        val available: Int? = null,
    ) : Serializable

    class Dimensions(
        val length: Int? = null,
        val weight: String? = null,
        val width: Int? = null,
        val height: Int? = null,
        @SerializedName("weight_unit") val weightUnit: String? = null
    ) : Serializable
}

enum class FileMode {
    URL,
    ID,
}

fun ProductResponseModel.isHotDeal(): Boolean {
    if (deals != null && deals) {
        val endDate = Date(price?.discount?.endTime!!)
        val currentDate = Date()

        return endDate.time > currentDate.time
    } else {
        return false
    }
}

fun ProductResponseModel.getOldPrice(): Int {
    return price!!.amount!!
}

fun ProductResponseModel.getPrice(): Int {
    return if (isHotDeal()) {
        price!!.discount!!.amount!!
    } else {
        price!!.amount!!
    }
}

fun ProductResponseModel.getVat(): Int {
    return if (isHotDeal()) {
        price!!.discount!!.vat!!
    } else {
        price!!.vat!!
    }
}

fun ProductResponseModel.isMatchedWithCategory(categoryName: String): Boolean {
    return category.equals(categoryName, ignoreCase = true)
}