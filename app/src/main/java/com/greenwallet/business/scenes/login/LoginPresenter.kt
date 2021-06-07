package com.greenwallet.business.scenes.login

import android.content.Context
import android.util.Log
import com.greenwallet.business.helper.keystore.User
import com.greenwallet.business.network.InteractorFactory
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.login.LoginResponse
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.login.ui.LoginView

class LoginPresenter(context: Context) :
    BasePresenter<LoginView, LoginProcessHandler>(context), LoginView.Presenter {

    enum class State {
        LOGIN,
        SHOP_GREEN,
        LOADING,
        ERROR
    }

    var state: State = State.LOGIN
        set(value) {
            field = value
            activityHandler?.let {
                when (state) {
                    State.LOGIN -> it.showLoginScreen()
                    State.SHOP_GREEN -> it.showShopGreenScreen()
                    State.LOADING -> it.showLoadingScreen()
                    State.ERROR -> it.showErrorMessage()
                }
            }
        }

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun onViewSubscribed(view: LoginView) {
        state = State.LOGIN
    }

    override fun onLoginButtonClicked(email: String, password: String) {
        Log.e("LoginPresenter", "email: $email, password: $password")

        state = State.LOADING

        val interactor = InteractorFactory(this.context).createLoginInteractor()

        interactor.login(
            email = email,
            password = password,
            listener = object :
                Subscriber<LoginResponse> {
                override fun onRequestSuccess(response: LoginResponse) {
                    if (response.response == LoginResponse.Result.SUCCESS) {
                        User.shared.merchantId = response.merchant_id

                        state = State.SHOP_GREEN

                        Log.e("Login Request", "Success")
                    } else {
                        state = State.ERROR

                        Log.e("Login Request", "Error")
                    }
                }

                override fun onRequestFailure(t: Throwable) {
                    state = State.ERROR

                    Log.e("Login Request", "onRequestFailure")
                }

                override fun onUserUnauthorized() {
                    activityHandler?.userShouldReAuthenticate()
                }
            })
    }
}