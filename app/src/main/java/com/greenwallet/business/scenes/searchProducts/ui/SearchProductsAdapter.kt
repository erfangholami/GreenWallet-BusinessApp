package com.greenwallet.business.scenes.searchProducts.ui

import android.graphics.Bitmap
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemSearchProductsGridBinding
import com.greenwallet.business.databinding.ItemSearchProductsListBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.product.response.*
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter
import com.greenwallet.business.scenes.base.ProductItemListener
import java.util.*

class SearchProductsAdapter(
    private val adapterMode: AdapterMode,
    private val productItemListener: ProductItemListener
) : BaseRecyclerViewAdapter<ProductResponseModel>() {

    override fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (adapterMode) {
            AdapterMode.LIST -> {
                val itemBinding =
                    ItemSearchProductsListBinding.inflate(layoutInflater, parent, false)
                SearchProductsListViewHolder(itemBinding, productItemListener)
            }
            AdapterMode.GRID -> {
                val itemBinding =
                    ItemSearchProductsGridBinding.inflate(layoutInflater, parent, false)
                SearchProductsViewHolder(itemBinding, productItemListener)
            }
        }
    }

    override fun onBindViewHolder2(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        val sizes = when (holder) {
            is SearchProductsViewHolder -> holder.getImageViewSizes()
            is SearchProductsListViewHolder -> holder.getImageViewSizes()
            else -> Pair(0, 0)
        }

        when (holder) {
            is SearchProductsViewHolder -> holder.bind(item)
            is SearchProductsListViewHolder -> holder.bind(item)
        }

        if (!items[position].defaultFileUrl.isNullOrEmpty()) {
            when (holder) {
                is SearchProductsViewHolder -> holder.loadImageUrl(items[position].defaultFileUrl!!)
                is SearchProductsListViewHolder -> holder.loadImageUrl(items[position].defaultFileUrl!!)
            }
        } else if (!items[position].defaultFileID.isNullOrEmpty()) {
            productItemListener.fetchImage(items[position].defaultFileID!!, object : ImageLoaderListener {
                override fun onFetchFinished(image: Bitmap?) {
                    when (holder) {
                        is SearchProductsViewHolder -> holder.setImage(image)
                        is SearchProductsListViewHolder -> holder.setImage(image)
                    }
                }
            }, sizes)
        }

        if (item.reviews == null) {
            item.productID?.let {
                productItemListener.fetchReviews(
                    it,
                    object : CallbackListener<ArrayList<ProductReviewsResponseModel>>() {
                        override fun onAPICallFinished(data: ArrayList<ProductReviewsResponseModel>) {
                            items[position].reviews = data

                            when (holder) {
                                is SearchProductsViewHolder -> holder.updateReviews(data)
                                is SearchProductsListViewHolder -> holder.updateReviews(data)
                            }
                        }

                        override fun onAPICallFailed() {

                            when (holder) {
                                is SearchProductsViewHolder -> holder.updateReviews(arrayListOf())
                                is SearchProductsListViewHolder -> holder.updateReviews(arrayListOf())
                            }
                        }
                    })
            }
        } else {
            when (holder) {
                is SearchProductsViewHolder -> holder.updateReviews(item.reviews ?: arrayListOf())
                is SearchProductsListViewHolder -> holder.updateReviews(
                    item.reviews ?: arrayListOf()
                )
            }
        }
    }

    class SearchProductsViewHolder(
        private val itemBinding: ItemSearchProductsGridBinding,
        private val productItemListener: ProductItemListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        lateinit var item: ProductResponseModel

        fun bind(item: ProductResponseModel) {
            this.item = item

            if (item.stock?.available!! > 0) {
                itemBinding.tvOutOfStock.visibility = GONE
            } else {
                itemBinding.tvOutOfStock.visibility = VISIBLE
            }

            itemBinding.content.setOnClickListener { productItemListener.onItemClicked(item) }

            updateReviewListener()

            itemBinding.tvName.text = item.name
            itemBinding.tvReviews.text =
                itemBinding.root.context.getString(
                    R.string.label_reviews,
                    (item.reviews ?: arrayListOf()).size
                )

            if (item.isHotDeal()) {
                itemBinding.tvOldPrice.visibility = VISIBLE

                val oldPrice: Float = item.getOldPrice().toFloat().div(100)
                val price: Float = item.getPrice().toFloat() / 100.0f
                val discount: Int = (100 - ((price * 100) / oldPrice)).toInt()

                itemBinding.tvOff.text =
                    itemBinding.root.context.getString(R.string.label_off, discount)
                itemBinding.tvPrice.text =
                    itemBinding.root.context.getString(
                        R.string.label_price,
                        price
                    )
                itemBinding.tvOldPrice.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = itemBinding.root.context.getString(
                        R.string.label_price,
                        oldPrice
                    )
                }
            } else {
                itemBinding.tvOff.visibility = GONE
                itemBinding.tvOldPrice.visibility = GONE

                itemBinding.tvPrice.text =
                    itemBinding.root.context.getString(
                        R.string.label_price,
                        item.getPrice().toFloat().div(100)
                    )
            }
        }

        fun getImageViewSizes() = itemBinding.ivImage.layoutParams.run { width to height }

        fun setImage(image: Bitmap?) {
            itemBinding.ivImage.setImageBitmap(image)
        }

        fun loadImageUrl(imageUrl: String) {
            Glide.with(itemBinding.root)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_loading)
                .into(itemBinding.ivImage)
        }

        fun updateReviews(reviews: ArrayList<ProductReviewsResponseModel>) {
            var rate = 0
            for (review in reviews) {
                rate += review.rating ?: 0
            }

            itemBinding.rbRating.rating = if (reviews.size > 0) {
                rate.toFloat() / reviews.size
            } else {
                0.0f
            }
            itemBinding.tvReviews.text =
                itemBinding.root.context.getString(R.string.label_reviews, reviews.size)

            updateReviewListener()

        }

        private fun updateReviewListener() {
            itemBinding.clRatingContainer.setOnClickListener {
                productItemListener.onItemReviewClicked(item.productID!!, item.reviews ?: arrayListOf())
            }
        }
    }

    class SearchProductsListViewHolder(
        private val itemBinding: ItemSearchProductsListBinding,
        private val productItemListener: ProductItemListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        lateinit var item: ProductResponseModel

        fun bind(item: ProductResponseModel) {
            this.item = item

            if (item.stock?.available!! > 0) {
                itemBinding.tvOutOfStock.visibility = GONE
            } else {
                itemBinding.tvOutOfStock.visibility = VISIBLE
            }

            itemBinding.content.setOnClickListener { productItemListener.onItemClicked(item) }

            updateReviewListener()

            itemBinding.tvName.text = item.name
            itemBinding.tvReviews.text =
                itemBinding.root.context.getString(
                    R.string.label_reviews,
                    (item.reviews ?: arrayListOf()).size
                )

            if (item.isHotDeal()) {
                itemBinding.tvOldPrice.visibility = VISIBLE

                val oldPrice: Float = item.getOldPrice().toFloat().div(100)
                val price: Float = item.getPrice().toFloat() / 100.0f
                val discount: Int = (100 - ((price * 100) / oldPrice)).toInt()

                itemBinding.tvOff.text =
                    itemBinding.root.context.getString(R.string.label_off, discount)
                itemBinding.tvPrice.text =
                    itemBinding.root.context.getString(
                        R.string.label_price,
                        price
                    )
                itemBinding.tvOldPrice.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = itemBinding.root.context.getString(
                        R.string.label_price,
                        oldPrice
                    )
                }
            } else {
                itemBinding.tvOff.visibility = GONE
                itemBinding.tvOldPrice.visibility = GONE

                itemBinding.tvPrice.text =
                    itemBinding.root.context.getString(
                        R.string.label_price,
                        item.getPrice().toFloat().div(100)
                    )
            }
        }

        fun getImageViewSizes() = itemBinding.ivImage.layoutParams.run { width to height }

        fun setImage(image: Bitmap?) {
            itemBinding.ivImage.setImageBitmap(image)
        }

        fun loadImageUrl(imageUrl: String) {
            Glide.with(itemBinding.root)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_loading)
                .into(itemBinding.ivImage)
        }

        private fun updateReviewListener() {
            itemBinding.clRatingContainer.setOnClickListener {
                productItemListener.onItemReviewClicked(item.productID!!, item.reviews ?: arrayListOf())
            }
        }

        fun updateReviews(reviews: ArrayList<ProductReviewsResponseModel>) {
            var rate = 0
            for (review in reviews) {
                rate += review.rating ?: 0
            }

            itemBinding.rbRating.rating = if (reviews.size > 0) {
                rate.toFloat() / reviews.size
            } else {
                0.0f
            }
            itemBinding.tvReviews.text =
                itemBinding.root.context.getString(R.string.label_reviews, reviews.size)

            updateReviewListener()
        }
    }
}

enum class AdapterMode {
    LIST, GRID
}