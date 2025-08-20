package com.bottari.presentation.view.common.alert

import com.bottari.presentation.R

enum class DialogPresetType {
    EXIT_WITHOUT_SAVE,
    NAVIGATE_TO_NOTIFICATION_SETTINGS,
    NAVIGATE_TO_ALARM_SETTINGS,
    RESET_BOTTARI_ITEMS_CHECK_STATE,
    FORCE_UPDATE,
    ;

    fun applyTo(dialog: CustomAlertDialog) =
        when (this) {
            EXIT_WITHOUT_SAVE -> applyExitWithoutSave(dialog)
            NAVIGATE_TO_NOTIFICATION_SETTINGS -> applyNavigateToNotificationSettings(dialog)
            NAVIGATE_TO_ALARM_SETTINGS -> applyNavigateToAlarmSettings(dialog)
            RESET_BOTTARI_ITEMS_CHECK_STATE -> applyResetBottariItemsCheckState(dialog)
            FORCE_UPDATE -> applyForceUpdate(dialog)
        }

    private fun applyExitWithoutSave(dialog: CustomAlertDialog) {
        with(dialog) {
            binding.tvDialogCustomTitle.setText(R.string.common_alert_dialog_title_text)
            binding.tvDialogCustomDescription.setText(R.string.common_alert_unsaved_dialog_message_text)
            setPositiveButton(
                textRes = R.string.common_yes_btn_text,
                textColorRes = R.color.white,
                backgroundColorRes = R.color.primary,
            )
            setNegativeButton(
                textRes = R.string.common_no_btn_text,
                textColorRes = R.color.gray_787878,
                backgroundColorRes = R.color.white,
            )
            setCloseButton()
        }
    }

    private fun applyNavigateToNotificationSettings(dialog: CustomAlertDialog) {
        with(dialog) {
            binding.tvDialogCustomTitle.setText(R.string.common_permission_dialog_title_text)
            binding.tvDialogCustomDescription.setText(R.string.common_notification_permission_dialog_message_text)
            setPositiveButton(
                textRes = R.string.common_permission_dialog_positive_btn_text,
                textColorRes = R.color.white,
                backgroundColorRes = R.color.primary,
            )
            setNegativeButton(
                textRes = R.string.common_permission_dialog_negative_btn_text,
                textColorRes = R.color.gray_787878,
                backgroundColorRes = R.color.white,
            )
            setCloseButton()
        }
    }

    private fun applyNavigateToAlarmSettings(dialog: CustomAlertDialog) {
        with(dialog) {
            binding.tvDialogCustomTitle.setText(R.string.common_permission_dialog_title_text)
            binding.tvDialogCustomDescription.setText(R.string.common_alarm_permission_dialog_message_text)
            setPositiveButton(
                textRes = R.string.common_permission_dialog_positive_btn_text,
                textColorRes = R.color.white,
                backgroundColorRes = R.color.primary,
            )
            setNegativeButton(
                textRes = R.string.common_permission_dialog_negative_btn_text,
                textColorRes = R.color.gray_787878,
                backgroundColorRes = R.color.white,
            )
            setCloseButton()
        }
    }

    private fun applyResetBottariItemsCheckState(dialog: CustomAlertDialog) {
        with(dialog) {
            binding.tvDialogCustomTitle.setText(R.string.reset_bottari_items_check_state_dialog_title_text)
            binding.tvDialogCustomDescription.setText(R.string.reset_bottari_items_check_state_dialog_description_text)
            setPositiveButton(
                textRes = R.string.common_yes_btn_text,
                textColorRes = R.color.white,
                backgroundColorRes = R.color.primary,
            )
            setNegativeButton(
                textRes = R.string.common_no_btn_text,
                textColorRes = R.color.gray_787878,
                backgroundColorRes = R.color.white,
            )
        }
    }

    private fun applyForceUpdate(dialog: CustomAlertDialog) {
        with(dialog) {
            binding.tvDialogCustomTitle.setText(R.string.force_update_dialog_title_text)
            binding.tvDialogCustomDescription.setText(R.string.force_update_dialog_description_text)
            isCancelable = false
            requireDialog().setCanceledOnTouchOutside(false)
            setPositiveButton(
                textRes = R.string.force_update_dialog_update_btn_text,
                textColorRes = R.color.white,
                backgroundColorRes = R.color.primary,
            )
            setNegativeButton(
                textRes = R.string.force_update_dialog_close_btn_text,
                textColorRes = R.color.gray_787878,
                backgroundColorRes = R.color.white,
            )
        }
    }
}
