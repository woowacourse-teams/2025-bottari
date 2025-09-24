package com.bottari.presentation.view.edit.personal.main

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.collectWithLifecycle
import com.bottari.presentation.common.extension.formatWithPattern
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentPersonalBottariEditBinding
import com.bottari.presentation.model.alarm.AlarmTypeUiModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.util.PermissionUtil
import com.bottari.presentation.util.PermissionUtil.requiredPermissions
import com.bottari.presentation.view.common.alert.CustomAlertDialog
import com.bottari.presentation.view.common.alert.DialogListener
import com.bottari.presentation.view.common.alert.DialogPresetType
import com.bottari.presentation.view.edit.alarm.AlarmEditFragment
import com.bottari.presentation.view.edit.personal.item.PersonalItemEditFragment
import com.bottari.presentation.view.edit.personal.main.adapter.PersonalBottariEditItemAdapter
import com.bottari.presentation.view.edit.personal.main.rename.BottariRenameDialog
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.time.format.TextStyle
import java.util.Locale

class PersonalBottariEditFragment : BaseFragment<FragmentPersonalBottariEditBinding>(FragmentPersonalBottariEditBinding::inflate) {
    private val viewModel: PersonalBottariEditViewModel by viewModels {
        val bottariId = requireArguments().getLong(ARG_BOTTARI_ID)
        PersonalBottariEditViewModel.Factory(bottariId)
    }

    private lateinit var popupMenu: PopupMenu
    private val itemAdapter by lazy { PersonalBottariEditItemAdapter() }
    private val permissionLauncher = registerPermissionLauncher()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchBottari()
    }

    private fun setupObserver() {
        collectWithLifecycle(viewModel.uiState) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            renderTitle(uiState.bottariTitle)
            renderItems(uiState)
            renderAlarm(uiState)
        }
        collectWithLifecycle(viewModel.uiEvent) { uiEvent ->
            when (uiEvent) {
                PersonalBottariEditUiEvent.FetchBottariFailure ->
                    showSnackbar(R.string.bottari_edit_fetch_failure_text)

                PersonalBottariEditUiEvent.CreateTemplateFailure ->
                    showSnackbar(R.string.bottari_edit_create_template_failure_text)

                PersonalBottariEditUiEvent.CreateTemplateSuccess ->
                    showSnackbar(R.string.bottari_edit_create_template_success_text)

                is PersonalBottariEditUiEvent.ToggleAlarmStateFailure ->
                    showSnackbar(R.string.bottari_edit_toggle_alarm_state_failure_text)
            }
        }
    }

    private fun setupUI() {
        setupPopupMenu()
        setupItemRecyclerView()
    }

    private fun setupListener() {
        binding.btnOption.setOnClickListener { popupMenu.show() }
        binding.btnPrevious.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        binding.viewPersonalItemEdit.btnRoot.setOnClickListener {
            viewModel.uiState.value.run {
                navigateToScreen(
                    PersonalItemEditFragment::class.java,
                    PersonalItemEditFragment.newBundle(
                        bottariId,
                        bottariTitle,
                    ),
                )
            }
        }

        binding.viewAlarmEdit.btnRoot.setOnClickListener {
            if (PermissionUtil.hasAllRuntimePermissions(requireContext())) {
                return@setOnClickListener checkAndRequestSpecialPermission()
            }
            permissionLauncher.launch(requiredPermissions)
        }

        binding.viewAlarmEdit.switchAlarmEdit.setOnClickListener {
            viewModel.updateAlarmState()
        }

        parentFragmentManager.setFragmentResultListener(
            BottariRenameDialog.SAVE_BOTTARI_TITLE_RESULT_KEY,
            viewLifecycleOwner,
        ) { _, _ -> viewModel.fetchBottari() }
    }

    private fun renderTitle(title: String) {
        binding.tvBottariTitle.text = title
    }

    private fun renderItems(uiState: PersonalBottariEditUiState) {
        itemAdapter.submitList(uiState.items)
        binding.viewPersonalItemEdit.apply {
            tvItemEditTitle.text = getString(R.string.bottari_edit_personal_items_title_text)
            viewItemEditEmpty.root.isVisible = uiState.isEmpty
            tvItemEditDescription.isVisible = uiState.isEmpty.not()
        }
    }

    private fun renderAlarm(uiState: PersonalBottariEditUiState) {
        binding.viewAlarmEdit.apply {
            switchAlarmEdit.isChecked = uiState.isAlarmActive
            viewAlarmEditEmpty.root.isVisible = uiState.isShowAlarmCreate
            groupAlarmItem.isVisible = uiState.isShowAlarm
            tvAlarmEditDescription.isVisible = uiState.isShowAlarm
            tvAlarmTime.text =
                uiState.alarm?.time?.formatWithPattern(getString(R.string.common_format_time_alarm))
            tvAlarmType.text = uiState.alarm?.let { formatAlarmTypeText(it) }
        }
    }

    private fun registerPermissionLauncher() =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            when {
                allGranted -> checkAndRequestSpecialPermission()
                PermissionUtil.isPermanentlyDenied(this) -> showSettingsDialog()
                else -> showSnackbar(R.string.common_permission_failure_text)
            }
        }

    private fun checkAndRequestSpecialPermission() {
        if (PermissionUtil.hasExactAlarmPermission(requireContext())) {
            return navigateToAlarmEditScreen()
        }
        showExactAlarmSettingsDialog()
    }

    private fun navigateToAlarmEditScreen() {
        viewModel.uiState.value.run {
            navigateToScreen(
                AlarmEditFragment::class.java,
                AlarmEditFragment.newBundle(bottariId, bottariTitle),
            )
        }
    }

    private fun navigateToScreen(
        fragmentClass: Class<out Fragment>,
        bundle: Bundle? = null,
    ) {
        parentFragmentManager.commit {
            replace(R.id.fcv_personal_edit, fragmentClass, bundle)
            addToBackStack(fragmentClass.simpleName)
        }
    }

    private fun setupPopupMenu() {
        val contextWrapper = ContextThemeWrapper(requireContext(), R.style.CustomPopupMenuText)
        popupMenu =
            PopupMenu(
                contextWrapper,
                binding.btnOption,
                Gravity.CENTER,
                0,
                R.style.CustomPopupMenu,
            ).apply {
                menuInflater.inflate(R.menu.personal_bottari_edit_popup_menu, menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_template -> viewModel.createBottariTemplate().let { true }
                        R.id.action_rename -> showRenameDialog().let { true }
                        else -> false
                    }
                }
            }
    }

    private fun setupItemRecyclerView() {
        binding.viewPersonalItemEdit.rvItemEdit.adapter = itemAdapter
        binding.viewPersonalItemEdit.rvItemEdit.layoutManager =
            FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }
    }

    private fun showSettingsDialog() {
        CustomAlertDialog
            .newInstance(DialogPresetType.NAVIGATE_TO_NOTIFICATION_SETTINGS)
            .setDialogListener(
                object : DialogListener {
                    override fun onClickNegative() {}

                    override fun onClickPositive() = PermissionUtil.openAppSettings(requireContext())
                },
            ).show(parentFragmentManager, DialogPresetType.NAVIGATE_TO_NOTIFICATION_SETTINGS.name)
    }

    private fun showExactAlarmSettingsDialog() {
        CustomAlertDialog
            .newInstance(DialogPresetType.NAVIGATE_TO_ALARM_SETTINGS)
            .setDialogListener(
                object : DialogListener {
                    override fun onClickNegative() {}

                    override fun onClickPositive() = PermissionUtil.requestExactAlarmPermission(requireContext())
                },
            ).show(parentFragmentManager, DialogPresetType.NAVIGATE_TO_ALARM_SETTINGS.name)
    }

    private fun showRenameDialog() {
        viewModel.uiState.value.run {
            BottariRenameDialog
                .newInstance(bottariId, bottariTitle)
                .show(parentFragmentManager, BottariRenameDialog::class.java.name)
        }
    }

    private fun formatAlarmTypeText(alarm: AlarmUiModel): String =
        when (alarm.type) {
            AlarmTypeUiModel.NON_REPEAT -> alarm.date.formatWithPattern(getString(R.string.common_format_date_alarm))
            AlarmTypeUiModel.REPEAT ->
                if (alarm.isRepeatEveryDay) {
                    getString(R.string.bottari_item_alarm_repeat_everyday_text)
                } else {
                    formatEveryWeek(alarm)
                }
        }

    private fun formatEveryWeek(alarm: AlarmUiModel): String {
        val checkedDays = alarm.repeatDays.filter { it.isChecked }
        return buildString {
            append(getString(R.string.bottari_item_alarm_repeat_everyweek_text))
            append(getString(R.string.common_separator_text))
            append(
                checkedDays.joinToString { checkedDay ->
                    checkedDay.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault(),
                    )
                },
            )
        }
    }

    private fun showSnackbar(messageRes: Int) {
        requireView().showSnackbar(messageRes)
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        fun newBundle(bottariId: Long) =
            Bundle().apply {
                putLong(ARG_BOTTARI_ID, bottariId)
            }
    }
}
