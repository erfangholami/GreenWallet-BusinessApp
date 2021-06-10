package com.greenwallet.business.helper.shop

import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel

fun getOverallRating(reviews: ArrayList<ProductReviewsResponseModel>?): Float {
    if (reviews == null) {
        return 0.0f
    } else if (reviews.size == 0) {
        return 0.0f
    } else {
        var rate5Size = 0
        var rate4Size = 0
        var rate3Size = 0
        var rate2Size = 0
        var rate1Size = 0

        for (review in reviews) {
            when (review.rating) {
                1 -> rate1Size++
                2 -> rate2Size++
                3 -> rate3Size++
                4 -> rate4Size++
                5 -> rate5Size++
            }
        }

        return ((rate1Size) + (rate2Size * 2) + (rate3Size * 3) + (rate4Size * 4) + (rate5Size * 5)).toFloat() / reviews.size
    }
}