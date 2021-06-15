package com.greenwallet.business.scenes.campaignDetails

import android.content.Context
import com.greenwallet.business.network.InteractorFactory
import com.greenwallet.business.network.campaings.response.CampaignsResponseModel
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.campaignDetails.ui.CampaignDetailsView

class CampaignDetailsPresenter(context: Context) :
    BasePresenter<CampaignDetailsView, CampaignDetailsProcessHandler>(context),
    CampaignDetailsView.Presenter {

    enum class State {
        CAMPAIGN_DETAILS,
        LINK_INSTAGRAM,
        DISMISS,
        LOADING,
        ERROR
    }

    var state: State = State.CAMPAIGN_DETAILS
        set(value) {
            field = value
            activityHandler?.let {
                when (state) {
                    State.CAMPAIGN_DETAILS -> it.showCampaignDetailsScreen()
                    State.LINK_INSTAGRAM -> it.showLinkInstagramScreen()
                    State.DISMISS -> it.dismiss()
                    State.LOADING -> it.showLoadingScreen()
                    State.ERROR -> it.showErrorMessage()
                }
            }
        }

    private val productInteractor = InteractorFactory(context).createProductInteractor()

    lateinit var campaignDetails: CampaignsResponseModel

    override fun onViewSubscribed(view: CampaignDetailsView) {
        state = State.CAMPAIGN_DETAILS
    }

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun onCloseButtonClicked() {
        state = State.DISMISS
    }

    override fun onTagClicked(tag: String) {
        activityHandler?.onTagClicked(tag)
    }

    override fun onLinkInstagramButtonClicked() {
        state = State.LINK_INSTAGRAM
    }
}