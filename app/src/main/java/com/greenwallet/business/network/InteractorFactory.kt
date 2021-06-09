package com.greenwallet.business.network

import android.content.Context
import com.greenwallet.business.network.campaings.CampaignsInteractor
import com.greenwallet.business.network.campaings.ICampaignsInteractor
import com.greenwallet.business.network.product.ProductInteractor
import com.greenwallet.business.network.files.FilesInteractor
import com.greenwallet.business.network.files.IFilesInteractor
import com.greenwallet.business.network.login.ILoginInteractor
import com.greenwallet.business.network.login.LoginInteractor
import com.greenwallet.business.network.productReviews.IProductReviewInteractor
import com.greenwallet.business.network.productReviews.ProductReviewInteractor
import okhttp3.logging.HttpLoggingInterceptor

class InteractorFactory(val context: Context) : IInteractorFactory {

    override fun isApiReady(): Boolean {
        return RetrofitFactory.getInstance().isOpenApiReady()
    }

    override fun createLoginInteractor(): ILoginInteractor {
        return LoginInteractor(RetrofitFactory.getInstance().getUnauthenticatedApi())
    }

    override fun createProductInteractor(): ProductInteractor {
        return ProductInteractor(RetrofitFactory.getInstance().getUnauthenticatedApi())
    }

    override fun createCampaignsInteractor(): ICampaignsInteractor {
        return CampaignsInteractor(RetrofitFactory.getInstance().getUnauthenticatedApi())
    }

    override fun createFilesInteractor(): IFilesInteractor {
        return FilesInteractor(RetrofitFactory.getInstance().getUnauthenticatedApi())
    }

    override fun createProductReviewInteractor(): IProductReviewInteractor {
        return ProductReviewInteractor(RetrofitFactory.getInstance().getUnauthenticatedApi())
    }
}