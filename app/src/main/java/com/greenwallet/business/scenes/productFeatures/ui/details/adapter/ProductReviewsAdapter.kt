package com.greenwallet.business.scenes.productFeatures.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemReviewBinding
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel

const val LIMIT = 3

class ProductReviewsAdapter(private val reviews: ArrayList<ProductReviewsResponseModel>) :
    RecyclerView.Adapter<ProductReviewsAdapter.ReviewsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemReviewBinding.inflate(layoutInflater, parent, false)
        return ReviewsViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = if (reviews.size > LIMIT) LIMIT else reviews.size

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    inner class ReviewsViewHolder(private val itemBinding: ItemReviewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(reviews: ProductReviewsResponseModel) {
            itemBinding.tvTitle.text = reviews.title
            itemBinding.tvMessage.text = reviews.description
            itemBinding.rbRating.rating = reviews.rating?.toFloat() ?: 0.0f
            itemBinding.tvCreatedBy.text =
                itemBinding.root.context.getString(R.string.label_created_by, reviews.nickname)
        }
    }
}
