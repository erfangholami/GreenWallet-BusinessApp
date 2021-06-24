package com.greenwallet.business.scenes.shopgreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.databinding.ItemHomeCategoriesBinding
import com.greenwallet.business.network.product.response.CategoriesResponseModel
import java.util.*

class ShopGreenCategoriesAdapter :
    RecyclerView.Adapter<ShopGreenCategoriesAdapter.HomeCategoriesPageViewHolder>() {

    companion object {
        private const val SPAN_NUMBER = 3
    }

    lateinit var itemClickListener: ((CategoriesResponseModel) -> Unit)

    var items: ArrayList<ArrayList<CategoriesResponseModel>> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCategoriesPageViewHolder {
        val itemBinding = ItemHomeCategoriesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HomeCategoriesPageViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HomeCategoriesPageViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    override fun getItemCount() = items.size

    class HomeCategoriesPageViewHolder(private val itemBinding: ItemHomeCategoriesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            items: ArrayList<CategoriesResponseModel>,
            itemClickListener: (CategoriesResponseModel) -> Unit
        ) {
            val adapter = ShopGreenCategoriesGridAdapter(items, itemClickListener)
            val manager =
                GridLayoutManager(
                    itemBinding.root.context,
                    SPAN_NUMBER,
                    GridLayoutManager.VERTICAL,
                    false
                )
            itemBinding.rvCategoriesPage.layoutManager = manager
            itemBinding.rvCategoriesPage.adapter = adapter
            itemBinding.rvCategoriesPage.isNestedScrollingEnabled = false
        }
    }

}