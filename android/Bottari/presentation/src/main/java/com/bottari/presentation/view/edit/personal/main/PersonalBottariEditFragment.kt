package com.bottari.presentation.view.edit.personal.main

import PersonalBottariEditViewModel
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentPersonalBottariEditBinding
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.extension.takeSuccess
import com.bottari.presentation.model.BottariDetailUiModel
import com.bottari.presentation.util.permission.PermissionUtil
import com.bottari.presentation.util.permission.PermissionUtil.requiredPermissions
import com.bottari.presentation.view.edit.alarm.AlarmEditFragment
import com.bottari.presentation.view.edit.personal.item.PersonalItemEditFragment
import com.bottari.presentation.view.edit.personal.main.adapter.PersonalBottariEditAlarmAdapter
import com.bottari.presentation.view.edit.personal.main.adapter.PersonalBottariEditItemAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class PersonalBottariEditFragment : BaseFragment<FragmentPersonalBottariEditBinding>(FragmentPersonalBottariEditBinding::inflate) {
    private val viewModel: PersonalBottariEditViewModel by viewModels {
        val bottariId = arguments?.getLong(EXTRA_BOTTARI_ID) ?: error(ERROR_REQUIRE_BOTTARI_ID)
        PersonalBottariEditViewModel.Factory(requireContext().getSSAID(), bottariId)
    }
    private val itemAdapter: PersonalBottariEditItemAdapter by lazy { PersonalBottariEditItemAdapter() }
    private val alarmAdapter: PersonalBottariEditAlarmAdapter by lazy { PersonalBottariEditAlarmAdapter() }
    private val permissionLauncher =
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
                    showSnackbar(R.string.alarm_edit_permission_failure_text)
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
        viewModel.bottari.observe(viewLifecycleOwner, ::handleBottariState)
    }

    private fun setupUI() {
        setupItemRecyclerView()
        setupAlarmRecyclerView()
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.clEditItem.setOnClickListener {
            val bottariDetail = viewModel.bottari.value?.takeSuccess() ?: return@setOnClickListener
            navigateToScreen(
                PersonalItemEditFragment::class.java,
                PersonalItemEditFragment.newBundle(bottariDetail),
            )
        }
        binding.clEditAlarm.setOnClickListener {
            if (PermissionUtil.hasAllRuntimePermissions(requireContext())) {
                checkAndRequestSpecialPermission()
                return@setOnClickListener
            }
            permissionLauncher.launch(requiredPermissions)
        }
        binding.switchAlarm.setOnCheckedChangeListener { _, isChecked ->

            viewModel.toggleAlarmState(isChecked)
        }
    }

    private fun handleBottariState(uiState: UiState<BottariDetailUiModel>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                setupTitle(uiState.data)
                setupItems(uiState.data)
                setupAlarm(uiState.data)
            }

            is UiState.Failure -> Unit
        }
    }

    private fun setupTitle(bottari: BottariDetailUiModel) {
        binding.tvBottariTitle.text = bottari.title
    }

    private fun setupItems(bottari: BottariDetailUiModel) {
        itemAdapter.submitList(bottari.items)
        toggleItemSection(bottari.items.isNotEmpty())
    }

    private fun setupAlarm(bottari: BottariDetailUiModel) {
        alarmAdapter.submitList(listOf(bottari.alarm))
        toggleAlarmSelection(bottari.alarm != null)
        binding.switchAlarm.isChecked = bottari.alarm?.isActive ?: false
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
            navigateToScreen(AlarmEditFragment::class.java, AlarmEditFragment.newBundle())
            return
        }
        showExactAlarmSettingsDialog()
    }

    private fun showSettingsDialog() {
        AlertDialog
            .Builder(requireContext())
            .setTitle(R.string.alarm_edit_permission_dialog_title_text)
            .setMessage(R.string.alarm_edit_permission_dialog_message_text)
            .setPositiveButton(R.string.alarm_edit_permission_dialog_positive_btn_text) { _, _ ->
                PermissionUtil.openAppSettings(requireContext())
            }.setNegativeButton(R.string.alarm_edit_permission_dialog_negative_btn_text, null)
            .show()
    }

    private fun showExactAlarmSettingsDialog() {
        AlertDialog
            .Builder(requireContext())
            .setTitle(R.string.alarm_edit_permission_dialog_title_text)
            .setMessage(R.string.alarm_edit_exact_alarm_permission_dialog_message_text)
            .setPositiveButton(R.string.alarm_edit_permission_dialog_positive_btn_text) { _, _ ->
                PermissionUtil.requestExactAlarmPermission(
                    requireContext(),
                )
            }.setNegativeButton(R.string.alarm_edit_permission_dialog_negative_btn_text, null)
            .show()
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRAS_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "보따리 ID가 없습니다"

        fun newBundle(bottariId: Long) =
            Bundle().apply {
                putLong(EXTRA_BOTTARI_ID, bottariId)
            }
    }
}
