package com.greenwallet.business.scenes.shopgreen.ui

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.R
import com.greenwallet.business.network.campaings.response.CampaingsResponseModel
import kotlinx.android.synthetic.main.shopgreen_rv_campaigns_item.view.*

class ShopGreenCampaignsAdapter(var mModels: Array<Pair<CampaingsResponseModel, Bitmap?>>, var context: Context) :
    RecyclerView.Adapter<ShopGreenCampaignsAdapter.ViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    class ViewHolder(val layout: LinearLayout) :
        RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val constraintLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopgreen_rv_campaigns_item, parent, false) as LinearLayout

        return ViewHolder(
            constraintLayout
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.layout.tv_description_campaign.text = mModels[position].first.name
        holder.layout.iv_icon_campaign.setImageBitmap(mModels[position].second)

        //holder.itemView.setOnClickListener {
        //    onItemClick?.invoke(mModels[position])
        //}
    }

    override fun getItemCount(): Int = mModels.size
}