package com.greenwallet.business.scenes.shopgreen.ui

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.databinding.ShopgreenRvCampaignsItemBinding
import com.greenwallet.business.helper.network.campaings.response.CampaingsResponseModel

class ShopGreenCampaignsAdapter(var mModels: Array<Pair<CampaingsResponseModel, Bitmap?>>) :
    RecyclerView.Adapter<ShopGreenCampaignsAdapter.ViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding = ShopgreenRvCampaignsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDescription(mModels[position].first.name)
        holder.setImageBitmap(mModels[position].second)

        //holder.itemView.setOnClickListener {
        //    onItemClick?.invoke(mModels[position])
        //}
    }

    override fun getItemCount(): Int = mModels.size

    class ViewHolder(private val itemBinding:  ShopgreenRvCampaignsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun setDescription(description: String?) {
            itemBinding.tvDescriptionCampaign.text = description
        }

        fun setImageBitmap(bitmap: Bitmap?) {
            itemBinding.ivIconCampaign.setImageBitmap(bitmap)
        }
    }
}