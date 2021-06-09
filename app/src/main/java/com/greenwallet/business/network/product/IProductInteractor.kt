package com.greenwallet.business.network.product

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber

interface IProductInteractor {

    fun search(
        query: String,
        offset: Int,
        size: Int,
        listener: Subscriber<ProductResponse>
    ): Disposable?

    fun categories(
        merchantId: String,
        listener: Subscriber<CategoriesResponse>
    ): Disposable?

    fun productsByCategory(
        categoryName: String,
        offset: Int,
        size: Int,
        listener: Subscriber<ProductResponse>
    ): Disposable?

    fun hotDeals(
        merchantId: String,
        offset: Int,
        size: Int,
        listener: Subscriber<ProductResponse>
    ): Disposable?

    fun bestSellers(
        merchantId: String,
        offset: Int,
        size: Int,
        listener: Subscriber<ProductResponse>
    ): Disposable?

    fun shipments(
        merchantId: String,
        productId: String,
        listener: Subscriber<ProductShipmentsResponse>
    ): Disposable?

    fun variants(
        merchantId: String,
        productId: String,
        listener: Subscriber<ProductVariantsResponse>
    ): Disposable?

    fun variations(
        merchantId: String,
        productId: String,
        variations: Array<Pair<String, String>>,
        listener: Subscriber<ProductVariationsResponse>
    ): Disposable?
}