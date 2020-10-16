package com.greenwallet.business.helper.network

import android.content.Context
import com.greenwallet.business.helper.network.campaings.CampaignsInteractor
import com.greenwallet.business.helper.network.campaings.ICampaignsInteractor
import com.greenwallet.business.helper.network.dealsNoDeals.DealsNoDealsInteractor
import com.greenwallet.business.helper.network.dealsNoDeals.IDealsNoDealsInteractor
import com.greenwallet.business.helper.network.files.FilesInteractor
import com.greenwallet.business.helper.network.files.IFilesInteractor
import com.greenwallet.business.helper.network.login.ILoginInteractor
import com.greenwallet.business.helper.network.login.LoginInteractor

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