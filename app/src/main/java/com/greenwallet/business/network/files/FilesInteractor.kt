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
//                    val image = BitmapFactory.decodeStream(response.byteStream())

                    val result = FileResponse(null, FileResponse.Result.SUCCESS)

                    BitmapFactory.Options().run {

                        val res = response.bytes()

                        inJustDecodeBounds = true
                        BitmapFactory.decodeByteArray(res, 0, res.size, this)
//                        BitmapFactory.decodeStream(response.byteStream(), null, this)

                        //calculate sampling
                        if (sizes == null || sizes.first == 0 || sizes.second == 0) {
                            inSampleSize = if (reduceQuality) 4 else 1
                        } else {
                            val (height: Int, width: Int) = this.run { outHeight to outWidth }
                            var sampleSize = 1

                            val reqHeight = sizes.second
                            val reqWidth = sizes.first

                            if (height > reqHeight || width > reqWidth) {

                                val halfHeight: Int = height / 2
                                val halfWidth: Int = width / 2

                                while (((halfHeight / sampleSize) >= reqHeight) && ((halfWidth / sampleSize) >= reqWidth)) {
                                    sampleSize *= 2
                                }
                            }
                            inSampleSize = sampleSize
                        }

                        inJustDecodeBounds = false
//                        result.image = BitmapFactory.decodeStream(response.byteStream(), null, this)
                        result.image = BitmapFactory.decodeByteArray(res, 0, res.size, this)

                        listener.onRequestSuccess(result)

                        Log.e("Request", "onSuccess")
                    }




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