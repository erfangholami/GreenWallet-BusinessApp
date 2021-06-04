package com.greenwallet.business.network.product.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProductReviewsResponseModel(
    @SerializedName("product_id") val productId: String? = null,
    @SerializedName("nick_name") val nickname: String? = null,
    val rating: Int? = null,
    val id: String? = null,
    val title: String? = null,
    val created: Long? = null,
    @SerializedName("user_id") val userId: String? = null,
    val description: String? = null,
) : Serializable