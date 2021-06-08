package com.greenwallet.business.scenes.searchProducts.ui

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.product.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter
import com.greenwallet.business.scenes.base.LoadMoreCallBack
import com.greenwallet.business.scenes.searchProducts.SearchProductsPresenter


interface SearchProductsView {

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: SearchProductsView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: SearchProductsView)

        fun getSearchProductsList(): ArrayList<ProductResponseModel>

        fun getLastInsertedList(): ArrayList<ProductResponseModel>

        fun fetchImage(fileId: String, loaderListener: ImageLoaderListener, sizes: Pair<Int, Int>)

        fun onSearchTextChanged(query: String)

        fun onBackPressed()

        fun hasMore(): Boolean

        fun isLoading(): Boolean

        fun onLoadMore()

        fun onCartListClicked()

        fun onItemClicked(product: ProductResponseModel)

        fun onItemReviewClicked(
            productID: String,
            reviews: java.util.ArrayList<ProductReviewsResponseModel>
        )

        fun setLoadCallBack(loadMoreCallBack: LoadMoreCallBack<ProductResponseModel, BaseRecyclerViewAdapter.Mode>)

        fun fetchReviews(
            productId: String,
            listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>
        )

        fun getSearchText(): String
    }

    fun initValues(
        mode: SearchProductsPresenter.Mode,
        defaultTitle: String? = null,
        defaultDescription: String? = null
    )
}