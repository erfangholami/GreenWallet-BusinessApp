package com.greenwallet.business.network.dealsNoDeals

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber

interface IDealsNoDealsInteractor {

    fun categories(
        merchantId: String,
        listener: Subscriber<CategoriesResponse>
    ): Disposable?
}