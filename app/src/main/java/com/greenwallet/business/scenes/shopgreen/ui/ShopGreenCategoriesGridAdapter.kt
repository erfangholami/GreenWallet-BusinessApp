package com.greenwallet.business.scenes.shopgreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.databinding.ItemCategoryBinding
import com.greenwallet.business.helper.shop.getCategoryItem
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import java.util.*

class ShopGreenCategoriesGridAdapter(
    val items: ArrayList<CategoriesResponseModel>,
    val itemClickListener: (CategoriesResponseModel) -> Unit
) :
    RecyclerView.Adapter<ShopGreenCategoriesGridAdapter.HomeCategoriesGridViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCategoriesGridViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
        return HomeCategoriesGridViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HomeCategoriesGridViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    inner class HomeCategoriesGridViewHolder(private val itemBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(
            item: CategoriesResponseModel,
            itemClickListener: (category: CategoriesResponseModel) -> Unit
        ) {
            val categoryItem = getCategoryItem(
                itemBinding.root.resources,
                item.category!!
            )

            itemBinding.ivCategoryIcon.setImageDrawable(
                categoryItem?.second
            )

            itemBinding.tvCategoryName.text = categoryItem?.first
            itemBinding.root.setOnClickListener {
                categoryItem?.first?.let { it1 -> itemClickListener.invoke(item) }
            }
        }
    }
}
