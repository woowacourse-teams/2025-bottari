package com.bottari.presentation.view.join

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bottari.logger.LogEventHelper
import com.bottari.presentation.databinding.DialogTeamBottariJoinBinding
import com.bottari.presentation.view.home.team.TeamBottariFragment.Companion.REQUEST_KEY_REQUIRE_REFRESH

class TeamBottariJoinDialog :
    DialogFragment(),
    TextWatcher {
    private val viewModel: TeamBottariJoinViewModel by viewModels { TeamBottariJoinViewModel.Factory() }
    private var _binding: DialogTeamBottariJoinBinding? = null
    val binding: DialogTeamBottariJoinBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogEventHelper.logScreenEnter(javaClass.simpleName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogTeamBottariJoinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        setupDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int,
    ) {
    }

    override fun afterTextChanged(s: Editable?) {
        viewModel.updateInviteCode(s.toString())
        handleDescriptionTextVisibility(false)
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int,
    ) {
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            handleCreateButtonState(uiState.isCanJoin)
            handleEditTextState(uiState.inviteCode)
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                TeamBottariJoinUiEvent.JoinTeamBottariFailure -> handleJoinTeamBottariFailure()
                TeamBottariJoinUiEvent.JoinTeamBottariSuccess -> handleJoinTeamBottariSuccess()
            }
        }
    }

    private fun setupDialog() {
        val metrics = Resources.getSystem().displayMetrics
        val width = (metrics.widthPixels * WIDTH_RATIO).toInt()
        dialog?.run {
            setCancelable(false)
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            window?.setLayout(
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }
    }

    private fun setupListener() {
        binding.etTeamBottariJoinInviteCode.addTextChangedListener(this)
        binding.btnTeamBottariJoinClose.setOnClickListener { dismiss() }
        binding.btnTeamBottariJoin.setOnClickListener {
            viewModel.joinTeamBottari()
        }
    }

    private fun handleDescriptionTextVisibility(isVisible: Boolean) {
        binding.teamBottariJoinDialogDescriptionText.isVisible = isVisible
    }

    private fun handleEditTextState(inviteCode: String) {
        if (binding.etTeamBottariJoinInviteCode.text.toString() == inviteCode) return
        binding.etTeamBottariJoinInviteCode.setText(inviteCode)
        binding.etTeamBottariJoinInviteCode.setSelection(inviteCode.length)
    }

    private fun handleCreateButtonState(isCanCreate: Boolean) {
        binding.btnTeamBottariJoin.isEnabled = isCanCreate
        binding.btnTeamBottariJoin.alpha =
            if (isCanCreate) ENABLED_ALPHA_VALUE else DISABLED_ALPHA_VALUE
    }

    private fun handleJoinTeamBottariSuccess() {
        setFragmentResult(REQUEST_KEY_REQUIRE_REFRESH, Bundle.EMPTY)
        dismiss()
    }

    private fun handleJoinTeamBottariFailure() {
        binding.etTeamBottariJoinInviteCode.text.clear()
        handleDescriptionTextVisibility(true)
    }

    companion object {
        private const val WIDTH_RATIO = 0.9
        private const val DISABLED_ALPHA_VALUE = 0.4f
        private const val ENABLED_ALPHA_VALUE = 1f

        fun newInstance(): TeamBottariJoinDialog = TeamBottariJoinDialog()
    }
}
