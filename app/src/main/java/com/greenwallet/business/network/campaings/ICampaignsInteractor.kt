package com.greenwallet.business.network.campaings

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber

interface ICampaignsInteractor {

    fun campaigns(
        listener: Subscriber<CampaignsResponse>
    ): Disposable?
}