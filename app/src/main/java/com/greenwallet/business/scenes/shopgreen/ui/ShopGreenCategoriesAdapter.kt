package com.greenwallet.business.scenes.shopgreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.databinding.ShopgreenRvCategoriesItemBinding
import com.greenwallet.business.helper.shop.getCategoryItem
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import java.util.*

class ShopGreenCategoriesAdapter :
    RecyclerView.Adapter<ShopGreenCategoriesAdapter.ViewHolder>() {

    lateinit var itemClickListener: ((CategoriesResponseModel) -> Unit)

    var items: ArrayList<CategoriesResponseModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding = ShopgreenRvCategoriesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    override fun getItemCount() = items.size

    class ViewHolder(private val itemBinding: ShopgreenRvCategoriesItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            item: CategoriesResponseModel,
            itemClickListener: (CategoriesResponseModel) -> Unit
        ) {
            val categoryItem = getCategoryItem(
                itemBinding.root.resources,
                item.category!!
            )

            itemBinding.ivIcon.setImageDrawable(
                categoryItem?.second
            )

            itemBinding.tvTitle.text = categoryItem?.first
            itemBinding.root.setOnClickListener {
                categoryItem?.first?.let { it1 -> itemClickListener.invoke(item) }
            }
        }

        fun setText(text: String) {
            itemBinding.tvTitle.text = text
        }

        fun setImage(imageIconId: Int) {
            itemBinding.ivIcon.setImageResource(imageIconId)
        }
    }

}