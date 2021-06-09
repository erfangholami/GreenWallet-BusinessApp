package com.greenwallet.business.network.productReviews

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber

interface IProductReviewInteractor {

    fun reviews(
        productId: String,
        listener: Subscriber<ProductReviewsResponse>
    ): Disposable?
}