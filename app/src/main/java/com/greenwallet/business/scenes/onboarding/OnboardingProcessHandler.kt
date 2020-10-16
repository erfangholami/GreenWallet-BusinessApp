package com.greenwallet.business.scenes.onboarding

interface OnboardingProcessHandler {

    fun showOnboardingScreen()

    fun showLoginScreen()

    fun showLoadingScreen()

    fun showErrorMessage()
}