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
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentPersonalBottariEditBinding
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.util.AlarmScheduler
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

class PersonalBottariEditFragment : BaseFragment<FragmentPersonalBottariEditBinding>(FragmentPersonalBottariEditBinding::inflate) {
    private val viewModel: PersonalBottariEditViewModel by viewModels {
        val bottariId = requireArguments().getLong(ARG_BOTTARI_ID)
        PersonalBottariEditViewModel.Factory(bottariId, AlarmScheduler())
    }
    private lateinit var popupMenu: PopupMenu
    private val itemAdapter: PersonalBottariEditItemAdapter by lazy { PersonalBottariEditItemAdapter() }
    private val permissionLauncher = getPermissionLauncher()
    private val alarmViewBinder: AlarmViewBinder by lazy { AlarmViewBinder(requireContext()) }

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
                    requireView().showSnackbar(R.string.common_permission_failure_text)
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
            setupAlarm(uiState)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                PersonalBottariEditUiEvent.FetchBottariFailure -> requireView().showSnackbar(R.string.bottari_edit_fetch_failure_text)
                PersonalBottariEditUiEvent.CreateTemplateFailure ->
                    requireView().showSnackbar(
                        R.string.bottari_edit_create_template_failure_text,
                    )

                PersonalBottariEditUiEvent.CreateTemplateSuccess ->
                    requireView().showSnackbar(
                        R.string.bottari_edit_create_template_success_text,
                    )

                is PersonalBottariEditUiEvent.ToggleAlarmStateFailure ->
                    requireView().showSnackbar(
                        R.string.bottari_edit_toggle_alarm_state_failure_text,
                    )
            }
        }
    }

    private fun setupUI() {
        setupPopupMenu()
        setupItemRecyclerView()
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
        binding.viewClickEditAlarm.setOnClickListener {
            if (PermissionUtil.hasAllRuntimePermissions(requireContext())) {
                checkAndRequestSpecialPermission()
                return@setOnClickListener
            }
            permissionLauncher.launch(requiredPermissions)
        }
        binding.switchAlarm.setOnClickListener {
            viewModel.updateAlarmState()
            toggleAlarmSelection(
                binding.switchAlarm.isChecked,
                viewModel.uiState.value?.alarm != null,
            )
        }
        binding.viewAlarmItem.clAlarmItem.setOnClickListener {
            if (PermissionUtil.hasAllRuntimePermissions(requireContext())) {
                checkAndRequestSpecialPermission()
                return@setOnClickListener
            }
            permissionLauncher.launch(requiredPermissions)
        }
        parentFragmentManager.setFragmentResultListener(
            BottariRenameDialog.SAVE_BOTTARI_TITLE_RESULT_KEY,
            viewLifecycleOwner,
        ) { _, _ ->
            viewModel.fetchBottari()
        }
    }

    private fun setupPopupMenu() {
        popupMenu = createPopupMenu()
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

    private fun setupAlarm(uiState: PersonalBottariEditUiState) {
        alarmViewBinder.bind(binding, uiState)
    }

    private fun toggleAlarmSelection(
        isActive: Boolean,
        hasAlarm: Boolean,
    ) {
        val showEmptyState = isActive && !hasAlarm
        val showAlarm = isActive && hasAlarm

        binding.tvClickEditAlarmTitle.isVisible = showEmptyState
        binding.tvClickEditAlarmDescription.isVisible = showEmptyState
        binding.viewClickEditAlarm.isVisible = showEmptyState
        binding.tvClickEditAlarmDescriptionNotEmpty.isVisible = showAlarm
        binding.viewAlarmItem.clAlarmItem.isVisible = showAlarm
    }

    private fun toggleItemSection(hasItems: Boolean) {
        binding.tvClickEditItemTitle.isVisible = !hasItems
        binding.tvClickEditItemDescription.isVisible = !hasItems
        binding.viewClickEditItem.isVisible = !hasItems
        binding.tvClickEditItemDescriptionNotEmpty.isVisible = hasItems
        binding.rvEditItem.isVisible = hasItems
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

    private fun navigateToScreen(
        fragmentClass: Class<out Fragment>,
        bundle: Bundle? = null,
    ) {
        parentFragmentManager.commit {
            replace(R.id.fcv_personal_edit, fragmentClass, bundle)
            addToBackStack(fragmentClass.simpleName)
        }
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
        CustomAlertDialog
            .newInstance(DialogPresetType.NAVIGATE_TO_NOTIFICATION_SETTINGS)
            .setDialogListener(
                object : DialogListener {
                    override fun onClickNegative() {}

                    override fun onClickPositive() {
                        PermissionUtil.openAppSettings(requireContext())
                    }
                },
            ).show(parentFragmentManager, DialogPresetType.NAVIGATE_TO_NOTIFICATION_SETTINGS.name)
    }

    private fun showExactAlarmSettingsDialog() {
        CustomAlertDialog
            .newInstance(DialogPresetType.NAVIGATE_TO_ALARM_SETTINGS)
            .setDialogListener(
                object : DialogListener {
                    override fun onClickNegative() {}

                    override fun onClickPositive() {
                        PermissionUtil.requestExactAlarmPermission(requireContext())
                    }
                },
            ).show(parentFragmentManager, DialogPresetType.NAVIGATE_TO_ALARM_SETTINGS.name)
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
