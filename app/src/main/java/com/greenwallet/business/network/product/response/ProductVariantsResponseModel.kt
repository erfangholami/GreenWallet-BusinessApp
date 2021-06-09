package com.greenwallet.business.network.product.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProductVariantsResponseModel(
    @SerializedName("variant_id") var variantId: String? = null,
    var value: String? = null,
    var type: String? = null,
    var description: String? = null,
) : Serializable