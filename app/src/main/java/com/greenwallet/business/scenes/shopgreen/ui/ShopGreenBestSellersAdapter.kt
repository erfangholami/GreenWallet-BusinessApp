package com.greenwallet.business.scenes.shopgreen.ui

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemBestSellersBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.CallbackListener
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.product.response.ProductReviewsResponseModel
import com.greenwallet.business.network.product.response.getPrice

class ShopGreenBestSellersAdapter :
    RecyclerView.Adapter<ShopGreenBestSellersAdapter.HomeBestSellersViewHolder>() {

    lateinit var reviewsLoaderListener: (id: String, listener: CallbackListener<ArrayList<ProductReviewsResponseModel>>) -> (Unit)
    lateinit var itemClickListener: (product: ProductResponseModel) -> (Unit)
    lateinit var reviewClickListener: (productID: String, reviews: ArrayList<ProductReviewsResponseModel>) -> (Unit)
    lateinit var imageLoaderListener: (id: String, listener: ImageLoaderListener, sizes: Pair<Int, Int>) -> (Unit)

    var items: ArrayList<ProductResponseModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeBestSellersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemBestSellersBinding.inflate(layoutInflater, parent, false)

        return HomeBestSellersViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HomeBestSellersViewHolder, position: Int) {
        if (!items[position].defaultFileUrl.isNullOrEmpty()) {
            holder.loadImageUrl(items[position].defaultFileUrl!!)
        } else if (!items[position].defaultFileID.isNullOrEmpty()) {
            imageLoaderListener.invoke(items[position].defaultFileID!!, object : ImageLoaderListener {
                override fun onFetchFinished(image: Bitmap?) {
                    holder.setImage(image)
                }
            }, holder.getImageViewSizes())
        }

        if (items[position].reviews == null) {
            items[position].productID?.let {
                reviewsLoaderListener.invoke(
                    it,
                    object : CallbackListener<ArrayList<ProductReviewsResponseModel>>() {
                        override fun onAPICallFinished(data: ArrayList<ProductReviewsResponseModel>) {
                            items[position].reviews = data
                            holder.updateReviews(data)
                        }

                        override fun onAPICallFailed() {
                            holder.updateReviews(arrayListOf())
                        }
                    })
            }
        } else {
            holder.updateReviews(items[position].reviews ?: arrayListOf())
        }

        holder.bind(items[position], itemClickListener)
    }

    inner class HomeBestSellersViewHolder(private val itemBinding: ItemBestSellersBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        lateinit var item: ProductResponseModel
        fun bind(
            item: ProductResponseModel,
            itemClickListener: (product: ProductResponseModel) -> Unit
        ) {

            this.item = item
            itemBinding.root.setOnClickListener {
                itemClickListener.invoke(item)
            }

            if (item.stock?.available!! > 0) {
                itemBinding.tvOutOfStock.visibility = View.GONE
            } else {
                itemBinding.tvOutOfStock.visibility = View.VISIBLE
            }

            updateReviewListener()

            itemBinding.tvBestSellersName.text = item.name
//            itemBinding.rbRating.rating = item.rate
            itemBinding.tvReviews.text =
                itemBinding.root.context.getString(
                    R.string.label_reviews,
                    (item.reviews ?: arrayListOf()).size
                )
            itemBinding.tvPrice.text =
                itemBinding.root.context.getString(
                    R.string.label_price,
                    item.getPrice().toFloat().div(100.0f)
                )
        }

        fun getImageViewSizes() =
            itemBinding.ivBestSellersImage.layoutParams.run { width to height }

        fun setImage(image: Bitmap?) {
            itemBinding.ivBestSellersImage.setImageBitmap(image)
        }

        fun loadImageUrl(imageUrl: String) {
            Glide.with(itemBinding.root)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_loading)
                .into(itemBinding.ivBestSellersImage)
        }

        fun updateReviews(
            reviews: ArrayList<ProductReviewsResponseModel>
        ) {
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
                reviewClickListener.invoke(item.productID!!, item.reviews ?: arrayListOf())
            }
        }
    }
}
