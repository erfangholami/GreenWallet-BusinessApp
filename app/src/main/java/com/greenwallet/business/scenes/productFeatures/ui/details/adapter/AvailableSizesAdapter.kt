package com.greenwallet.business.scenes.productFeatures.ui.details.adapter

import android.view.LayoutInflater
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemAvailableSizesBinding
import com.greenwallet.business.network.product.response.ProductVariantsResponseModel

class AvailableSizesAdapter(val items: ArrayList<ProductVariantsResponseModel>, val cgSizes: ChipGroup) {

    lateinit var sizeChangeListener: (ProductVariantsResponseModel) -> (Unit)
    var selectedItem: ProductVariantsResponseModel = items[0]

    private val selectedTextColor = cgSizes.context.resources.getColor(R.color.white, null)
    private val unSelectedTextColor =
        cgSizes.context.resources.getColor(R.color.colorDisableDark, null)

    init {
        with (cgSizes) {

            removeAllViews()

            val layoutInflater = LayoutInflater.from(context)
            for (item in items) {

                val itemBinding = ItemAvailableSizesBinding.inflate(layoutInflater)
                itemBinding.tvSize.text = item.value

                updateItemView(itemBinding.tvSize, item == selectedItem)

                itemBinding.tvSize.setOnClickListener {
                    selectItem(item)
                    if (this@AvailableSizesAdapter::sizeChangeListener.isInitialized) {
                        sizeChangeListener.invoke(item)
                    }
                }

                addView(itemBinding.tvSize)
            }
        }
    }

    fun selectItem(item: ProductVariantsResponseModel) {

        if (item != selectedItem) {
            selectedItem = item
            val itemPosition = items.indexOf(selectedItem)

            cgSizes.children.forEachIndexed { index, view ->
                updateItemView(view as Chip, index == itemPosition)
            }
        }
    }
    
    private fun updateItemView(chip: Chip, isSelected: Boolean) {
        if (isSelected) {
            chip.isSelected = true
            chip.setTextColor(selectedTextColor)
            (chip.chipDrawable as ChipDrawable).setChipBackgroundColorResource(R.color.available_color_selected)
        } else {
            chip.isSelected = false
            chip.setTextColor(unSelectedTextColor)
            (chip.chipDrawable as ChipDrawable).setChipBackgroundColorResource(R.color.white)
        }
    }
}