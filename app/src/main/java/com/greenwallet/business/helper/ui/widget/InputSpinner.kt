package com.greenwallet.business.helper.ui.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.view.isVisible
import com.greenwallet.business.R

open class InputSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : InputBase(context, attrs, defStyleAttr, R.layout.content_input_spinner) {

    private var appSpinner: AppSpinner = findViewById(R.id.input_spinner)

    private var arrowIcon: ImageView = findViewById(R.id.iv_arrow)

    private var isDropDownEnabled = true

    private var listener: ((item: Any) -> Unit)? = null

    init {
        appSpinner.setOnOpenChangeListener { _: View, hasFocus: Boolean ->
            isSelected = hasFocus
            if (isSelected) {
                hideKeyboard()
            }
            animateArrow(hasFocus)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setDropDownEnabled(isDropDownEnabled)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        appSpinner.isEnabled = enabled
    }

    open fun setDropDownEnabled(enabled: Boolean) {
        isDropDownEnabled = enabled
        appSpinner.isVisible = enabled
    }

    open fun <T> setAdapter(adapter: SpinnerWidgetAdapter<T>) {
        appSpinner.isEnabled = adapter.count > 0
        adapter.setDataSizeListener {

        }
        adapter.setDataSizeListener { size: Int ->
            if (isEnabled) {
                appSpinner.isEnabled = size > 0
            } else {
                appSpinner.isEnabled = false
            }
        }
        adapter.setOnItemSelectedListener { item: SpinnerWidgetAdapter.SpinnerItem? ->
            valueView.text = item?.name
            if (adapter.selected != null) {
                listener?.invoke(adapter.selected as Any)
            }
        }
        appSpinner.adapter = adapter
    }

    open fun setOnItemSelectedListener(listener: ((item: Any) -> Unit)) {
        this.listener = listener
    }

    open fun hideKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }

    private fun animateArrow(shouldRotateUp: Boolean) {
        val start = if (shouldRotateUp) 0 else 10000
        val end = if (shouldRotateUp) 10000 else 0
        val animator = ObjectAnimator.ofInt(arrowIcon.drawable, "level", start, end)
        animator.start()
    }
}
