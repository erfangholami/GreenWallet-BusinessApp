package com.greenwallet.business.scenes.shopgreen

import android.content.Context
import android.util.Log
import com.greenwallet.business.R
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.InteractorFactory
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.campaings.CampaignsResponse
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.network.product.CategoriesResponse
import com.greenwallet.business.network.product.ProductResponse
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.productReviews.ProductReviewsResponse
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.campaignList.ExploreCampaignsView
import com.greenwallet.business.scenes.shopgreen.ui.HomeOnboardingItem
import com.greenwallet.business.scenes.shopgreen.ui.ShopGreenView

class ShopGreenPresenter(context: Context) :
    BasePresenter<ShopGreenView, ShopGreenProcessHandler>(context),
    ShopGreenView.Presenter,
    ExploreCampaignsView.Presenter {

    enum class State {
        SHOP_GREEN,
        EXPLORE_CAMPAIGNS,
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
                    State.EXPLORE_CAMPAIGNS -> it.showCampaignListScreen()
                    State.LOADING -> it.showLoadingScreen()
                    State.ERROR -> it.showErrorMessage()
                }
            }
        }

    val productInteractor = InteractorFactory(this.context).createProductInteractor()
    val campaignInteractor = InteractorFactory(this.context).createCampaignsInteractor()
    private val productReviewInteractor = InteractorFactory(context).createProductReviewInteractor()

    var fetchedCategories = false
    var categories: ArrayList<CategoriesResponseModel> = arrayListOf()

    var fetchedBestSellerItems = false
    var bestSellers: ArrayList<ProductResponseModel> = arrayListOf()

    var fetchedRedeemItems = false
    var redeems: ArrayList<ProductResponseModel> = arrayListOf()

    var fetchedCampaign = false
    var campaignList: ArrayList<CampaignsResponseModel> = ArrayList()


//    var shopGreenView: ShopGreenView? = null

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun onViewSubscribed(view: ShopGreenView) {}

    private fun requestCategories() {
        state = State.LOADING

        val merchantId = User.shared.merchantId ?: ""

        productInteractor.categories(
            merchantId = merchantId,
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
                        campaignList = (response.result ?: arrayOf()).toCollection(ArrayList())

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

        val merchantId = User.shared.merchantId ?: ""

        productInteractor.bestSellers(
            merchantId = merchantId,
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

        val merchantId = User.shared.merchantId ?: ""

        productInteractor.hotDeals(
            merchantId = merchantId,
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
        if (fetchedCategories && fetchedBestSellerItems && fetchedCampaign && fetchedRedeemItems) {
            state = State.SHOP_GREEN
        }
    }

    override fun createOnboardingItems() = listOf(
        HomeOnboardingItem(
            image = R.drawable.discover_deals_illustration,
            title = R.string.title_onboarding_first,
            message = R.string.message_onboarding_first
        ),
        HomeOnboardingItem(
            image = R.drawable.onboarding,
            title = R.string.title_onboarding_second,
            message = R.string.message_onboarding_second
        ),
        HomeOnboardingItem(
            image = R.drawable.planting_trees_illustration,
            title = R.string.title_onboarding_third,
            message = R.string.message_onboarding_third
        )
    )

    override fun getCategoryList(): ArrayList<CategoriesResponseModel> {
        return categories
    }

    override fun getBestSellerItems() = bestSellers

    override fun getRedeemOptions() = redeems

    override fun getCampaigns() = campaignList

    override fun subscribeView(view: ExploreCampaignsView) {

    }

    override fun disposeView(view: ExploreCampaignsView) {

    }

    override fun onBackButtonClicked() {
        state = State.SHOP_GREEN
    }

    override fun onCampaignClicked(campaignsResponseModel: CampaignsResponseModel) {

        activityHandler?.showCampaignDetails(campaignsResponseModel)
    }

    override fun onShowAllCampaignsClicked() {
        state = State.EXPLORE_CAMPAIGNS
    }

    override fun onSearchTextChanged(searchQuery: String) {
        activityHandler?.showSearchProductsScreen(searchQuery)
    }

    override fun onCategoryItemClicked(category: CategoriesResponseModel) {
        activityHandler?.showCategoryProductListScreen(category)
    }

    override fun onCartListClicked() {
        activityHandler?.showBasketScreen()
    }

    override fun onShowAllRedeemOptionsClicked() {
        activityHandler?.showRedeemListScreen()
    }

    override fun onShowAllBestSellersClicked() {
        activityHandler?.showBestSellerListScreen()
    }

    override fun onProductClicked(productModel: ProductResponseModel) {
        activityHandler?.onProductClicked(productModel)
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
        productReviewInteractor.reviews(
            productId = id,
            listener = object :
                Subscriber<ProductReviewsResponse> {
                override fun onRequestSuccess(response: ProductReviewsResponse) {
                    if (response.response == ProductReviewsResponse.Result.SUCCESS) {

                        listener.onAPICallFinished(
                            (response.reviews ?: arrayOf()).toCollection(
                                ArrayList()
                            )
                        )

                        Log.e("ProductReviews", "reviews: ${response.reviews?.size}")

                        Log.e("ProductReviews", "Success")
                    } else {
                        listener.onAPICallFailed()

                        Log.e("ProductReviews", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    listener.onAPICallFailed()

                    Log.e("ProductReviews", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }
}