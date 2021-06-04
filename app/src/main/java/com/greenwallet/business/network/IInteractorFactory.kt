package com.greenwallet.business.network

import com.greenwallet.business.network.campaings.ICampaignsInteractor
import com.greenwallet.business.network.dealsNoDeals.IDealsNoDealsInteractor
import com.greenwallet.business.network.files.IFilesInteractor
import com.greenwallet.business.network.login.ILoginInteractor

interface IInteractorFactory {

    fun isApiReady(): Boolean

    fun createLoginInteractor(): ILoginInteractor
    fun createDealsNoDealsInteractor(): IDealsNoDealsInteractor
    fun createCampaignsInteractor(): ICampaignsInteractor
    fun createFilesInteractor(): IFilesInteractor
}