package com.greenwallet.business.scenes.onboarding

import com.greenwallet.business.scenes.base.BaseProcessHandler

interface OnboardingProcessHandler: BaseProcessHandler {

    fun showOnboardingScreen()

    fun showLoginScreen()

    fun showLoadingScreen()

    fun showErrorMessage()
}