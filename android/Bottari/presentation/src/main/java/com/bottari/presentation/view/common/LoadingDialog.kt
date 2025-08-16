package com.bottari.presentation.view.common

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import com.bottari.presentation.R

class LoadingDialog(
    context: Context,
) : Dialog(context) {
    init {
        window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            setDimAmount(0f)
        }

        setContentView(R.layout.dialog_loading)
        setCancelable(false)
    }
}
