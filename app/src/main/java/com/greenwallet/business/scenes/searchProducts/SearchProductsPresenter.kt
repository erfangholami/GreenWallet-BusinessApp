package com.greenwallet.business.scenes.searchProducts

import android.content.Context
import android.util.Log
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.InteractorFactory
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.product.ProductResponse
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.product.response.ProductReviewsResponseModel
import com.greenwallet.business.network.product.response.isHotDeal
import com.greenwallet.business.network.product.response.isMatchedWithCategory
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter
import com.greenwallet.business.scenes.base.LoadMoreCallBack
import com.greenwallet.business.scenes.searchProducts.ui.SearchProductsView
import java.util.*
import kotlin.collections.ArrayList

class SearchProductsPresenter(context: Context) :
    BasePresenter<SearchProductsView, SearchProductsProcessHandler>(context),
    SearchProductsView.Presenter {

    enum class State {
        SEARCH_PRODUCTS,
        LOADING,
    }

    enum class Mode {
        DISCOVER,
        CATEGORY,
        HOT_DEALS,
        BEST_SELLERS,
    }

    var state: State = State.SEARCH_PRODUCTS
        set(value) {
            field = value
            when (state) {
                State.SEARCH_PRODUCTS -> {
                    activityHandler?.showSearchProductsScreen()
                }
                State.LOADING -> activityHandler?.showLoadingScreen()
            }
        }

    private val productInteractor = InteractorFactory(context).createProductInteractor()
    //todo: After adding reviews
//    private val productReviewInteractor =
//        InteractorFactory(this.context).createProductReviewInteractor()

    var mode = Mode.HOT_DEALS
    var categoryName: String? = null

    private lateinit var loadCallBack: LoadMoreCallBack<ProductResponseModel, BaseRecyclerViewAdapter.Mode>
    var items: ArrayList<ProductResponseModel> = arrayListOf()
    var lastInsertedItems: ArrayList<ProductResponseModel> = arrayListOf()
    var hasMore = true
    val pageSize = 50
    var pageOffset = 0
    var pageNumber = 0
    private var isLoading = false

    var searchQuery = ""

    override fun onViewSubscribed(view: SearchProductsView) {
        view.initValues(mode, categoryName, null)
    }

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun getSearchProductsList(): ArrayList<ProductResponseModel> {
        val newList = arrayListOf<ProductResponseModel>()
        newList.addAll(items)
        return newList
    }

    override fun getLastInsertedList(): ArrayList<ProductResponseModel> {
        return lastInsertedItems
    }

    override fun onSearchTextChanged(query: String) {
        resetList()
        searchQuery = query
    }

    private fun fetchProductsByCategory(category: String) {
        isLoading = true

        productInteractor.productsByCategory(
            categoryName = category,
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

                        Log.e("productsByCategory", "products: ${response.products?.size}")

                        Log.e("productsByCategory", "Success")
                    } else {
                        isLoading = false
                        loadCallBack.onLoadMoreFailed()

                        Log.e("productsByCategory", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    isLoading = false
                    loadCallBack.onLoadMoreFailed()

                    Log.e("productsByCategory", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
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
                        val now = Date().time
                        val productsToShow =
                            (response.products ?: emptyArray()).toCollection(ArrayList()).filter {
                                it.price?.discount?.endTime ?: 0 >= now
                            }

                        hasMore = productsToShow.size == pageSize
                        pageOffset += productsToShow.size
                        items.addAll(productsToShow.toCollection(ArrayList()))
                        lastInsertedItems = productsToShow.toCollection(ArrayList())
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

    private fun fetchBestSellers() {
        isLoading = true

        productInteractor.bestSellers(
            offset = pageOffset,
            size = pageSize,
            merchantId = User.shared.merchantId!!,
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

                        Log.e("bestSellers", "products: ${response.products?.size}")

                        Log.e("bestSellers", "Success")
                    } else {
                        isLoading = false
                        loadCallBack.onLoadMoreFailed()

                        Log.e("bestSellers", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    isLoading = false
                    loadCallBack.onLoadMoreFailed()

                    Log.e("bestSellers", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }

    private fun search(searchedText: String) {
        isLoading = true

        productInteractor.search(
            query = searchedText,
            offset = pageOffset,
            size = pageSize,
            listener = object :
                Subscriber<ProductResponse> {
                override fun onRequestSuccess(response: ProductResponse) {
                    if (response.result == ProductResponse.Result.SUCCESS) {
                        hasMore = (response.products ?: emptyArray()).size == pageSize
                        pageOffset += (response.products ?: emptyArray()).size

                        val filteredList: ArrayList<ProductResponseModel> = when (mode) {
                            Mode.HOT_DEALS -> {
                                val now = Date().time
                                (response.products ?: emptyArray()).toCollection(ArrayList())
                                    .filter {
                                        it.isHotDeal() && it.price?.discount?.endTime ?: 0 >= now
                                    }.toCollection(ArrayList())
                            }
                            Mode.CATEGORY -> {
                                (response.products ?: emptyArray()).toCollection(ArrayList())
                                    .filter {
                                        it.isMatchedWithCategory(categoryName!!)
                                    }.toCollection(ArrayList())
                            }
                            Mode.BEST_SELLERS -> {
                                (response.products ?: emptyArray()).toCollection(ArrayList())
                                    .filter {
                                        !it.isHotDeal()
                                    }.toCollection(ArrayList())
                            }
                            else -> {
                                (response.products ?: emptyArray()).toCollection(ArrayList())
                            }
                        }

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

    override fun fetchReviews(
        productId: String,
        listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
    ) {

        //todo
//        productReviewInteractor.reviews(
//            productId = productId,
//            listener = object :
//                Subscriber<ProductReviewsResponse> {
//                override fun onRequestSuccess(response: ProductReviewsResponse) {
//                    if (response.response == ProductReviewsResponse.Result.SUCCESS) {
//                        listener.onAPICallFinished(
//                            (response.reviews ?: arrayOf()).toCollection(
//                                ArrayList()
//                            )
//                        )
//
//                        Log.e("ProductReviews", "reviews: ${response.reviews?.size}")
//
//                        Log.e("ProductReviews", "Success")
//                    } else {
//                        listener.onAPICallFailed()
//
//                        Log.e("ProductReviews", "Error")
//                    }
//                }
//
//                override fun onRequestFailure(t: Throwable) {
//                    listener.onAPICallFailed()
//
//                    Log.e("ProductReviews", "onRequestFailure")
//                }
//
//                override fun onUserUnauthorized() {
//                    activityHandler?.userShouldReAuthenticate()
//                }
//            })
    }

    override fun getSearchText(): String {
        return searchQuery
    }

    override fun onBackPressed() {
        activityHandler?.onBackPressed()
    }

    override fun hasMore(): Boolean {
        return hasMore
    }

    override fun isLoading(): Boolean {
        return isLoading
    }

    override fun onLoadMore() {
        if (hasMore && !isLoading) {
            if (searchQuery.isEmpty()) {
                when (mode) {
                    Mode.DISCOVER -> search(searchQuery)
                    Mode.CATEGORY -> fetchProductsByCategory(categoryName!!)
                    Mode.HOT_DEALS -> fetchHotDeals()
                    Mode.BEST_SELLERS -> fetchBestSellers()
                }
            } else {
                search(searchQuery)
            }
        }
    }

    override fun onCartListClicked() {
        activityHandler?.shoeCartScreen()
    }

    override fun onItemClicked(product: ProductResponseModel) {
        activityHandler?.onItemClicked(product)
    }

    override fun onItemReviewClicked(
        productID: String,
        reviews: ArrayList<ProductReviewsResponseModel>
    ) {
        activityHandler?.onItemReviewClicked(productID, reviews)
    }

    override fun setLoadCallBack(loadMoreCallBack: LoadMoreCallBack<ProductResponseModel, BaseRecyclerViewAdapter.Mode>) {
        loadCallBack = loadMoreCallBack
    }

    private fun resetList() {
        items.clear()
        lastInsertedItems.clear()
        pageNumber = 0
        pageOffset = 0
        searchQuery = ""
        hasMore = true
        isLoading = false
    }
}
