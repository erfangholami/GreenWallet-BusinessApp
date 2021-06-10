package com.greenwallet.business.scenes.productFeatures.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemAvailableSizesBinding
import com.greenwallet.business.network.product.response.ProductVariantsResponseModel

class AvailableSizesAdapter(val items: ArrayList<ProductVariantsResponseModel>) :
    RecyclerView.Adapter<AvailableSizesAdapter.AvailableSizesViewHolder>() {

    lateinit var sizeChangeListener: (ProductVariantsResponseModel) -> (Unit)
    var selectedItemPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AvailableSizesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemAvailableSizesBinding.inflate(layoutInflater, parent, false)
        return AvailableSizesViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AvailableSizesViewHolder, position: Int) {
        holder.bind(items[position], position == selectedItemPosition)
        holder.setOnClickListener {
            if (position != selectedItemPosition) {
                val prevPos = selectedItemPosition
                selectedItemPosition = position
                notifyItemChanged(prevPos)
                notifyItemChanged(selectedItemPosition)
                sizeChangeListener.invoke(items[position])
            }
        }
    }

    inner class AvailableSizesViewHolder(private val itemBinding: ItemAvailableSizesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val selectedTextColor = itemBinding.root.context.resources.getColor(R.color.white, null)
        private val unSelectedTextColor =
            itemBinding.root.context.resources.getColor(R.color.colorDisableDark, null)

        fun bind(
            item: ProductVariantsResponseModel,
            isSelected: Boolean
        ) {
            itemBinding.tvSize.text = item.value
            itemBinding.tvSize.isSelected = isSelected

            if (isSelected) {
                itemBinding.tvSize.setTextColor(selectedTextColor)
            } else {
                itemBinding.tvSize.setTextColor(unSelectedTextColor)
            }
        }

        fun setOnClickListener(function: () -> Unit) {
            itemBinding.root.setOnClickListener {
                function.invoke()
            }
        }
    }
}