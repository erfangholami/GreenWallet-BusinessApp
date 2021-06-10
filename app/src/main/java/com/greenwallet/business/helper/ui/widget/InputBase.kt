package com.greenwallet.business.helper.ui.widget

import android.content.Context
import android.os.Parcelable
import android.text.InputFilter
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.greenwallet.business.R
import java.util.*

open class InputBase @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    layoutId: Int = R.layout.content_input_spinner,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var isViewEnabled = true

    protected var valueView: TextView

    protected var maxSize = 0

    init {
        View.inflate(context, layoutId, this)
        isClickable = true
        isFocusable = true

        valueView = findViewById(R.id.input_value)

        setOnClickListener { valueView.requestFocus() }
        applyAttrs(attrs, defStyleAttr)
    }

    private fun applyAttrs(
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        if (attrs != null) {
            val values = context.obtainStyledAttributes(
                attrs,
                R.styleable.InputBase,
                defStyleAttr,
                0
            )
            try {
                valueView.hint = values.getString(R.styleable.InputBase_input_hint)
                isViewEnabled = values.getBoolean(
                    R.styleable.InputBase_android_enabled, true
                )
                maxSize = values.getInt(R.styleable.InputBase_max_size, Int.MAX_VALUE)

                val filters: MutableList<InputFilter> = ArrayList()
                if (values.hasValue(R.styleable.InputBase_max_size)) {
                    filters.add(InputFilter.LengthFilter(maxSize))
                }
                if (values.hasValue(R.styleable.InputBase_upper_case)) {
                    filters.add(InputFilter.AllCaps())
                }
                valueView.filters = filters.toTypedArray()

                val hint = values.getString(R.styleable.InputBase_hint)
                if (hint != null) valueView.hint = hint
            } finally {
                values.recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        isEnabled = isViewEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isViewEnabled = enabled
        valueView.isEnabled = enabled
    }

    fun getValue(): String {
        return valueView.text.toString()
    }

    open fun setValue(value: String?) {
        if (valueView.text.toString() != value) {
            valueView.text = value
        }
    }

    open fun setValueHint(hint: String?) {
        valueView.hint = hint
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        valueView.layoutDirection = layoutDirection
    }
}
