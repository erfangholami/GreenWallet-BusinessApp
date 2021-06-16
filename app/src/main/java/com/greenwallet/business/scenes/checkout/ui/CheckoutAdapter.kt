package com.greenwallet.business.scenes.checkout.ui

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemCheckoutBinding
import com.greenwallet.business.helper.shop.getCheckoutItemPrices
import com.greenwallet.business.helper.shop.getFreeShippingAmount
import com.greenwallet.business.helper.shop.getShippingPrices
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.scenes.basket.model.CheckoutItem

class CheckoutAdapter(
    private val editClickListener: EditClickListener,
    private val checkoutItemListener: CheckoutItemChangeListener
) : RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder>() {

    var items: MutableList<CheckoutItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemCheckoutBinding.inflate(layoutInflater, parent, false)

        return CheckoutViewHolder(itemBinding, viewPool)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
    }

    inner class CheckoutViewHolder(
        private val itemBinding: ItemCheckoutBinding,
        viewPool: RecyclerView.RecycledViewPool
    ) :
        RecyclerView.ViewHolder(itemBinding.root),
        CheckoutProductAdapter.ProductItemListener {

        private val context = itemBinding.root.context

        private val adapter = CheckoutProductAdapter(this)

        init {
            itemBinding.rvCheckoutProducts.setRecycledViewPool(viewPool)
            itemBinding.rvCheckoutProducts.isNestedScrollingEnabled = false
            itemBinding.rvCheckoutProducts.addItemDecoration(object :
                RecyclerView.ItemDecoration() {
                private val firstStartMargin =
                    context!!.resources.getDimensionPixelSize(R.dimen.margin_default)

                override fun getItemOffsets(
                    outRect: Rect, view: View, parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.left = firstStartMargin
                    } else if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
                        outRect.right = firstStartMargin
                    }
                }
            })
            itemBinding.rvCheckoutProducts.adapter = adapter
        }

        fun bind(item: CheckoutItem) {

            adapter.isSelectingShippingMethodAvailable =
                (item.totalAmount < item.freeShippingAmount) || (item.freeShippingAmount == 0)
            adapter.items = item.products

            itemBinding.tvType.text = item.type
            itemBinding.tvItemCount.text =
                context.getString(R.string.label_products_count, item.products.size.toString())

            updateFreeShippingAmount(item)

            itemBinding.ivEdit.setOnClickListener {
                editClickListener.onEditCheckoutItemClicked(items[adapterPosition])
            }
        }

        private fun updateFreeShippingAmount(item: CheckoutItem) {
            item.freeShippingAmount = getFreeShippingAmount(item.products)
            val prices = getCheckoutItemPrices(item.products)
            item.totalAmount = prices.first
            item.totalVat = prices.second

            if (item.totalAmount >= item.freeShippingAmount) {
                item.shippingAmount = 0
            } else {
                item.shippingAmount = getShippingPrices(item.products)
            }

            if (item.freeShippingAmount > 0) {
                itemBinding.clFreeShippingContainer.visibility = VISIBLE
                itemBinding.tvShippingInfo.text =
                    context.getString(
                        R.string.label_free_shipping,
                        item.freeShippingAmount.toFloat().div(100).toInt(),
                        item.type
                    )
            } else {
                itemBinding.clFreeShippingContainer.visibility = GONE
            }
        }

        override fun onItemEditShippingClicked(productItem: CartProduct) {
            checkoutItemListener.onItemEditShippingClicked(productItem)
        }

        override fun onProductItemCountChanged(productItem: CartProduct) {
            val context = itemBinding.root.context

            val checkoutItem = items.find {
                productItem.product!!.merchantID == it.id
            }

            checkoutItem?.let {
                updateFreeShippingAmount(checkoutItem)
                if (it.products.size > 0) {
                    notifyItemChanged(adapterPosition)
                } else {
                    items.remove(it)
                    notifyItemRemoved(adapterPosition)
                }
                checkoutItemListener.onCheckoutItemCountChanged(productItem)

            }
        }

        override fun onImageLoadListener(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>?
        ) {
            checkoutItemListener.onImageLoadListener(fileId, loaderListener, sizes)
        }
    }

    interface EditClickListener {
        fun onEditCheckoutItemClicked(item: CheckoutItem)
    }

    interface CheckoutItemChangeListener {
        fun onCheckoutItemCountChanged(cartItem: CartProduct)

        fun onImageLoadListener(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>? = null
        )

        fun onItemEditShippingClicked(productItem: CartProduct)
    }
}
