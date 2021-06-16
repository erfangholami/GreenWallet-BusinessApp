package com.greenwallet.business.scenes.checkout.ui

import android.graphics.Bitmap
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemCheckoutProductBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.*

class CheckoutProductAdapter(
    private val productItemListener: ProductItemListener
) :
    RecyclerView.Adapter<CheckoutProductAdapter.CheckoutProductViewHolder>() {

    var isSelectingShippingMethodAvailable: Boolean = true
    var items: MutableList<CartProduct> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemCheckoutProductBinding.inflate(layoutInflater, parent, false)
        return CheckoutProductViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CheckoutProductViewHolder, position: Int) {
        holder.isShippingSelectionAvailable(isSelectingShippingMethodAvailable)
        holder.bind(items[holder.adapterPosition])
    }

    inner class CheckoutProductViewHolder(private val itemBinding: ItemCheckoutProductBinding) :
        RecyclerView.ViewHolder(itemBinding.root), ImageLoaderListener {
        private val context = itemBinding.root.context

        fun bind(item: CartProduct) {
            itemBinding.tvTitle.text = item.product!!.name

            itemBinding.ivEditShipping.setOnClickListener {
                productItemListener.onItemEditShippingClicked(item)
            }

            item.selectedShipmentMethod.let {
                itemBinding.tvDeliveryOption.text =
                    item.selectedShipmentMethod?.getShippingMethodName() + " (" +
                            context.getString(
                                R.string.label_price,
                                item.selectedShipmentMethod?.amount?.toFloat()?.div(100)
                            ) +
                            ")"
            }

            var typeText = ""
            item.variants.forEachIndexed { index, variant ->
                typeText += "${variant.description}"
                if (index < item.variants.size - 1) {
                    typeText += ", "
                }
            }
            itemBinding.tvVariation.text = typeText

            updatePriceAndCountText(item)

            var imageUrl: String? = null
            var imageId: String? = null

            if (item.variation != null) {
                if (!item.variation!!.defaultFileUrl.isNullOrEmpty()) {
                    imageUrl = item.variation!!.defaultFileUrl!!
                } else if (!item.variation!!.defaultFileId.isNullOrEmpty()) {
                    imageId = item.variation!!.defaultFileId!!
                }
            } else {
                if (!item.product!!.defaultFileUrl.isNullOrEmpty()) {
                    imageUrl = item.product!!.defaultFileUrl!!
                } else if (!item.product!!.defaultFileID.isNullOrEmpty()) {
                    imageId = item.product!!.defaultFileID!!
                }
            }

            if (imageUrl != null) {
                loadImageUrl(imageUrl)
            } else if (imageId != null) {
                productItemListener.onImageLoadListener(imageId, this, sizes = getImageViewSizes())
            }

            itemBinding.tvPlus.setOnClickListener {
                item.quantity++
                updatePriceAndCountText(item)
                updateItemCount(adapterPosition)
            }
            itemBinding.tvMinus.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    updatePriceAndCountText(item)
                    updateItemCount(adapterPosition)
                }
            }
            itemBinding.ivDelete.setOnClickListener {
                removeItem(adapterPosition)
            }
        }

        fun getImageViewSizes() = itemBinding.ivProductImage.layoutParams.run { width to height }

        fun loadImageUrl(imageUrl: String) {
            Glide.with(itemBinding.root)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_loading)
                .into(itemBinding.ivProductImage)
        }

        fun isShippingSelectionAvailable(isAvailable: Boolean) {
            if (isAvailable) {
                itemBinding.ivEditShipping.visibility = VISIBLE
                itemBinding.tvDeliveryOption.visibility = VISIBLE
            } else {
                itemBinding.ivEditShipping.visibility = INVISIBLE
                itemBinding.tvDeliveryOption.visibility = INVISIBLE
            }
        }

        private fun updatePriceAndCountText(item: CartProduct) {
            itemBinding.tvCounter.text = item.quantity.toString()

            if (item.product!!.isHotDeal()) {

                val oldPrice = item.getOldPrice().toFloat().div(100)
                val price: Float = item.getPrice().toFloat().div(100)

                if (itemBinding.tvOldPrice.visibility != VISIBLE) {
                    itemBinding.tvOldPrice.visibility = VISIBLE
                }

                itemBinding.tvPrice.text =
                    context.getString(
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
                if (itemBinding.tvOldPrice.visibility != GONE) {
                    itemBinding.tvOldPrice.visibility = GONE
                }

                itemBinding.tvPrice.text =
                    context.getString(
                        R.string.label_price,
                        item.getPrice().toFloat().div(100)
                    )
            }
        }

        override fun onFetchFinished(image: Bitmap?) {
            itemBinding.ivProductImage.setImageBitmap(image)
        }

        override fun onFetchFailed() {

        }
    }

    fun updateItemCount(position: Int) {
        val item = items[position]
        productItemListener.onProductItemCountChanged(item)
    }

    fun removeItem(position: Int) {
        val item = items[position]
        items.remove(item)
        notifyItemRemoved(position)
        item.quantity = 0
        productItemListener.onProductItemCountChanged(item)
    }

    interface ProductItemListener {
        fun onItemEditShippingClicked(productItem: CartProduct)

        fun onProductItemCountChanged(productItem: CartProduct)

        fun onImageLoadListener(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>? = null
        )
    }
}
