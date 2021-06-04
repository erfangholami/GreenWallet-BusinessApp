package com.greenwallet.business.network

import android.content.Context
import com.greenwallet.business.network.campaings.CampaignsInteractor
import com.greenwallet.business.network.campaings.ICampaignsInteractor
import com.greenwallet.business.network.dealsNoDeals.DealsNoDealsInteractor
import com.greenwallet.business.network.dealsNoDeals.IDealsNoDealsInteractor
import com.greenwallet.business.network.files.FilesInteractor
import com.greenwallet.business.network.files.IFilesInteractor
import com.greenwallet.business.network.login.ILoginInteractor
import com.greenwallet.business.network.login.LoginInteractor

class InteractorFactory(val context: Context) : IInteractorFactory {

    override fun isApiReady(): Boolean {
        return RetrofitFactory.getInstance().isOpenApiReady()
    }

    override fun createLoginInteractor(): ILoginInteractor {
        return LoginInteractor(RetrofitFactory.getInstance().getUnauthenticatedApi())
    }

    override fun createDealsNoDealsInteractor(): IDealsNoDealsInteractor {
        return DealsNoDealsInteractor(RetrofitFactory.getInstance().getUnauthenticatedApi())
    }

    override fun createCampaignsInteractor(): ICampaignsInteractor {
        return CampaignsInteractor(RetrofitFactory.getInstance().getUnauthenticatedApi())
    }

    override fun createFilesInteractor(): IFilesInteractor {
        return FilesInteractor(RetrofitFactory.getInstance().getUnauthenticatedApi())
    }
}