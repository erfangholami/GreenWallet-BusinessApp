package com.greenwallet.business.scenes.shopgreen.ui

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ShopgreenRvCampaignsItemBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel

class ShopGreenCampaignsAdapter :
    RecyclerView.Adapter<ShopGreenCampaignsAdapter.ViewHolder>() {

    lateinit var itemClickListener: ((CampaignsResponseModel) -> Unit)
    lateinit var imageLoaderListener: (id: String, listener: ImageLoaderListener, sizes: Pair<Int, Int>) -> (Unit)

    var items: ArrayList<CampaignsResponseModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding = ShopgreenRvCampaignsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items[position]
        holder.bind(model, itemClickListener)

        if (!model.defaultFileUrl.isNullOrEmpty()) {
            holder.loadImageUrl(model.defaultFileUrl)
        } else if (!model.defaultFileId.isNullOrEmpty()) {
            imageLoaderListener.invoke(model.defaultFileId, object : ImageLoaderListener {
                override fun onFetchFinished(image: Bitmap?) {
                    holder.setImageBitmap(image)
                }
            }, holder.getImageViewSizes())
        }

    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val itemBinding: ShopgreenRvCampaignsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            model: CampaignsResponseModel,
            itemClickListener: (CampaignsResponseModel) -> Unit
        ) {
            itemBinding.root.setOnClickListener {
                itemClickListener.invoke(model)
            }
            itemBinding.tvDescriptionCampaign.text = model.name
        }

        fun setImageBitmap(bitmap: Bitmap?) {
            itemBinding.ivIconCampaign.setImageBitmap(bitmap)
        }

        fun loadImageUrl(imageUrl: String) {
            Glide.with(itemBinding.root)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_loading)
                .into(itemBinding.ivIconCampaign)
        }

        fun getImageViewSizes() =
            itemBinding.ivIconCampaign.layoutParams.run { width to height }
    }
}