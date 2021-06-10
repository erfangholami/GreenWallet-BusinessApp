package com.greenwallet.business.scenes.productFeatures.ui.fullscreen

import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.network.product.response.FileMode


interface FullScreenPhotoView {

    interface Presenter {
        /**
         * To be called when the View gets visible in onStart()
         */
        fun subscribeView(view: FullScreenPhotoView)

        /**
         * To be called when the View gets off screen in onStop()
         */
        fun disposeView(view: FullScreenPhotoView)

        fun getProductFiles(): Pair<FileMode, ArrayList<String>>

        fun onDismissFullScreenPhotoClicked()

        fun fetchImage(
            fileId: String,
            loaderListener: ImageLoaderListener,
            sizes: Pair<Int, Int>?,
            reduceQuality: Boolean
        )

        fun setCurrentPicturePosition(currentPagePosition: Int)

        fun getShowingImagePosition(): Int
    }
}