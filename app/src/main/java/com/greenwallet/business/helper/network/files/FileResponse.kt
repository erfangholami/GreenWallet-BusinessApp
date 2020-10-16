package com.greenwallet.business.helper.network.files

import android.graphics.Bitmap
import android.media.Image
import com.greenwallet.business.helper.network.ResponseError
import com.greenwallet.business.helper.network.campaings.response.CampaingsResponseModel
import com.greenwallet.business.helper.network.dealsNoDeals.response.CategoriesResponseModel
import com.greenwallet.business.helper.network.files.response.FilesResponseModel

class FileResponse(val image: Bitmap?, val response : Result, val error: ResponseError = ResponseError.NONE) {

    enum class Result {
        ERROR,
        SUCCESS
    }
}