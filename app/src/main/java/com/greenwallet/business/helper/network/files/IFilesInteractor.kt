package com.greenwallet.business.helper.network.files

import com.greenwallet.business.helper.network.Disposable
import com.greenwallet.business.helper.network.Subscriber

interface IFilesInteractor {

    fun file(
        id: String,
        listener: Subscriber<FileResponse>
    ): Disposable?
}