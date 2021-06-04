package com.greenwallet.business.network.campaings.response

import com.google.gson.annotations.SerializedName

class CampaignsResponseModel {
    val status: String? = null
    val name: String? = null
    @SerializedName("merchant_id") val merchantId: String? = null
    val updated: Int? = null
    val description: String? = null
    @SerializedName("default_file_id") val defaultFileId: String? = null
    @SerializedName("default_file_url") val defaultFileUrl: String? = null
    @SerializedName("website_url") val websiteUrl: String? = null
    val created: Long? = null
    @SerializedName("campaign_id") val campaignId: String? = null
    @SerializedName("file_ids") val fileIds: List<String>? = null
    @SerializedName("inspiration_ids") val inspirationIds: List<String>? = null
    val handles: String? = null
    @SerializedName("post_frequency") val postFrequency: Int? = null
    val details: String? = null
    val tags: String? = null
    @SerializedName("campaign_start_time") val campaignStartTime: Long? = null
    @SerializedName("campaign_end_time") val campaignEndTime: Long? = null
    @SerializedName("business_logo") val businessLogo: String? = null
    @SerializedName("business_name") val businessName: String? = null
}