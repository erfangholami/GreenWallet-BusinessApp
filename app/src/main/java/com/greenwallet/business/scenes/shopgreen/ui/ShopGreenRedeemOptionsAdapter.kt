package com.greenwallet.business.scenes.shopgreen.ui

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemRedeemOptionsBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.scenes.base.ProductItemListener

class ShopGreenRedeemOptionsAdapter(private val productItemListener: ProductItemListener) :
    RecyclerView.Adapter<ShopGreenRedeemOptionsAdapter.RedeemOptionsViewHolder>() {

    var items: ArrayList<ProductResponseModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RedeemOptionsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemRedeemOptionsBinding.inflate(layoutInflater, parent, false)

        return RedeemOptionsViewHolder(itemBinding, productItemListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RedeemOptionsViewHolder, position: Int) {

        if (!items[position].defaultFileUrl.isNullOrEmpty()) {
            holder.loadImageUrl(items[position].defaultFileUrl!!)
        } else if (!items[position].defaultFileID.isNullOrEmpty()) {
            productItemListener.fetchImage(
                items[position].defaultFileID!!,
                object : ImageLoaderListener {
                    override fun onFetchFinished(image: Bitmap?) {
                        holder.setImage(image)
                    }
                },
                holder.getImageViewSizes()
            )
        }

        holder.bind(items[position])
    }

    class RedeemOptionsViewHolder(
        private val itemBinding: ItemRedeemOptionsBinding,
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

