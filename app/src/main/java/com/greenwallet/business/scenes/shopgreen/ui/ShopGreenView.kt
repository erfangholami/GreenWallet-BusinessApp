package com.greenwallet.business.scenes.shopgreen.ui

import android.graphics.Bitmap
import android.media.Image
import com.greenwallet.business.helper.network.campaings.response.CampaingsResponseModel
import java.util.ArrayList

interface ShopGreenView {

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: ShopGreenView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: ShopGreenView)

        fun onLoginButtonClicked(email: String, password: String)
    }

    fun showCategories(categories: ArrayList<String>)

    fun showCampaigns(campaigns: Array<Pair<CampaingsResponseModel, Bitmap?>>)
}