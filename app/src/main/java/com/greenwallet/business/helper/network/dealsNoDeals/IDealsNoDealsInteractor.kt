package com.greenwallet.business.helper.network.dealsNoDeals

import com.greenwallet.business.helper.network.Disposable
import com.greenwallet.business.helper.network.Subscriber

interface IDealsNoDealsInteractor {

    fun categories(
        merchantId: String,
        listener: Subscriber<CategoriesResponse>
    ): Disposable?
}