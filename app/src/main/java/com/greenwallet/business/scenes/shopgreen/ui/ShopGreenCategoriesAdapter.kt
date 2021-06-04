package com.greenwallet.business.scenes.shopgreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.R.drawable.*
import com.greenwallet.business.databinding.ShopgreenRvCategoriesItemBinding
import java.util.*

class ShopGreenCategoriesAdapter :
    RecyclerView.Adapter<ShopGreenCategoriesAdapter.ViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    var mModels: ArrayList<String> = arrayListOf()
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
        val value = mModels[position].lowercase(Locale.getDefault()).replace(" ", "")

        when {
            value == "accessories" -> {
                holder.setText("Accessories")
                holder.setImage(category_accessories_icon)
            }
            value == "apparel" -> {
                holder.setText("Apparel")
                holder.setImage(category_apparel_icon)
            }
            value.contains("ethical") && value.contains("fashion") -> {
                holder.setText("Ethical Fashion")
                holder.setImage(category_ethical_fashion_icon)
            }
            value.contains("health") && value.contains("beauty") -> {
                holder.setText("Health and Beauty")
                holder.setImage(category_health_and_beauty_icon)
            }
            value.contains("nutrition") -> {
                holder.setText("Nutrition")
                holder.setImage(category_nutrition_icon)
            }
            value == "organics" -> {
                holder.setText("Organics")
                holder.setImage(category_organics_icon)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(mModels[position])
        }
    }

    override fun getItemCount() = mModels.size

    class ViewHolder(private val itemBinding: ShopgreenRvCategoriesItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun setText(text: String) {
            itemBinding.tvTitle.text = text
        }

        fun setImage(imageIconId: Int) {
            itemBinding.ivIcon.setImageResource(imageIconId)
        }
    }

}