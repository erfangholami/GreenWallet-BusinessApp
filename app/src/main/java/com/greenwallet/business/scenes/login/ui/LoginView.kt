package com.greenwallet.business.scenes.login.ui

interface LoginView {

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: LoginView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: LoginView)

        fun onLoginButtonClicked(email: String, password: String)
    }
}