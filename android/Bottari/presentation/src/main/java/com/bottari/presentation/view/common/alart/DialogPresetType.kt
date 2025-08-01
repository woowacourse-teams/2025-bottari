package com.bottari.presentation.view.common.alart

import com.bottari.presentation.R

enum class DialogPresetType {
    EXIT_WITHOUT_SAVE,
    ;

    fun applyTo(dialog: CustomAlertDialog) =
        with(dialog) {
            binding.tvDialogCustomTitle.setText(R.string.common_alert_dialog_title_text)
            binding.tvDialogCustomDescription.setText(R.string.common_alert_unsaved_dialog_message_text)
            setPositiveButton(
                textRes = R.string.common_yes_btn_text,
                textColorRes = R.color.white,
                backgroundColorRes = R.color.primary,
            ) { listener?.onClickPositive() }

            setNegativeButton(
                textRes = R.string.common_no_btn_text,
                textColorRes = R.color.gray_787878,
                backgroundColorRes = R.color.white,
            ) { listener?.onClickNegative() }

            setCloseButton()
        }
}
