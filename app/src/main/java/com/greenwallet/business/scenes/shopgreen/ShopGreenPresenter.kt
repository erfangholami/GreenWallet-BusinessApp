package com.greenwallet.business.scenes.shopgreen

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import com.greenwallet.business.helper.keystore.CipherStorageFactory
import com.greenwallet.business.helper.keystore.KeystoreKeys
import com.greenwallet.business.helper.network.InteractorFactory
import com.greenwallet.business.helper.network.Subscriber
import com.greenwallet.business.helper.network.campaings.CampaignsResponse
import com.greenwallet.business.helper.network.campaings.response.CampaingsResponseModel
import com.greenwallet.business.helper.network.dealsNoDeals.CategoriesResponse
import com.greenwallet.business.helper.network.files.FileResponse
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.shopgreen.ui.ShopGreenView
import java.util.ArrayList

class ShopGreenPresenter(var context: Context) :
    BasePresenter<ShopGreenView, ShopGreenProcessHandler>(), ShopGreenView.Presenter {

    enum class State {
        SHOP_GREEN,
        LOADING,
        ERROR
    }

    var state: State = State.SHOP_GREEN
        set(value) {
            field = value
            activityHandler?.let {
                when (state) {
                    State.SHOP_GREEN -> it.showShopGreenScreen()
                    State.LOADING -> it.showLoadingScreen()
                    State.ERROR -> it.showErrorMessage()
                }
            }
        }

    var shopGreenView: ShopGreenView? = null

    var campaigns: ArrayList<Pair<CampaingsResponseModel, Bitmap?>> = ArrayList()

    var numberCampaigns = 0

    override fun onActivityHandlerSubscribed() {
        state = state

        requestCategories()
        requestCampaigns()
    }

    override fun onViewSubscribed(view: ShopGreenView) {
        shopGreenView = view

        state = State.SHOP_GREEN
    }

    private fun requestCategories() {
        state = State.LOADING

        val interactor = InteractorFactory(this.context).createDealsNoDealsInteractor()

        val cipherStorage = CipherStorageFactory.newInstance(context)
        val merchantId = if(!cipherStorage.decrypt(KeystoreKeys.merchantId.name).isNullOrEmpty()) cipherStorage.decrypt(KeystoreKeys.merchantId.name) else ""

        interactor.categories(
            merchantId = merchantId!!,
            listener = object :
                Subscriber<CategoriesResponse> {
                override fun onRequestSuccess(response: CategoriesResponse) {
                    if (response.response == CategoriesResponse.Result.SUCCESS) {
                        response.result?.forEach { i -> i.category?.let {
                            Log.e("Categories Request",
                                it
                            )
                        } }

                        state = State.SHOP_GREEN

                        shopGreenView?.showCategories(response.result?.map { i -> i.category } as ArrayList<String>)

                        Log.e("Categories Request", "Success")
                    } else {
                        state = State.ERROR

                        Log.e("Categories Request", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    state = State.ERROR

                    Log.e("Categories Request", "onRequestFailure")
                }
            })
    }

    private fun requestCampaigns() {
        state = State.LOADING

        val interactor = InteractorFactory(this.context).createCampaignsInteractor()

        interactor.campaigns(
            listener = object :
                Subscriber<CampaignsResponse> {
                override fun onRequestSuccess(response: CampaignsResponse) {
                    if (response.response == CampaignsResponse.Result.SUCCESS) {
                        //response.result?.forEach { i -> Log.e("Campaigns Request", i.name ) }

                        state = State.SHOP_GREEN

                        numberCampaigns = response.result!!.count()

                        for (index in 0 until response.result.count()) {
                            if (response.result[index].default_file_id != null) {
                                getImage(response.result[index].default_file_id!!, response.result[index])
                            } else {
                                campaigns.add((response.result[index] to null))

                                if(campaigns.count() == numberCampaigns) {
                                    shopGreenView?.showCampaigns(campaigns.toTypedArray())
                                }
                            }
                        }

                        Log.e("Campaigns Request", "Success")
                    } else {
                        state = State.ERROR

                        Log.e("Campaigns Request", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    state = State.ERROR

                    Log.e("Campaigns Request", "onRequestFailure")
                }
            })
    }

    private fun getImage(fileId: String, campaingsResponseModel: CampaingsResponseModel){
        val interactor = InteractorFactory(this.context).createFilesInteractor()

        interactor.file(
            id = fileId,
            listener = object :
                Subscriber<FileResponse> {
                override fun onRequestSuccess(response: FileResponse) {
                    if (response.response == FileResponse.Result.SUCCESS) {
                        campaigns.add((campaingsResponseModel to response.image))

                        if(campaigns.count() == numberCampaigns) {
                            shopGreenView?.showCampaigns(campaigns.toTypedArray())
                        }

                        Log.e("Files Request", "Success")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    campaigns.add((campaingsResponseModel to null))

                    if(campaigns.count() == numberCampaigns) {
                        shopGreenView?.showCampaigns(campaigns.toTypedArray())
                    }

                    Log.e("Files Request", "onRequestFailure")
                }
            })
    }

    override fun onLoginButtonClicked(email: String, password: String) {
        Log.e("ShopGreenPresenter", "email: $email, password: $password")
    }
}