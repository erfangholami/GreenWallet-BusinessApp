package com.greenwallet.business.helper.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner
import com.greenwallet.business.R

class AppSpinner : AppCompatSpinner {
    private var isOpen = false

    private var focusChangeListener: OnFocusChangeListener? = null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet?) : super(
        context, attrs
    )

    constructor(context: Context) : super(context)

    override fun setSelection(position: Int) {
        super.setSelection(position)
        widgetAdapter.onItemSelected(position)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        dropDownVerticalOffset = h - resources.getDimensionPixelSize(R.dimen.spinner_corner_radius)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private val widgetAdapter: SpinnerWidgetAdapter<*>
        get() = adapter as SpinnerWidgetAdapter<*>

    override fun performClick(): Boolean {
        isOpen = true
        notifyListener()
        return super.performClick()
    }

    private fun performClosedEvent() {
        isOpen = false
        notifyListener()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (isOpen && hasWindowFocus) {
            performClosedEvent()
        }
    }

    private fun notifyListener() {
        focusChangeListener?.onFocusChange(this, isOpen)
    }

    fun setOnOpenChangeListener(onFocusChangeListener: OnFocusChangeListener?) {
        this.focusChangeListener = onFocusChangeListener
    }
}