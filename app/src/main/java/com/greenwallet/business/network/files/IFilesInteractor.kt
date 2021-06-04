package com.greenwallet.business.network.files

import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.Subscriber

interface IFilesInteractor {

    fun file(
        id: String,
        listener: Subscriber<FileResponse>
    ): Disposable?
}