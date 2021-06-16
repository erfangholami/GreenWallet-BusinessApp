package com.greenwallet.business.scenes.productFeatures.ui.details

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import com.greenwallet.business.R

class CircleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var circleRadius = 0
    private var strokeColor = 0
    private var strokeWidth = 0
    private var fillColor = 0
    private var circleGap = 0
    private var isOuterCircleVisible = false

    init {
        attrs?.let {
            val aTypedArray =
                context.obtainStyledAttributes(it, R.styleable.CircleView, defStyleAttr, 0)
            strokeColor = aTypedArray.getColor(R.styleable.CircleView_strokeColor, strokeColor)
            strokeWidth =
                aTypedArray.getDimensionPixelSize(R.styleable.CircleView_strokeWidth, strokeWidth)
            fillColor = aTypedArray.getColor(
                R.styleable.CircleView_fillColor,
                resources.getColor(R.color.colorAccent)
            )
            circleRadius =
                aTypedArray.getDimensionPixelSize(R.styleable.CircleView_circleRadius, circleRadius)
            circleGap =
                aTypedArray.getDimensionPixelSize(R.styleable.CircleView_circleGap, circleGap)
            aTypedArray.recycle()
        }
        init()
    }

    private fun init() {
        this.minimumHeight = circleRadius * 2 + strokeWidth
        this.minimumWidth = circleRadius * 2 + strokeWidth
        this.isSaveEnabled = true
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = this.width
        val h = this.height
        val ox = w / 2
        val oy = h / 2
        if (isOuterCircleVisible) {
            canvas.drawCircle(ox.toFloat(), oy.toFloat(), circleRadius.toFloat(), strokePaint)
        }
        canvas.drawCircle(ox.toFloat(), oy.toFloat(), circleRadius - circleGap.toFloat(), fillPaint)
    }

    private val strokePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = strokeWidth
        color = strokeColor
        style = Paint.Style.STROKE
    }

    private val fillPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = fillColor
        style = Paint.Style.FILL
    }

    fun setFillColor(@ColorInt color: Int) {
        fillColor = color
        fillPaint.color = color
        invalidate()
    }

    fun setFillColor(color: String) {
        try {
            fillColor = Color.parseColor(color)
            fillPaint.color = fillColor
            invalidate()
        } catch (exception: IllegalArgumentException) {
            Log.d("CircleView", "$color is not a color string.")
        }
    }

    fun setStrokeColor(@ColorInt color: Int) {
        strokeColor = color
        strokePaint.color = color
        invalidate()
    }

    fun setStrokeColor(color: String) {
        try {
            strokeColor = Color.parseColor(color)
            strokePaint.color = strokeColor
            invalidate()
        } catch (exception: IllegalArgumentException) {
            Log.d("CircleView", "$color is not a color string.")
        }
    }

    fun showOuterCircle(showOuterCircle: Boolean) {
        if (isOuterCircleVisible != showOuterCircle) {
            isOuterCircleVisible = showOuterCircle
            invalidate()
        }
    }

    fun isOuterCircleVisible(): Boolean {
        return isOuterCircleVisible
    }
}