package com.greenwallet.business.scenes.campaignDetails.ui

import com.greenwallet.business.helper.ui.ImageLoaderListener

interface CampaignDetailsView {

    interface Presenter {
        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: CampaignDetailsView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: CampaignDetailsView)

        fun onCloseButtonClicked()

        fun fetchImage(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>?,
            reduceQuality: Boolean
        )

        fun onTagClicked(tag: String)

        fun onLinkInstagramButtonClicked()
    }
}