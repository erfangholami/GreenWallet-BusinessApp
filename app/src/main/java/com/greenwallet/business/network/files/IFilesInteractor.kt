package com.greenwallet.business.network.files

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber

interface IFilesInteractor {

    fun file(
        fileId: String,
        listener: Subscriber<FileResponse>,
        sizes: Pair<Int, Int>? = null,
        reduceQuality: Boolean = true
    ): Disposable?
}