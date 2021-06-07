package com.greenwallet.business.scenes.shopgreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.databinding.ItemHomeOnboardingBinding

class OnboardingAdapter(val items: List<HomeOnboardingItem>) :
    RecyclerView.Adapter<OnboardingAdapter.HomeOnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOnboardingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemHomeOnboardingBinding.inflate(layoutInflater, parent, false)
        return HomeOnboardingViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HomeOnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class HomeOnboardingViewHolder(private val itemBinding: ItemHomeOnboardingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: HomeOnboardingItem) {
            itemBinding.ivOnboardingIllustration.setImageResource(item.image)
            itemBinding.tvOnboardingTitle.setText(item.title)
            itemBinding.tvOnboardingContent.setText(item.message)
        }
    }
}

data class HomeOnboardingItem(val image: Int, val title: Int, val message: Int)