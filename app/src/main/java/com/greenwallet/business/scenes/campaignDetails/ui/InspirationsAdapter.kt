package com.greenwallet.business.scenes.campaignDetails.ui

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemCampaignInspirationBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.FileMode

class InspirationsAdapter : RecyclerView.Adapter<InspirationsAdapter.InspirationItemViewHolder>() {

    var items: Array<String> = arrayOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var mode = FileMode.ID

    lateinit var imageLoadListener: (id: String, listener: ImageLoaderListener, sizes: Pair<Int, Int>) -> (Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationItemViewHolder {
        val itemBinding =
            ItemCampaignInspirationBinding.inflate(LayoutInflater.from(parent.context))
        return InspirationItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: InspirationItemViewHolder, position: Int) {

        when (mode) {
            FileMode.URL -> {
                holder.loadImageUrl(items[position])
            }
            FileMode.ID -> {
                imageLoadListener.invoke(items[position], object : ImageLoaderListener {
                    override fun onFetchFinished(image: Bitmap?) {
                        holder.setImage(image)
                    }
                }, holder.getImageViewSizes())
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class InspirationItemViewHolder(val itemBinding: ItemCampaignInspirationBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun getImageViewSizes() = itemBinding.ivImage.layoutParams.run { width to height }

        fun setImage(image: Bitmap?) {
            itemBinding.ivImage.setImageBitmap(image)
        }

        fun loadImageUrl(imageUrl: String) {
            Glide.with(itemBinding.root)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_image_loading)
                .into(itemBinding.ivImage)
        }
    }
}