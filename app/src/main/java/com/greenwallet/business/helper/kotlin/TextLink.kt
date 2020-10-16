package com.greenwallet.business.helper.kotlin

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.TextView

fun makeLinks(textView: TextView, links: Array<String>, clickableSpans: Array<ClickableSpan>) {
    val spannableString = SpannableString(textView.text)

    for (i in links.indices) {
        val clickableSpan = clickableSpans[i]
        val link = links[i]

        val startIndexOfLink = textView.text.indexOf(link)

        spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    textView.movementMethod = LinkMovementMethod.getInstance()
    textView.setText(spannableString, TextView.BufferType.SPANNABLE)
}