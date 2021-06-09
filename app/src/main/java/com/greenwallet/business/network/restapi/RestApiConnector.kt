package com.greenwallet.business.network.restapi

import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.network.login.request.LoginRequestModel
import com.greenwallet.business.network.login.response.LoginResponseModel
import com.greenwallet.business.network.product.response.*
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RestApiConnector {

    @POST("login")
    fun login(@Body requestModel: LoginRequestModel): Call<LoginResponseModel>

    @GET("merchant/search/products")
    fun search(
        @Query("name") name: String = "name",
        @Query("query") query: String,
        @Query("type") type: String = "ILIKE",
        @Query("offset") offset: String,
        @Query("size") size: String,
        @Query("is_published") isPublished: Boolean = false
    ): Call<Array<ProductResponseModel>>

    @GET("merchant/search/products/categories")
    fun categories(@Query("merchant_id") merchantId: String): Call<Array<CategoriesResponseModel>>

    @GET("merchant/search/products")
    fun productsByCategory(
        @Query("name") name: String = "category",
        @Query("query") categoryName: String,
        @Query("type") type: String = "ILIKE",
        @Query("offset") offset: String,
        @Query("size") size: String,
        @Query("is_published") isPublished: Boolean = false
    ): Call<Array<ProductResponseModel>>

    @GET("merchant/search/products")
    fun productsHotDeals(
        @Query("name") name: String = "merchant_id",
        @Query("query") query: String,
        @Query("type") type: String = "EQ",
        @Query("offset") offset: String,
        @Query("size") size: String,
        @Query("is_published") isPublished: Boolean = false
    ): Call<Array<ProductResponseModel>>

    @GET("merchant/search/products")
    fun productsBestSellers(
        @Query("name") name: String = "merchant_id",
        @Query("query") query: String,
        @Query("type") type: String = "EQ",
        @Query("offset") offset: String,
        @Query("size") size: String,
        @Query("is_published") isPublished: Boolean = false
    ): Call<Array<ProductResponseModel>>

    @GET("campaigns/loyalty_reward")
    fun campaigns(): Call<Array<CampaignsResponseModel>>

    @GET("file/{fileId}")
    fun file(@Path("fileId") fileId: String): Call<ResponseBody>

    @GET("reviews/{productId}")
    fun productReviews(@Path("productId") productId: String): Call<Array<ProductReviewsResponseModel>>

    @GET("merchant/{merchantId}/product/{productId}/shipment")
    fun productShipments(
        @Path("merchantId") merchantId: String,
        @Path("productId") productId: String
    ): Call<Array<ProductShipmentsResponseModel>>

    @GET("merchant/{merchantId}/product/{productId}/variant")
    fun productVariants(
        @Path("merchantId") merchantId: String,
        @Path("productId") productId: String
    ): Call<Array<ProductVariantsResponseModel>>

    @GET("merchant/{merchantId}/product/{productId}/variation")
    fun productVariations(
        @Path("merchantId") merchantId: String,
        @Path("productId") productId: String,
        @Query("variant_ids") variantIds: Array<String>,
        @Query("variants") variants: Array<String>,
    ): Call<Array<ProductVariationsResponseModel>>
}