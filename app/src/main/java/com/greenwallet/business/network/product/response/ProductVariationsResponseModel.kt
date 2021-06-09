package com.greenwallet.business.network.product.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProductVariationsResponseModel(
    var description: String,
    var dimensions: Dimensions,
    var updated: Long,
    var sku: String,
    var stock: Stock,
    var files: Array<String>? = null,
    @SerializedName("file_urls") var fileUrls: Array<String>? = null,
    var price: Price,
    @SerializedName("default_file_id") var defaultFileId: String? = null,
    @SerializedName("default_file_url") var defaultFileUrl: String? = null,
    @SerializedName("other_variant_id") var otherVariantId: String,
    @SerializedName("size_variant_id") var sizeVariantId: String,
    @SerializedName("product_id") var productId: String,
    @SerializedName("manage_stock") var manageStock: Boolean,
    @SerializedName("merchant_id") var merchantId: String,
    @SerializedName("colour_variant_id") var colourVariantId: String,
    var created: Long
) : Serializable {
    class Dimensions(
        var length: Int,
        var weight: Int,
        var width: Int,
        var height: Int,
        @SerializedName("weight_unit") var weightUnit: String
    ) : Serializable

    class Stock(
        var allocated: Int,
        var type: String,
        var total: Int,
        var available: Int
    ) : Serializable

    class Price(
        var amount: Int,
        var currency: String,
        var vat: Int
    ) : Serializable
}