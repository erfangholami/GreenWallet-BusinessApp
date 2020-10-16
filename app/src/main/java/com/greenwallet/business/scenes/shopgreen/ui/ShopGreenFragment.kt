package com.greenwallet.business.scenes.shopgreen.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.R
import com.greenwallet.business.helper.network.campaings.response.CampaingsResponseModel
import kotlinx.android.synthetic.main.fragment_shop_green.*
import java.util.ArrayList

class ShopGreenFragment : Fragment(), ShopGreenView {

    private lateinit var presenter: ShopGreenView.Presenter

    private var mAdapterCategories: ShopGreenCategoriesAdapter? = null

    private var mAdapterCampaigns: ShopGreenCampaignsAdapter? = null

    private var currentContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_shop_green, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)
    }

    override fun onStop() {
        presenter.disposeView(this)
        super.onStop()
    }

    private fun setup() {
    }

    override fun showCategories(categories: ArrayList<String>) {
        mAdapterCategories = ShopGreenCategoriesAdapter(categories, context!!)

        rv_categories.layoutManager = GridLayoutManager(context, 3)
        rv_categories.adapter = mAdapterCategories

        mAdapterCategories?.onItemClick = { it ->
            Log.e("Value", ": $it")

            //presenter.onEyeDropSelected(it)
        }
    }

    override fun showCampaigns(campaigns: Array<Pair<CampaingsResponseModel, Bitmap?>>) {
        mAdapterCampaigns = ShopGreenCampaignsAdapter(campaigns, context!!)

        rv_campaigns.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_campaigns.adapter = mAdapterCampaigns

        rv_campaigns.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State,
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildAdapterPosition(view) > 0) {
                    outRect.left = 88
                }
            }
        })

        mAdapterCampaigns?.onItemClick = { it ->
            Log.e("Value", ": $it")

            //presenter.onEyeDropSelected(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ShopGreenPresenterProvider) {
            this.presenter = context.getShopGreenPresenter()
        } else {
            throw RuntimeException("$context must implement ShopGreenPresenterProvider")
        }

        currentContext = context
    }

    interface ShopGreenPresenterProvider {
        fun getShopGreenPresenter(): ShopGreenView.Presenter
    }
}
