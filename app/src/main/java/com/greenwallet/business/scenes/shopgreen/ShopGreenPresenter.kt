package com.greenwallet.business.scenes.shopgreen

import android.content.Context
import android.util.Log
import com.greenwallet.business.helper.keystore.CipherStorageFactory
import com.greenwallet.business.helper.keystore.KeystoreKeys
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.InteractorFactory
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.campaings.CampaignsResponse
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.network.product.CategoriesResponse
import com.greenwallet.business.network.product.ProductResponse
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.product.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.shopgreen.ui.ShopGreenView
import kotlin.collections.ArrayList

class ShopGreenPresenter(context: Context) :
    BasePresenter<ShopGreenView, ShopGreenProcessHandler>(context), ShopGreenView.Presenter {

    enum class State {
        SHOP_GREEN,
        LOADING,
        ERROR
    }

    var state: State = State.SHOP_GREEN
        set(value) {
            field = value
            activityHandler?.let {
                when (state) {
                    State.SHOP_GREEN -> {
                        it.showShopGreenScreen()

                        if (!fetchedCategories) {
                            requestCategories()
                        }

                        if (!fetchedBestSellerItems) {
                            requestBestSellers()
                        }

                        if (!fetchedRedeemItems) {
                            requestRedeems()
                        }

                        if (!fetchedCampaign) {
                            requestCampaigns()
                        }
                    }
                    State.LOADING -> it.showLoadingScreen()
                    State.ERROR -> it.showErrorMessage()
                }
            }
        }

    val productInteractor = InteractorFactory(this.context).createProductInteractor()
    val campaignInteractor = InteractorFactory(this.context).createCampaignsInteractor()

    var fetchedCategories = false
    var categories: ArrayList<CategoriesResponseModel> = arrayListOf()

    var fetchedBestSellerItems = false
    var bestSellers: ArrayList<ProductResponseModel> = arrayListOf()

    var fetchedRedeemItems = false
    var redeems: ArrayList<ProductResponseModel> = arrayListOf()

    var fetchedCampaign = false
    var campaigns: ArrayList<CampaignsResponseModel> = ArrayList()


//    var shopGreenView: ShopGreenView? = null

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun onViewSubscribed(view: ShopGreenView) {}

    private fun requestCategories() {
        state = State.LOADING

        val cipherStorage = CipherStorageFactory.newInstance(context)
        val merchantId = if (!cipherStorage.decrypt(KeystoreKeys.merchantId.name)
                .isNullOrEmpty()
        ) cipherStorage.decrypt(KeystoreKeys.merchantId.name) else ""

        productInteractor.categories(
            merchantId = merchantId!!,
            listener = object :
                Subscriber<CategoriesResponse> {
                override fun onRequestSuccess(response: CategoriesResponse) {
                    if (response.response == CategoriesResponse.Result.SUCCESS) {
                        fetchedCategories = true
                        categories = (response.result ?: arrayOf()).toCollection(ArrayList())

                        checkShopGreenState()
                        Log.e("Categories Request", "Success")
                    } else {
                        requestCategories()

                        Log.e("Categories Request", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    requestCategories()

                    Log.e("Categories Request", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }

    private fun requestCampaigns() {
        state = State.LOADING

        campaignInteractor.campaigns(
            listener = object :
                Subscriber<CampaignsResponse> {
                override fun onRequestSuccess(response: CampaignsResponse) {
                    if (response.response == CampaignsResponse.Result.SUCCESS) {
                        fetchedCampaign = true
                        campaigns = (response.result ?: arrayOf()).toCollection(ArrayList())

                        checkShopGreenState()

                        Log.e("Campaigns Request", "Success")
                    } else {
                        requestCampaigns()

                        Log.e("Campaigns Request", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    requestCampaigns()

                    Log.e("Campaigns Request", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }

    private fun requestBestSellers() {
        state = State.LOADING

        val cipherStorage = CipherStorageFactory.newInstance(context)
        val merchantId = if (!cipherStorage.decrypt(KeystoreKeys.merchantId.name)
                .isNullOrEmpty()
        ) cipherStorage.decrypt(KeystoreKeys.merchantId.name) else ""

        productInteractor.bestSellers(
            merchantId = merchantId!!,
            offset = 0,
            size = 10,
            listener = object :
                Subscriber<ProductResponse> {
                override fun onRequestSuccess(response: ProductResponse) {
                    if (response.result == ProductResponse.Result.SUCCESS) {
                        fetchedBestSellerItems = true
                        bestSellers = (response.products ?: arrayOf()).toCollection(ArrayList())

                        checkShopGreenState()
                        Log.e("Best Sellers Request", "Success")
                    } else {
                        requestBestSellers()

                        Log.e("Best Sellers Request", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    requestBestSellers()

                    Log.e("Best Sellers Request", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }

    private fun requestRedeems() {
        state = State.LOADING

        val cipherStorage = CipherStorageFactory.newInstance(context)
        val merchantId = if (!cipherStorage.decrypt(KeystoreKeys.merchantId.name)
                .isNullOrEmpty()
        ) cipherStorage.decrypt(KeystoreKeys.merchantId.name) else ""

        productInteractor.hotDeals(
            merchantId = merchantId!!,
            offset = 0,
            size = 10,
            listener = object :
                Subscriber<ProductResponse> {
                override fun onRequestSuccess(response: ProductResponse) {
                    if (response.result == ProductResponse.Result.SUCCESS) {
                        fetchedRedeemItems = true
                        redeems = (response.products ?: arrayOf()).toCollection(ArrayList())

                        checkShopGreenState()
                        Log.e("Redeems Request", "Success")
                    } else {
                        requestRedeems()

                        Log.e("Redeems Request", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    requestRedeems()

                    Log.e("Redeems Request", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }

    private fun checkShopGreenState() {
        if (fetchedCategories && fetchedCampaign && fetchedRedeemItems) {
            state = State.SHOP_GREEN
        }
    }

    override fun getCategoryList(): ArrayList<String> {
        return categories.map { i -> i.category } as ArrayList<String>
    }

    override fun getBestSellerItems() = bestSellers

    override fun getRedeemOptions() = redeems

    override fun getCampaignsList() = campaigns

    override fun onCampaignClicked(campaignsResponseModel: CampaignsResponseModel) {
//        TODO("Not yet implemented")
    }

    override fun onCategoryItemClicked(categoryName: String) {
//        TODO("Not yet implemented")
    }

    override fun onCartListClicked() {
//        TODO("Not yet implemented")
    }

    override fun onShowAllRedeemOptionsClicked() {
//        TODO("Not yet implemented")
    }

    override fun onShowAllBestSellersClicked() {
//        TODO("Not yet implemented")
    }

    override fun onProductClicked(productModel: ProductResponseModel) {
//        TODO("Not yet implemented")
    }

    override fun onProductReviewClicked(
        productId: String,
        reviews: java.util.ArrayList<ProductReviewsResponseModel>
    ) {
//        TODO("Not yet implemented")
    }

    override fun fetchReviews(
        id: String,
        listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
    ) {
//        TODO("Not yet implemented")
    }
}