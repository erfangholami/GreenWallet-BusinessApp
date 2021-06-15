package com.greenwallet.business.scenes.campaignList

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel

interface ExploreCampaignsView {

    interface Presenter {
        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: ExploreCampaignsView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: ExploreCampaignsView)

        fun onBackButtonClicked()

        fun getCampaigns(): ArrayList<CampaignsResponseModel>

        fun onCampaignClicked(campaignsResponseModel: CampaignsResponseModel)

        fun fetchImage(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>?,
            reduceQuality: Boolean
        )
    }
}