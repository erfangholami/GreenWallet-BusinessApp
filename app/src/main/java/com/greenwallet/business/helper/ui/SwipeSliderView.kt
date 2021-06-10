package com.greenwallet.business.helper.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.smarteist.autoimageslider.SliderView

class SwipeSliderView : SliderView {

    companion object {
        private const val SWIPE_SENSITIVTTY_HIGH = 400
        private const val SWIPE_SENSITIVITY_MEDIUM = 600
    }

    lateinit var swipeDownListener: () -> (Unit)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var x1 = 3000f
    var y1 = 3000f

    var x2 = 0f
    var y2 = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = ev.x
                y1 = ev.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = ev.x
                y2 = ev.y
                if (y2 - SWIPE_SENSITIVITY_MEDIUM > y1) {
                    //swipe down
                    swipeDownListener.invoke()
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}