package com.greenwallet.business.scenes.campaignDetails

import com.greenwallet.business.scenes.base.BaseProcessHandler

interface CampaignDetailsProcessHandler : BaseProcessHandler {

    fun showCampaignDetailsScreen()

    fun showLinkInstagramScreen()

    fun dismiss()

    fun showLoadingScreen()

    fun showErrorMessage()

    fun onTagClicked(tag: String)
}