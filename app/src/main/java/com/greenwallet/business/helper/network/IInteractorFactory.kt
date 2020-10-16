package com.greenwallet.business.helper.network

import com.greenwallet.business.helper.network.campaings.ICampaignsInteractor
import com.greenwallet.business.helper.network.dealsNoDeals.IDealsNoDealsInteractor
import com.greenwallet.business.helper.network.files.IFilesInteractor
import com.greenwallet.business.helper.network.login.ILoginInteractor

interface IInteractorFactory {

    fun isApiReady(): Boolean

    fun createLoginInteractor(): ILoginInteractor
    fun createDealsNoDealsInteractor(): IDealsNoDealsInteractor
    fun createCampaignsInteractor(): ICampaignsInteractor
    fun createFilesInteractor(): IFilesInteractor
}