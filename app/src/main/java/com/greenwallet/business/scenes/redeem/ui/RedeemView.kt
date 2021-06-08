package com.greenwallet.business.scenes.redeem.ui

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter
import com.greenwallet.business.scenes.base.LoadMoreCallBack


interface RedeemView {

    interface Presenter {
        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: RedeemView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: RedeemView)

        fun onResetClicked()

        fun onSearchTextChanged(valueToSearch: String)

        fun dismiss()

        fun hasMore(): Boolean

        fun isLoading(): Boolean

        fun onLoadMore()

        fun fetchImage(fileId: String, loaderListener: ImageLoaderListener, sizes: Pair<Int, Int>)

        fun getHotDealItems(): ArrayList<ProductResponseModel?>

        fun setLoadCallBack(loadMoreCallBack: LoadMoreCallBack<ProductResponseModel, BaseRecyclerViewAdapter.Mode>)

        fun onProductClicked(product: ProductResponseModel)

        fun onCartListClicked()
    }
}