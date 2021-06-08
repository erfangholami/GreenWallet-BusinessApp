package com.greenwallet.business.scenes.redeem

import android.content.Context
import android.util.Log
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.network.InteractorFactory
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.product.ProductResponse
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.product.response.isHotDeal
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter
import com.greenwallet.business.scenes.base.LoadMoreCallBack
import com.greenwallet.business.scenes.redeem.ui.RedeemView

class RedeemPresenter(context: Context) :
    BasePresenter<RedeemView, RedeemProcessHandler>(context),
    RedeemView.Presenter {

    enum class State {
        REDEEM,
        CART_LIST,
        PRODUCT_DETAILS,
        DISMISS,
        LOADING,
        ERROR
    }

    var state: State = State.REDEEM
        set(value) {
            field = value
            activityHandler?.let {
                when (state) {
                    State.REDEEM -> it.showRedeemScreen()
                    State.CART_LIST -> it.shoeCartScreen()
                    State.PRODUCT_DETAILS -> it.showProductDetailsScreen(selectedProduct)
                    State.DISMISS -> it.dismiss()
                    State.LOADING -> it.showLoadingScreen()
                    State.ERROR -> it.showErrorMessage()
                }
            }
        }

    private val productInteractor = InteractorFactory(context).createProductInteractor()

    private lateinit var loadCallBack: LoadMoreCallBack<ProductResponseModel, BaseRecyclerViewAdapter.Mode>

    var items: ArrayList<ProductResponseModel?> = arrayListOf()
    var lastInsertedItems: ArrayList<ProductResponseModel> = arrayListOf()

    var hasMore = true
    val pageSize = 50
    var pageOffset = 0
    var pageNumber = 0
    private var isLoading = false

    private lateinit var selectedProduct: ProductResponseModel

    override fun onViewSubscribed(view: RedeemView) {
        state = State.REDEEM
    }

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun onSearchTextChanged(valueToSearch: String) {
        resetList()

        isLoading = true

        productInteractor.search(
            query = valueToSearch,
            offset = pageOffset,
            size = pageSize,
            listener = object :
                Subscriber<ProductResponse> {
                override fun onRequestSuccess(response: ProductResponse) {
                    if (response.result == ProductResponse.Result.SUCCESS) {
                        hasMore = (response.products ?: emptyArray()).size == pageSize
                        pageOffset += (response.products ?: emptyArray()).size

                        val filteredList: ArrayList<ProductResponseModel> =
                            (response.products ?: emptyArray()).toCollection(ArrayList())
                                .filter {
                                    it.isHotDeal()
                                }.toCollection(ArrayList())

                        items.addAll(filteredList)
                        lastInsertedItems = filteredList
                        if (hasMore) {
                            pageNumber++
                        }

                        isLoading = false
                        loadCallBack.onLoadMoreFinished(
                            lastInsertedItems,
                            BaseRecyclerViewAdapter.Mode.SEARCH
                        )

                        Log.e("Search", "products: ${response.products?.size}")

                        Log.e("Search", "Success")
                    } else {
                        isLoading = false
                        loadCallBack.onLoadMoreFailed()

                        Log.e("Search", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    isLoading = false
                    loadCallBack.onLoadMoreFailed()

                    Log.e("Search", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }

    override fun dismiss() {
        state = State.DISMISS
    }

    override fun hasMore(): Boolean {
        return hasMore
    }

    override fun isLoading(): Boolean {
        return isLoading
    }

    override fun onLoadMore() {
        if (hasMore && !isLoading) {
            fetchHotDeals()
        }
    }

    override fun setLoadCallBack(loadMoreCallBack: LoadMoreCallBack<ProductResponseModel, BaseRecyclerViewAdapter.Mode>) {
        loadCallBack = loadMoreCallBack
    }

    override fun onProductClicked(product: ProductResponseModel) {
        selectedProduct = product
        state = State.PRODUCT_DETAILS
    }

    override fun onCartListClicked() {
        state = State.CART_LIST
    }

    override fun onResetClicked() {
        resetList()

        fetchHotDeals()
    }

    private fun fetchHotDeals() {
        isLoading = true

        productInteractor.hotDeals(
            merchantId = User.shared.merchantId!!,
            offset = pageOffset,
            size = pageSize,
            listener = object :
                Subscriber<ProductResponse> {
                override fun onRequestSuccess(response: ProductResponse) {
                    if (response.result == ProductResponse.Result.SUCCESS) {
                        hasMore = (response.products ?: emptyArray()).size == pageSize
                        pageOffset += (response.products ?: emptyArray()).size
                        items.addAll((response.products ?: emptyArray()).toCollection(ArrayList()))
                        lastInsertedItems =
                            (response.products ?: emptyArray()).toCollection(ArrayList())
                        if (hasMore) {
                            pageNumber++
                        }

                        isLoading = false
                        loadCallBack.onLoadMoreFinished(
                            lastInsertedItems,
                            BaseRecyclerViewAdapter.Mode.NORMAL
                        )

                        Log.e("hotDeals", "products: ${response.products?.size}")

                        Log.e("hotDeals", "Success")
                    } else {
                        isLoading = false
                        loadCallBack.onLoadMoreFailed()

                        Log.e("hotDeals", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    isLoading = false
                    loadCallBack.onLoadMoreFailed()

                    Log.e("hotDeals", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }

    override fun getHotDealItems(): ArrayList<ProductResponseModel?> {
        return items
    }

    private fun resetList() {
        items.clear()
        lastInsertedItems.clear()
        pageNumber = 0
        pageOffset = 0
        hasMore = true
        isLoading = false
    }
}