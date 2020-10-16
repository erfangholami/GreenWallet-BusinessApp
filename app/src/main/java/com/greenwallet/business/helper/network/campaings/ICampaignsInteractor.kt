package com.greenwallet.business.helper.network.campaings

import com.greenwallet.business.helper.network.Disposable
import com.greenwallet.business.helper.network.Subscriber

interface ICampaignsInteractor {

    fun campaigns(
        listener: Subscriber<CampaignsResponse>
    ): Disposable?
}