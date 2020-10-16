package com.greenwallet.business.scenes.shopgreen.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.R
import com.greenwallet.business.R.drawable.*
import kotlinx.android.synthetic.main.shopgreen_rv_categories_item.view.*
import java.util.*

class ShopGreenCategoriesAdapter(var mModels: ArrayList<String>, var context: Context) :
    RecyclerView.Adapter<ShopGreenCategoriesAdapter.ViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    class ViewHolder(val layout: LinearLayout) :
        RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val constraintLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopgreen_rv_categories_item, parent, false) as LinearLayout

        return ViewHolder(
            constraintLayout
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = mModels[position].toLowerCase().replace(" ", "")

        when {
            value == "accessories" -> {
                holder.layout.tv_title.text = "Accessories"
                holder.layout.iv_icon.setImageResource(category_accessories_icon)
            }
            value == "apparel" -> {
                holder.layout.tv_title.text = "Apparel"
                holder.layout.iv_icon.setImageResource(category_apparel_icon)
            }
            value.contains("ethical") && value.contains("fashion") -> {
                holder.layout.tv_title.text = "Ethical Fashion"
                holder.layout.iv_icon.setImageResource(category_ethical_fashion_icon)
            }
            value.contains("health") && value.contains("beauty") -> {
                holder.layout.tv_title.text = "Health and Beauty"
                holder.layout.iv_icon.setImageResource(category_health_and_beauty_icon)
            }
            value.contains("nutrition") -> {
                holder.layout.tv_title.text = "Nutrition"
                holder.layout.iv_icon.setImageResource(category_nutrition_icon)
            }
            value == "organics" -> {
                holder.layout.tv_title.text = "Organics"
                holder.layout.iv_icon.setImageResource(category_organics_icon)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(mModels[position])
        }
    }

    override fun getItemCount() = mModels.size
}