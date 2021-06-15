package com.greenwallet.business.scenes.campaignList

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemCampaignListBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel

class ExploreCampaignListAdapter :
    RecyclerView.Adapter<ExploreCampaignListAdapter.CampaignViewHolder>() {

    lateinit var itemClickListener: (CampaignsResponseModel) -> (Unit)
    lateinit var imageLoaderListener: (id: String, listener: ImageLoaderListener, sizes: Pair<Int, Int>) -> (Unit)
    var campaignList: ArrayList<CampaignsResponseModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemCampaignListBinding.inflate(layoutInflater, parent, false)
        return CampaignViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {

        if (!campaignList[position].defaultFileUrl.isNullOrEmpty()) {
            holder.loadImageUrl(campaignList[position].defaultFileUrl!!)
        } else if (!campaignList[position].defaultFileId.isNullOrEmpty()) {
            imageLoaderListener.invoke(campaignList[position].defaultFileId!!, object : ImageLoaderListener {
                override fun onFetchFinished(image: Bitmap?) {
                    holder.setImage(image)
                }
            }, holder.getImageViewSizes())
        }
        holder.bind(campaignList[position], itemClickListener)
    }

    override fun getItemCount(): Int = campaignList.size

    class CampaignViewHolder(private val itemBinding: ItemCampaignListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            item: CampaignsResponseModel,
            itemClickListener: (CampaignsResponseModel) -> Unit
        ) {
            itemBinding.root.setOnClickListener {
                itemClickListener.invoke(item)
            }
            itemBinding.tvCampaignTitle.text = item.name
//            Glide.with(itemBinding.root.context)
//                .load(item.websiteUrl)
//                .placeholder(R.drawable.image_hot_deals)
//                .into(itemBinding.ivCampaignsImage)
            //todo(set other features)
        }

        fun getImageViewSizes() = itemBinding.ivCampaignsImage.layoutParams.run { width to height }

        fun setImage(image: Bitmap?) {
            itemBinding.ivCampaignsImage.setImageBitmap(image)
        }

        fun loadImageUrl(imageUrl: String) {
            Glide.with(itemBinding.root)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_loading)
                .into(itemBinding.ivCampaignsImage)
        }
    }
}