package com.greenwallet.business.scenes.productFeatures.ui.aboutThisItem

interface ProductFeaturesAboutItemView {

    interface Presenter {
        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: ProductFeaturesAboutItemView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: ProductFeaturesAboutItemView)

        fun onDismissAboutItemClicked()
    }
}