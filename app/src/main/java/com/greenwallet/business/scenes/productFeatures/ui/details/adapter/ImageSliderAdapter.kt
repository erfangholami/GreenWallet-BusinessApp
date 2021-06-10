package com.greenwallet.business.scenes.productFeatures.ui.details.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemProductDetailsImageBinding
import com.greenwallet.business.databinding.ItemProductDetailsImageZoomBinding
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.FileMode
import com.smarteist.autoimageslider.SliderViewAdapter

class ImageSliderAdapter : SliderViewAdapter<ImageSliderAdapter.ImageViewHolder>() {
    var hasZoom = false

    lateinit var itemClickListener: () -> (Unit)
    lateinit var imageListener: (id: String, listener: ImageLoaderListener) -> (Unit)

    var items: ArrayList<String> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var mode = FileMode.ID

    override fun getCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?): ImageViewHolder {
        val view =
            if (hasZoom) {
                ItemProductDetailsImageZoomBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
                    .root
            } else {
                ItemProductDetailsImageBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
                    .root
            }
        return ImageViewHolder(view, hasZoom)
    }

    override fun onBindViewHolder(viewHolder: ImageViewHolder?, position: Int) {

        when (mode) {
            FileMode.URL -> {
                viewHolder?.loadImageUrl(items[position])
            }
            FileMode.ID -> {
                imageListener.invoke(items[position], object : ImageLoaderListener {
                    override fun onFetchFinished(image: Bitmap?) {
                        viewHolder?.setImage(image)
                    }
                })
            }
        }

        if (this::itemClickListener.isInitialized) {
            viewHolder?.setItemClickListener(itemClickListener)
        }
    }

    class ImageViewHolder(itemView: View, private val hasZoom: Boolean) :
        SliderViewAdapter.ViewHolder(itemView) {

        fun setImage(image: Bitmap?) {
            if (hasZoom) {
                (itemView as PhotoView).setImageBitmap(image)
            } else {
                (itemView as AppCompatImageView).setImageBitmap(image)
            }
        }

        fun loadImageUrl(imageUrl: String) {

            if (hasZoom) {
                Glide.with(itemView)
                    .load(imageUrl)
                    .placeholder(R.drawable.bg_image_loading)
                    .into(itemView as PhotoView)
            } else {
                Glide.with(itemView)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.bg_image_loading)
                    .into(itemView as AppCompatImageView)
            }

        }

        fun setItemClickListener(itemClickListener: () -> Unit) {
            itemView.setOnClickListener {
                itemClickListener.invoke()
            }
        }
    }
}