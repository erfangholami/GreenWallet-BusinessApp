package com.greenwallet.business.scenes.findMySize.ui

import com.greenwallet.business.helper.ui.ImageLoaderListener


interface FindMySizeView {

    interface Presenter {

        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: FindMySizeView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: FindMySizeView)

        fun fetchImage(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>?,
            reduceQuality: Boolean
        )

        fun onCloseFindMySize()

        fun getSizesFiles(): ArrayList<String>
    }
}