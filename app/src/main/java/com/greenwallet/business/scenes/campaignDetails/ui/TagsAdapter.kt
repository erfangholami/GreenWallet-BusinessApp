package com.greenwallet.business.scenes.campaignDetails.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.databinding.ItemCampaignTagBinding

class TagsAdapter(val context: Context, var items: Array<String>) :
    RecyclerView.Adapter<TagsAdapter.TagViewHolder>() {

    lateinit var itemClickListener: (tag: String) -> (Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val itemBinding = ItemCampaignTagBinding.inflate(LayoutInflater.from(context))
        return TagViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    class TagViewHolder(val itemBinding: ItemCampaignTagBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(tag: String, itemClickListener: (tag: String) -> Unit) {
            itemBinding.tvTag.text = tag
            itemBinding.root.setOnClickListener {
                itemClickListener.invoke(tag)
            }
        }
    }
}