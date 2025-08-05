package com.bottari.presentation.view.edit.personal.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.databinding.FragmentPersonalBottariEditBinding
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.util.PermissionUtil
import com.bottari.presentation.util.PermissionUtil.requiredPermissions
import com.bottari.presentation.view.edit.alarm.AlarmEditFragment
import com.bottari.presentation.view.edit.personal.item.PersonalItemEditFragment
import com.bottari.presentation.view.edit.personal.main.adapter.PersonalBottariEditAlarmAdapter
import com.bottari.presentation.view.edit.personal.main.adapter.PersonalBottariEditItemAdapter
import com.bottari.presentation.view.edit.personal.main.rename.BottariRenameDialog
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class PersonalBottariEditFragment : BaseFragment<FragmentPersonalBottariEditBinding>(FragmentPersonalBottariEditBinding::inflate) {
    private val viewModel: PersonalBottariEditViewModel by viewModels {
        val bottariId = requireArguments().getLong(ARG_BOTTARI_ID)
        PersonalBottariEditViewModel.Factory(requireContext().getSSAID(), bottariId)
    }
    private val popupMenu: PopupMenu by lazy { createPopupMenu() }
    private val itemAdapter: PersonalBottariEditItemAdapter by lazy { PersonalBottariEditItemAdapter() }
    private val alarmAdapter: PersonalBottariEditAlarmAdapter by lazy { PersonalBottariEditAlarmAdapter() }
    private val permissionLauncher = getPermissionLauncher()

    private fun getPermissionLauncher() =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                checkAndRequestSpecialPermission()
            } else {
                if (PermissionUtil.isPermanentlyDenied(this)) {
                    showSettingsDialog()
                } else {
                    showSnackbar(R.string.common_permission_failure_text)
                }
            }
        }

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
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            setupTitle(uiState.title)
            setupItems(uiState.items)
            setupAlarm(uiState.alarm)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                PersonalBottariEditUiEvent.FetchBottariFailure -> showSnackbar(R.string.bottari_edit_fetch_failure_text)
                PersonalBottariEditUiEvent.CreateTemplateFailure -> showSnackbar(R.string.bottari_edit_create_template_failure_text)
                PersonalBottariEditUiEvent.CreateTemplateSuccess -> showSnackbar(R.string.bottari_edit_create_template_success_text)
                is PersonalBottariEditUiEvent.ToggleAlarmStateFailure -> showSnackbar(R.string.bottari_edit_toggle_alarm_state_failure_text)
            }
        }
    }

    private fun setupUI() {
        setupPopupMenu()
        setupItemRecyclerView()
        setupAlarmRecyclerView()
    }

    private fun setupListener() {
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_template -> {
                    viewModel.createBottariTemplate()
                    true
                }

                R.id.action_rename -> {
                    showRenameDialog()
                    true
                }

                else -> false
            }
        }
        binding.btnOption.setOnClickListener {
            popupMenu.show()
        }
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.clEditItem.setOnClickListener {
            val uiState = viewModel.uiState.value ?: return@setOnClickListener
            navigateToScreen(
                PersonalItemEditFragment::class.java,
                PersonalItemEditFragment.newBundle(uiState.id, uiState.title, uiState.items),
            )
        }
        binding.clEditAlarm.setOnClickListener {
            if (PermissionUtil.hasAllRuntimePermissions(requireContext())) {
                checkAndRequestSpecialPermission()
                return@setOnClickListener
            }
            permissionLauncher.launch(requiredPermissions)
        }
        binding.switchAlarm.setOnClickListener {
            viewModel.updateAlarmState()
        }
        parentFragmentManager.setFragmentResultListener(
            BottariRenameDialog.SAVE_BOTTARI_TITLE_RESULT_KEY,
            viewLifecycleOwner,
        ) { _, _ ->
            viewModel.fetchBottari()
        }
    }

    private fun setupPopupMenu() {
        popupMenu.menuInflater.inflate(R.menu.personal_bottari_edit_popup_menu, popupMenu.menu)
    }

    private fun createPopupMenu(): PopupMenu {
        val contextWrapper = ContextThemeWrapper(requireContext(), R.style.CustomPopupMenuText)
        return PopupMenu(
            contextWrapper,
            binding.btnOption,
            Gravity.CENTER,
            0,
            R.style.CustomPopupMenu,
        )
    }

    private fun setupTitle(title: String) {
        binding.tvBottariTitle.text = title
    }

    private fun setupItems(items: List<BottariItemUiModel>) {
        itemAdapter.submitList(items)
        toggleItemSection(items.isNotEmpty())
    }

    private fun setupAlarm(alarm: AlarmUiModel?) {
        val isAlarmExist = alarm != null
        toggleAlarmSelection(isAlarmExist)
        binding.switchAlarm.isChecked = alarm?.isActive ?: false
        if (isAlarmExist.not()) return
        alarmAdapter.submitList(listOf(alarm))
    }

    private fun toggleItemSection(hasItems: Boolean) {
        binding.tvClickEditItemTitle.isVisible = !hasItems
        binding.tvClickEditItemDescription.isVisible = !hasItems
        binding.viewClickEditItem.isVisible = !hasItems
        binding.tvClickEditItemDescriptionNotEmpty.isVisible = hasItems
        binding.rvEditItem.isVisible = hasItems
    }

    private fun toggleAlarmSelection(hasAlarms: Boolean) {
        binding.tvClickEditAlarmTitle.isVisible = !hasAlarms
        binding.tvClickEditAlarmDescription.isVisible = !hasAlarms
        binding.viewClickEditAlarm.isVisible = !hasAlarms
        binding.tvClickEditAlarmDescriptionNotEmpty.isVisible = hasAlarms
        binding.rvEditAlarm.isVisible = hasAlarms
        binding.switchAlarm.isVisible = hasAlarms
    }

    private fun setupItemRecyclerView() {
        binding.rvEditItem.adapter = itemAdapter
        binding.rvEditItem.layoutManager =
            FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }
    }

    private fun setupAlarmRecyclerView() {
        binding.rvEditAlarm.adapter = alarmAdapter
        binding.rvEditAlarm.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun navigateToScreen(
        fragmentClass: Class<out Fragment>,
        bundle: Bundle? = null,
    ) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fcv_personal_edit, fragmentClass, bundle)
        transaction.addToBackStack(fragmentClass.simpleName)
        transaction.commit()
    }

    private fun checkAndRequestSpecialPermission() {
        if (PermissionUtil.hasExactAlarmPermission(requireContext())) {
            navigateToAlarmEditScreen()
            return
        }
        showExactAlarmSettingsDialog()
    }

    private fun navigateToAlarmEditScreen() {
        val uiState = viewModel.uiState.value ?: return
        navigateToScreen(
            AlarmEditFragment::class.java,
            AlarmEditFragment.newBundle(
                bottariId = uiState.id,
                bottariTitle = uiState.title,
                alarm = uiState.alarm,
            ),
        )
    }

    private fun showSettingsDialog() {
        AlertDialog
            .Builder(requireContext())
            .setTitle(R.string.common_permission_dialog_title_text)
            .setMessage(R.string.common_notification_permission_dialog_message_text)
            .setPositiveButton(R.string.common_permission_dialog_positive_btn_text) { _, _ ->
                PermissionUtil.openAppSettings(requireContext())
            }.setNegativeButton(R.string.common_permission_dialog_negative_btn_text, null)
            .show()
    }

    private fun showExactAlarmSettingsDialog() {
        AlertDialog
            .Builder(requireContext())
            .setTitle(R.string.common_permission_dialog_title_text)
            .setMessage(R.string.alarm_edit_exact_alarm_permission_dialog_message_text)
            .setPositiveButton(R.string.common_permission_dialog_positive_btn_text) { _, _ ->
                PermissionUtil.requestExactAlarmPermission(
                    requireContext(),
                )
            }.setNegativeButton(R.string.common_permission_dialog_negative_btn_text, null)
            .show()
    }

    private fun showRenameDialog() {
        val uiState = viewModel.uiState.value ?: return

        BottariRenameDialog
            .newInstance(uiState.id, uiState.title)
            .show(parentFragmentManager, BottariRenameDialog::class.java.name)
    }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        fun newBundle(bottariId: Long) =
            Bundle().apply {
                putLong(ARG_BOTTARI_ID, bottariId)
            }
    }
}
