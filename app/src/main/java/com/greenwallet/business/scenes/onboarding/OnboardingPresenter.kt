package com.greenwallet.business.scenes.onboarding

import android.content.Context
import com.greenwallet.business.scenes.base.BasePresenter
import com.greenwallet.business.scenes.onboarding.ui.OnboardingView

class OnboardingPresenter(context: Context) :
    BasePresenter<OnboardingView, OnboardingProcessHandler>(context), OnboardingView.Presenter {

    enum class State {
        ONBOARDING,
        LOGIN,
        LOADING,
        ERROR
    }

    var state: State = State.ONBOARDING
        set(value) {
            field = value
            activityHandler?.let {
                when (state) {
                    State.ONBOARDING -> it.showOnboardingScreen()
                    State.LOGIN -> it.showLoginScreen()
                    State.LOADING -> it.showLoadingScreen()
                    State.ERROR -> it.showErrorMessage()
                }
            }
        }

    override fun onViewSubscribed(view: OnboardingView) {
        state = State.ONBOARDING
    }

    override fun onActivityHandlerSubscribed() {
        state = state
    }

    override fun onLoginButtonClicked() {
        state = State.LOGIN
    }
}