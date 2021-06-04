package com.greenwallet.business.network.product

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber

interface IProductInteractor {

    fun categories(
        merchantId: String,
        listener: Subscriber<CategoriesResponse>
    ): Disposable?
}