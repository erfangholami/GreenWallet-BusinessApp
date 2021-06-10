package com.greenwallet.business.scenes.productFeatures.ui.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.databinding.ItemAvailableColorsBinding
import com.greenwallet.business.network.product.response.ProductVariantsResponseModel

class AvailableColorsAdapter(val items: ArrayList<ProductVariantsResponseModel>) :
    RecyclerView.Adapter<AvailableColorsAdapter.AvailableColorsViewHolder>() {

    lateinit var colorChangeListener: (ProductVariantsResponseModel) -> (Unit)
    var selectedItemPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AvailableColorsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemAvailableColorsBinding.inflate(layoutInflater, parent, false)
        return AvailableColorsViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AvailableColorsViewHolder, position: Int) {
        holder.bind(items[position], position == selectedItemPosition)
        holder.setOnClickListener {
            if (position != selectedItemPosition) {
                val prevPos = selectedItemPosition
                selectedItemPosition = position
                notifyItemChanged(prevPos)
                notifyItemChanged(selectedItemPosition)
                colorChangeListener.invoke(items[position])
            }
        }
    }

    inner class AvailableColorsViewHolder(private val itemBinding: ItemAvailableColorsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: ProductVariantsResponseModel, isSelected: Boolean) {
            itemBinding.ivCircleView.setFillColor(item.value!!)
            itemBinding.ivCircleView.setStrokeColor(item.value!!)

            if (isSelected) {
                itemBinding.ivCircleView.showOuterCircle(true)
                itemBinding.tvColorName.visibility = View.VISIBLE
                itemBinding.tvColorName.text = item.description
            } else {
                itemBinding.ivCircleView.showOuterCircle(false)
                itemBinding.tvColorName.visibility = View.GONE
            }
        }

        fun setOnClickListener(function: () -> Unit) {
            itemBinding.root.setOnClickListener {
                function.invoke()
            }
        }
    }
}
