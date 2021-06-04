package com.greenwallet.business.network.files

import android.graphics.BitmapFactory
import android.util.Log
import com.greenwallet.business.network.Disposable
import com.greenwallet.business.network.RequestSubscriber
import com.greenwallet.business.network.ResponseError
import com.greenwallet.business.network.Subscriber
import com.greenwallet.business.network.restapi.IRestApi
import okhttp3.ResponseBody

open class FilesInteractor(private var api: IRestApi?) : IFilesInteractor {

    override fun file(
        fileId: String,
        listener: Subscriber<FileResponse>,
        sizes: Pair<Int, Int>?,
        reduceQuality: Boolean
    ): Disposable? {
        return api?.file(fileId)
            ?.execute(object : RequestSubscriber<ResponseBody>() {
                override fun onUnprocessableEntity() {
                    val result = FileResponse(null, FileResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onUnprocessableEntity")
                }

                override fun onSuccess(response: ResponseBody) {
                    val image = BitmapFactory.decodeStream(response.byteStream())

                    val result = FileResponse(image, FileResponse.Result.SUCCESS)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onSuccess")
                }

                override fun onUnauthorizedError() {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                    Log.e("Request", "onUnauthorizedError")
                }

                override fun onServerError() {
                    listener.onRequestSuccess(
                        FileResponse(
                            null,
                            FileResponse.Result.ERROR,
                            ResponseError.ERROR_SERVER_500
                        )
                    )

                    Log.e("Request", "onServerError")
                }

                override fun onExpectedError(response: String) {
                    val result = FileResponse(null, FileResponse.Result.ERROR)

                    listener.onRequestSuccess(result)

                    Log.e("Request", "onExpectedError")
                }

                override fun onUnexpectedError(t: Throwable) {
                    listener.onRequestFailure(t)

                    Log.e("Request", "onUnexpectedError")
                }
            })
    }
}