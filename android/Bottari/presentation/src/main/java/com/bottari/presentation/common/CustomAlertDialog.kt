package com.bottari.presentation.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.annotation.StringRes
import androidx.core.graphics.drawable.toDrawable
import com.bottari.presentation.databinding.DialogCustomBinding

class CustomAlertDialog private constructor(
    private val context: Context,
) {
    private val dialog: Dialog = Dialog(context)
    private val binding: DialogCustomBinding =
        DialogCustomBinding.inflate(LayoutInflater.from(context))

    private var onPositiveClick: (() -> Unit)? = null
    private var onNegativeClick: (() -> Unit)? = null
    private var onCloseClick: (() -> Unit)? = null

    init {
        setupUI()
        setupListener()
    }

    private fun setupUI() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
    }

    private fun setupListener() {
        binding.btnDialogCustomYes.setOnClickListener {
            onPositiveClick?.invoke()
            dialog.dismiss()
        }

        binding.btnDialogCustomNo.setOnClickListener {
            onNegativeClick?.invoke()
            dialog.dismiss()
        }

        binding.btnDialogCustomClose.setOnClickListener {
            onCloseClick?.invoke()
            dialog.dismiss()
        }
    }

    fun setTitleText(
        @StringRes resId: Int,
    ): CustomAlertDialog {
        binding.tvDialogCustomTitle.setText(resId)
        return this
    }

    fun setSubTitleText(
        @StringRes resId: Int,
    ): CustomAlertDialog {
        binding.tvDialogCustomDescription.setText(resId)
        return this
    }

    fun setPositiveButton(
        @StringRes resId: Int,
        visible: Boolean = true,
        onClick: (() -> Unit)? = null,
    ): CustomAlertDialog {
        binding.btnDialogCustomYes.apply {
            setText(resId)
            visibility = if (visible) View.VISIBLE else View.GONE
        }
        onPositiveClick = onClick
        return this
    }

    fun setNegativeButton(
        @StringRes resId: Int,
        visible: Boolean = true,
        onClick: (() -> Unit)? = null,
    ): CustomAlertDialog {
        binding.btnDialogCustomNo.apply {
            setText(resId)
            visibility = if (visible) View.VISIBLE else View.GONE
        }
        onNegativeClick = onClick
        return this
    }

    fun setOnCloseClick(onClick: () -> Unit): CustomAlertDialog {
        onCloseClick = onClick
        return this
    }

    fun setAccessibilityDescriptions(
        @StringRes titleDescResId: Int? = null,
        @StringRes subTitleDescResId: Int? = null,
        @StringRes positiveBtnDescResId: Int? = null,
        @StringRes negativeBtnDescResId: Int? = null,
    ): CustomAlertDialog {
        titleDescResId?.let { binding.tvDialogCustomTitle.contentDescription = context.getString(it) }
        subTitleDescResId?.let { binding.tvDialogCustomDescription.contentDescription = context.getString(it) }
        positiveBtnDescResId?.let { binding.btnDialogCustomYes.contentDescription = context.getString(it) }
        negativeBtnDescResId?.let { binding.btnDialogCustomNo.contentDescription = context.getString(it) }
        return this
    }

    fun show() {
        dialog.show()
        dialog.window?.let { window ->
            window.setLayout(
                (context.resources.displayMetrics.widthPixels * 0.8).toInt(),
                window.attributes.height,
            )
            window.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }
    }

    fun dismiss() {
        dialog.dismiss()
    }

    companion object {
        fun create(context: Context): CustomAlertDialog = CustomAlertDialog(context)
    }
}
