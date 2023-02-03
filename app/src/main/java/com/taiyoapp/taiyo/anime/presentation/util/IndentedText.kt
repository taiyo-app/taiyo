package com.taiyoapp.taiyo.anime.presentation.util

import android.text.SpannableString
import android.text.style.LeadingMarginSpan

fun indentedText(
    text: String,
    marginFirstLine: Int,
    marginNextLines: Int,
): SpannableString {
    val result = SpannableString(text)
    result.setSpan(LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),
        0,
        text.length,
        0)
    return result
}