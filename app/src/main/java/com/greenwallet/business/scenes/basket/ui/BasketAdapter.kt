package com.greenwallet.business.scenes.basket.ui

import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemBasketProductsBinding
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.helper.ui.SwipeToDeleteCallback
import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.network.product.response.getPrice

class BasketAdapter :
    RecyclerView.Adapter<BasketAdapter.BasketViewHolder>(), SwipeToDeleteCallback.SwipeListener {

    var items: MutableList<CartProduct> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var deleteListener: (CartProduct) -> (Unit)

    lateinit var itemClickListener: (product: CartProduct) -> (Unit)

    lateinit var imageLoaderListener: (id: String, listener: ImageLoaderListener, sizes: Pair<Int, Int>) -> (Unit)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasketViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemBasketProductsBinding.inflate(layoutInflater, parent, false)

        itemBinding.tvCount.background = GradientDrawable().apply {
            setColor(parent.context.resources.getColor(R.color.colorDisable))
            cornerRadius =
                parent.context.resources.getDimensionPixelSize(R.dimen.corner_radius_small_x)
                    .toFloat()
        }

        return BasketViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val item = items[position]

        if (item.variation != null) {
            if (!item.variation!!.defaultFileUrl.isNullOrEmpty()) {
                holder.loadImageUrl(item.variation!!.defaultFileUrl!!)
            } else if (!item.variation!!.defaultFileId.isNullOrEmpty()) {
                imageLoaderListener.invoke(item.variation!!.defaultFileId!!, object : ImageLoaderListener {
                    override fun onFetchFinished(image: Bitmap?) {
                        holder.setImage(image)
                    }
                }, holder.getImageViewSizes())
            }
        } else {
            if (!item.product?.defaultFileUrl.isNullOrEmpty()) {
                holder.loadImageUrl(item.product?.defaultFileUrl!!)
            } else if (!item.product?.defaultFileID.isNullOrEmpty()) {
                imageLoaderListener.invoke(item.product?.defaultFileID!!, object : ImageLoaderListener {
                    override fun onFetchFinished(image: Bitmap?) {
                        holder.setImage(image)
                    }
                }, holder.getImageViewSizes())
            }
        }

        holder.bind(items[position], itemClickListener)
    }

    override fun deleteItem(position: Int) {
        val item = items.removeAt(position)
        notifyItemRemoved(position)

        deleteListener.invoke(item)
    }

    inner class BasketViewHolder(private val itemBinding: ItemBasketProductsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        lateinit var backgroundView: View
        lateinit var foregroundView: View

        fun bind(item: CartProduct, itemClickListener: (product: CartProduct) -> Unit) {
            val context = itemBinding.root.context

            itemBinding.root.setOnClickListener {
                itemClickListener.invoke(item)
            }

            backgroundView = itemBinding.flViewBackground
            foregroundView = itemBinding.cvViewForeground
            itemBinding.tvName.text = item.product?.name
            var typeText = ""
            item.variants.forEachIndexed { index, variant ->
                typeText += "${variant.description}"
                if (index < item.variants.size - 1) {
                    typeText += ", "
                }
            }
            itemBinding.tvType.text = typeText

            updateCountView(item)

            itemBinding.btnCountDown.setOnClickListener {
                item.quantity--
                if (item.quantity < 1) {
                    item.quantity = 1
                }
                updateCountView(item)
                User.shared.updateProductInCart(item)
            }

            itemBinding.btnCountUp.setOnClickListener {
                item.quantity++
                updateCountView(item)
                User.shared.updateProductInCart(item)
            }
        }

        private fun updateCountView(item: CartProduct) {
            itemBinding.tvCount.text = item.quantity.toString()

            itemBinding.tvPrice.text =
                itemBinding.root.context.getString(
                    R.string.label_price,
                    item.getPrice().toFloat().div(100)
                )

            itemBinding.btnCountDown.isEnabled = item.quantity > 1
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
    }
}