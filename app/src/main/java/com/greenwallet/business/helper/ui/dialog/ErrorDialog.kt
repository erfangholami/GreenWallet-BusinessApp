package com.greenwallet.business.helper.ui.dialog

import android.app.Activity
import androidx.core.content.res.ResourcesCompat
import com.greenwallet.business.R

class ErrorDialog(activity: Activity) : BaseDialog(activity) {

    init {
        icon = ResourcesCompat.getDrawable(activity.resources, R.drawable.ic_explanation, null)
        buttonText = context.resources.getString(R.string.try_again)
        explanation = context.resources.getString(R.string.something_went_wrong)
        title = context.resources.getString(R.string.oops)
    }
}