package com.greenwallet.business.network.product.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CategoriesResponseModel : Serializable {
    val category: String? = null
    val created: Long? = null
    @SerializedName("") var subCategories: ArrayList<String> = arrayListOf()
}