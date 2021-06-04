package com.greenwallet.business.helper.ui

import android.graphics.Bitmap

interface ImageLoaderListener {
    fun onFetchFinished(image: Bitmap?)
    fun onFetchFailed() {}
}