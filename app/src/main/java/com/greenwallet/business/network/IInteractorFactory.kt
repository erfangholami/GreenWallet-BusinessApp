package com.greenwallet.business.network

import com.greenwallet.business.network.campaings.ICampaignsInteractor
import com.greenwallet.business.network.product.ProductInteractor
import com.greenwallet.business.network.files.IFilesInteractor
import com.greenwallet.business.network.login.ILoginInteractor

interface IInteractorFactory {

    fun isApiReady(): Boolean

    fun createLoginInteractor(): ILoginInteractor
    fun createProductInteractor(): ProductInteractor
    fun createCampaignsInteractor(): ICampaignsInteractor
    fun createFilesInteractor(): IFilesInteractor
}