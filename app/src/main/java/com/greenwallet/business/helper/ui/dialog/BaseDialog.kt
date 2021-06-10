package com.greenwallet.business.helper.ui.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.greenwallet.business.R
import com.greenwallet.business.databinding.DialogBaseBinding

open class BaseDialog(val activity: Activity) : AppCompatDialog(activity) {

    var buttonListener: ((BaseDialog) -> (Unit))? = null
    var icon: Drawable? = null

    lateinit var buttonText: String
    lateinit var title: String

    var hasCloseIcon = false

    var titleSize: Float = activity.resources.getDimensionPixelSize(R.dimen.text_large_xx).toFloat()
    lateinit var explanation: String

    lateinit var binding: DialogBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogBaseBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.ibClose.setOnClickListener {
            dismiss()
        }

        if (hasCloseIcon) {
            binding.ibClose.visibility = VISIBLE
        } else {
            binding.ibClose.visibility = INVISIBLE
        }

        binding.ivIcon.setImageDrawable(icon)

        binding.tvTitle.text = title
        binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)

        val button = findViewById<ConstraintLayout>(R.id.btn_action)
            ?.findViewById<Button>(R.id.btn_default)

        button?.text = buttonText

        button?.setOnClickListener {
            buttonListener?.invoke(this)
        }

        binding.tvExplanation.text = explanation
    }
}