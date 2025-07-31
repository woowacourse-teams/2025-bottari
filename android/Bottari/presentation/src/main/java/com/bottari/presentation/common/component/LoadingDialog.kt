package com.bottari.presentation.common.component

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bottari.presentation.R

class LoadingDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)

            window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                setDimAmount(0f)
            }
        }
}
