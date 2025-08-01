package com.bottari.presentation.common.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.bottari.presentation.R
import com.bottari.presentation.databinding.DialogCustomBinding

class CustomAlertDialog : DialogFragment() {
    private var _binding: DialogCustomBinding? = null
    val binding get() = _binding!!

    private var listener: DialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogCustomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val type = requireArguments().getString(ARG_DIALOG_TYPE) ?: return
        setupUI(type)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val width = (requireContext().resources.displayMetrics.widthPixels * 0.8).toInt()
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            window.setLayout(width, height)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setDialogListener(listener: DialogListener): CustomAlertDialog {
        this.listener = listener
        return this
    }

    private fun setupUI(type: String) {
        when (DialogPresetType.valueOf(type)) {
            DialogPresetType.EXIT_WITHOUT_SAVE -> setupExitWithoutSave()
        }
    }

    private fun setupExitWithoutSave() {
        binding.tvDialogCustomTitle.setText(R.string.common_alert_dialog_title_text)
        binding.tvDialogCustomDescription.setText(R.string.common_alert_unsaved_dialog_message_text)
        setPositiveBtn(
            nameId = R.string.common_yes_btn_text,
            backgroundColor = R.color.primary,
            textColor = R.color.white,
        ) { listener?.onClickPositive() }
        setNegativeBtn(
            nameId = R.string.common_no_btn_text,
            backgroundColor = R.color.white,
            textColor = R.color.gray_787878,
        ) { listener?.onClickNegative() }
        setCloseBtn()
    }

    private fun setPositiveBtn(
        @StringRes nameId: Int? = null,
        @ColorRes textColor: Int? = null,
        @ColorRes backgroundColor: Int? = null,
        onClick: (() -> Unit) = {},
    ) {
        setupButton(
            binding.btnDialogCustomPositive,
            nameId,
            textColor,
            backgroundColor ?: R.color.primary,
            onClick,
        )
    }

    private fun setNegativeBtn(
        @StringRes nameId: Int? = null,
        @ColorRes textColor: Int? = null,
        @ColorRes backgroundColor: Int? = null,
        onClick: (() -> Unit) = {},
    ) {
        setupButton(
            binding.btnDialogCustomNegative,
            nameId,
            textColor,
            backgroundColor ?: R.color.white,
            onClick,
        )
    }

    private fun setupButton(
        button: View,
        @StringRes nameId: Int?,
        @ColorRes textColorRes: Int?,
        @ColorRes backgroundColorRes: Int?,
        onClick: () -> Unit,
    ) {
        if (nameId == null) {
            button.isVisible = false
            return
        }

        if (button is android.widget.TextView) {
            button.apply {
                setText(nameId)
                textColorRes?.let {
                    setTextColor(ContextCompat.getColor(context, it))
                }
                backgroundTintList =
                    ContextCompat.getColorStateList(
                        context,
                        backgroundColorRes ?: R.color.primary,
                    )
                setOnClickListener {
                    onClick()
                    dismiss()
                }
            }
        }
    }

    private fun setCloseBtn() {
        binding.btnDialogCustomClose.setOnClickListener { dismiss() }
    }

    companion object {
        private const val ARG_DIALOG_TYPE = "ARG_DIALOG_TYPE"

        fun newInstance(dialogPresetType: DialogPresetType): CustomAlertDialog =
            CustomAlertDialog().apply {
                arguments =
                    Bundle().apply {
                        putString(ARG_DIALOG_TYPE, dialogPresetType.name)
                    }
            }
    }
}
