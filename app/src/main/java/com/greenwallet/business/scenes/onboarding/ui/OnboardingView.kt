package com.greenwallet.business.scenes.onboarding.ui

interface OnboardingView {

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: OnboardingView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: OnboardingView)

        fun onLoginButtonClicked()
    }
}