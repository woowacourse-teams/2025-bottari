package com.bottari.presentation.common.extension

import android.widget.EditText

fun EditText.setTextIfDifferent(newText: CharSequence?) {
    if (text.toString() == newText?.toString()) return
    setText(newText)
}
