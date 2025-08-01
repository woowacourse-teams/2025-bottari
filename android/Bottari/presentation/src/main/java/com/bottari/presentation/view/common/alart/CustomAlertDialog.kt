package com.bottari.presentation.view.common.alart

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.bottari.presentation.databinding.DialogCustomBinding
import com.bottari.presentation.view.common.safeArgument

class CustomAlertDialog : DialogFragment() {
    private var _binding: DialogCustomBinding? = null
    val binding get() = requireNotNull(_binding)

    var listener: DialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
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

        val typeStr = safeArgument { getString(ARG_DIALOG_TYPE) }
        val preset = runCatching { DialogPresetType.valueOf(typeStr ?: "") }.getOrNull()

        if (preset == null) {
            dismiss()
            return
        }

        preset.applyTo(this)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (requireContext().resources.displayMetrics.widthPixels * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listener = null
    }

    fun setDialogListener(listener: DialogListener): CustomAlertDialog {
        this.listener = listener
        return this
    }

    fun setPositiveButton(
        @StringRes textRes: Int,
        @ColorRes textColorRes: Int,
        @ColorRes backgroundColorRes: Int,
        onClick: () -> Unit,
    ) {
        setupButton(
            binding.btnDialogCustomPositive,
            textRes,
            textColorRes,
            backgroundColorRes,
            onClick,
        )
    }

    fun setNegativeButton(
        @StringRes textRes: Int,
        @ColorRes textColorRes: Int,
        @ColorRes backgroundColorRes: Int,
        onClick: () -> Unit,
    ) {
        setupButton(
            binding.btnDialogCustomNegative,
            textRes,
            textColorRes,
            backgroundColorRes,
            onClick,
        )
    }

    fun setCloseButton() {
        binding.btnDialogCustomClose.setOnClickListener { dismiss() }
    }

    private fun setupButton(
        button: View,
        @StringRes textRes: Int?,
        @ColorRes textColorRes: Int?,
        @ColorRes backgroundColorRes: Int?,
        onClick: () -> Unit,
    ) {
        if (textRes == null) {
            button.isVisible = false
            return
        }

        (button as? TextView)?.apply {
            setText(textRes)
            textColorRes?.let { setTextColor(ContextCompat.getColor(context, it)) }
            backgroundTintList =
                backgroundColorRes?.let {
                    ContextCompat.getColorStateList(context, it)
                }
            setOnClickListener {
                onClick()
                dismiss()
            }
        }
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
