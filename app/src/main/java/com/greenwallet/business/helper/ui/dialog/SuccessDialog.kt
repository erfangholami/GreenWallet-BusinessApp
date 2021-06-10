package com.greenwallet.business.helper.ui.dialog

import android.app.Activity
import androidx.core.content.res.ResourcesCompat
import com.greenwallet.business.R

class SuccessDialog(activity: Activity) : BaseDialog(activity) {

    init {
        icon = ResourcesCompat.getDrawable(activity.resources, R.drawable.ic_confirmation, null)
        buttonText = context.resources.getString(R.string.dismiss)
        explanation = "success"
        titleSize = activity.resources.getDimensionPixelSize(R.dimen.text_big).toFloat()
    }
}