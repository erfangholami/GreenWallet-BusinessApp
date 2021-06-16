package com.greenwallet.business.scenes.redeem.ui

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemRedeemListBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.scenes.base.BaseRecyclerViewAdapter
import com.greenwallet.business.scenes.base.ProductItemListener

class RedeemAdapter(private val productItemListener: ProductItemListener) :
    BaseRecyclerViewAdapter<ProductResponseModel>() {

    override fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemRedeemListBinding.inflate(layoutInflater, parent, false)

        return RedeemListItemViewHolder(itemBinding, productItemListener)
    }

    override fun onBindViewHolder2(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RedeemListItemViewHolder).bind(items[position])

        if (!items[position].defaultFileUrl.isNullOrEmpty()) {
            holder.loadImageUrl(items[position].defaultFileUrl!!)
        } else if (!items[position].defaultFileID.isNullOrEmpty()) {
            productItemListener.fetchImage(
                items[position].defaultFileID!!,
                object : ImageLoaderListener {
                    override fun onFetchFinished(image: Bitmap?) {
                        (holder).setImage(image)
                    }
                },
                holder.getImageViewSizes()
            )
        }
    }

    class RedeemListItemViewHolder(
        val itemBinding: ItemRedeemListBinding,
        private val productItemListener: ProductItemListener
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: ProductResponseModel) {
            itemBinding.root.setOnClickListener {
                productItemListener.onItemClicked(item)
            }

            if (item.stock?.available!! > 0) {
                itemBinding.tvOutOfStock.visibility = View.GONE
            } else {
                itemBinding.tvOutOfStock.visibility = View.VISIBLE
            }

            itemBinding.tvRedeemOptionsName.text = item.name

            if (item.maxGreenCoinsPercentage ?: 0 > 0) {
                itemBinding.tvSavings.text = itemBinding.root.context.getString(
                    R.string.label_savings,
                    item.maxGreenCoinsPercentage
                )

                val discountAmount = item.price?.discount?.amount ?: 0

                if (discountAmount > 0) {
                    val coins = ((item.price?.discount?.amount
                        ?: 0).toInt() / 100) * (item.maxGreenCoinsPercentage ?: 0).toInt()

                    itemBinding.tvCoins.text =
                        itemBinding.root.context.getString(R.string.label_coins, coins)
                    itemBinding.tvGreenCoins.text =
                        itemBinding.root.context.getString(R.string.label_redeem_up, coins)
                } else {
                    val coins =
                        ((item.price?.amount ?: 0).toInt() / 100) * (item.maxGreenCoinsPercentage
                            ?: 0).toInt()

                    itemBinding.tvCoins.text =
                        itemBinding.root.context.getString(R.string.label_coins, coins)
                    itemBinding.tvGreenCoins.text =
                        itemBinding.root.context.getString(R.string.label_redeem_up, coins)
                }
            } else {
                itemBinding.tvCoins.text = ""
                itemBinding.tvGreenCoins.text = ""
                itemBinding.tvRedeemOptionsName.text = ""
            }
        }

        fun getImageViewSizes() =
            itemBinding.ivRedeemOptionsImage.layoutParams.run { width to height }

        fun setImage(image: Bitmap?) {
            itemBinding.ivRedeemOptionsImage.setImageBitmap(image)
        }

        fun loadImageUrl(imageUrl: String) {
            Glide.with(itemBinding.root)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_loading)
                .into(itemBinding.ivRedeemOptionsImage)
        }
    }
}