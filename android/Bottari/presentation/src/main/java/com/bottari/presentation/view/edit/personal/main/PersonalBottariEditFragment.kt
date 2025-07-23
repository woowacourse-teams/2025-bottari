package com.bottari.presentation.view.edit.personal.main

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
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.util.permission.PermissionUtil
import com.bottari.presentation.view.edit.alarm.AlarmEditFragment
import com.bottari.presentation.view.edit.personal.item.PersonalItemEditFragment
import com.bottari.presentation.view.edit.personal.main.adapter.PersonalBottariEditAlarmAdapter
import com.bottari.presentation.view.edit.personal.main.adapter.PersonalBottariEditItemAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar

class PersonalBottariEditFragment : BaseFragment<FragmentPersonalBottariEditBinding>(FragmentPersonalBottariEditBinding::inflate) {
    private val viewModel: PersonalBottariEditViewModel by viewModels {
        PersonalBottariEditViewModel.Factory(getBottariId())
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
                    Snackbar.make(binding.root, "권한 요청에 실패했어요.", Snackbar.LENGTH_SHORT).show()
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

    private fun setupObserver() {
        viewModel.bottari.observe(viewLifecycleOwner, ::handleBottariState)
        viewModel.items.observe(viewLifecycleOwner, ::handleItemsState)
        viewModel.alarms.observe(viewLifecycleOwner, ::handleAlarmState)
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
            navigateToScreen(
                PersonalItemEditFragment::class.java,
                PersonalItemEditFragment.newBundle(getBottariId()),
            )
        }
        binding.clEditAlarm.setOnClickListener {
            if (PermissionUtil.hasAllRuntimePermissions(requireContext())) {
                checkAndRequestSpecialPermission()
                return@setOnClickListener
            }
            permissionLauncher.launch(PermissionUtil.getRequiredPermissions())
        }
    }

    private fun getBottariId(): Long = arguments?.getLong(EXTRA_BOTTARI_ID) ?: INVALID_BOTTARI_ID

    private fun handleBottariState(uiState: UiState<BottariUiModel>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                binding.tvBottariTitle.text = uiState.data.title
            }

            is UiState.Failure -> Unit
        }
    }

    private fun handleItemsState(uiState: UiState<List<BottariItemUiModel>>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                itemAdapter.submitList(uiState.data)
                toggleItemSection(uiState.data.isNotEmpty())
            }

            is UiState.Failure -> Unit
        }
    }

    private fun handleAlarmState(uiState: UiState<List<AlarmUiModel>>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                alarmAdapter.submitList(uiState.data)
                toggleAlarmSelection(uiState.data.isNotEmpty())
            }

            is UiState.Failure -> Unit
        }
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
        PermissionUtil.requestExactAlarmPermission(requireContext())
    }

    private fun showSettingsDialog() {
        AlertDialog
            .Builder(requireContext())
            .setTitle("권한이 필요합니다")
            .setMessage("앱 기능을 사용하려면 권한을 허용해야 합니다. 설정 화면으로 이동하시겠습니까?")
            .setPositiveButton("설정으로 이동") { _, _ ->
                PermissionUtil.openAppSettings(requireContext())
            }.setNegativeButton("취소", null)
            .show()
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRAS_BOTTARI_ID"
        private const val INVALID_BOTTARI_ID = -1L

        fun newBundle(bottariId: Long) =
            Bundle().apply {
                putLong(EXTRA_BOTTARI_ID, bottariId)
            }
    }
}
