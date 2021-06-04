package com.greenwallet.business.network.files

import android.graphics.Bitmap
import com.greenwallet.business.network.ResponseError

class FileResponse(val image: Bitmap?, val response : Result, val error: ResponseError = ResponseError.NONE) {

    enum class Result {
        ERROR,
        SUCCESS
    }
}