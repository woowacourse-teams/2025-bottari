package com.bottari.presentation.view.join

import android.content.ClipboardManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import androidx.annotation.StringRes
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bottari.logger.LogEventHelper
import com.bottari.presentation.R
import com.bottari.presentation.databinding.DialogTeamBottariJoinBinding
import com.bottari.presentation.util.DeeplinkHelper
import com.bottari.presentation.view.home.team.TeamBottariFragment.Companion.REQUEST_KEY_REQUIRE_REFRESH

class TeamBottariJoinDialog : DialogFragment() {
    private val viewModel: TeamBottariJoinViewModel by viewModels { TeamBottariJoinViewModel.Factory() }
    private var _binding: DialogTeamBottariJoinBinding? = null
    val binding: DialogTeamBottariJoinBinding get() = _binding!!
    private val clipboardManager: ClipboardManager by lazy {
        requireContext().getSystemService(ClipboardManager::class.java)
    }

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
        setupUI()
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

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            handleEditTextState(uiState.inviteCode)
            handleJoinButtonState(uiState.isCanJoin)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                TeamBottariJoinUiEvent.JoinTeamBottariSuccess -> handleJoinTeamBottariSuccess()
                TeamBottariJoinUiEvent.JoinTeamBottariFailure.NotFoundException,
                -> handleJoinTeamBottariFailure(R.string.join_team_bottari_not_found_exception_text)

                TeamBottariJoinUiEvent.JoinTeamBottariFailure.DuplicatedException,
                -> handleJoinTeamBottariFailure(R.string.join_team_bottari_duplicated_exception_text)

                TeamBottariJoinUiEvent.UnexpectedException,
                -> handleJoinTeamBottariFailure(R.string.common_unexpected_exception_text)
            }
        }
    }

    private fun setupUI() {
        setInviteCodeFromClipboard()
    }

    private fun setupDialog() =
        dialog?.run {
            setCancelable(false)
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            window?.setLayout(
                (resources.displayMetrics.widthPixels * WIDTH_RATIO).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }

    private fun setupListener() {
        binding.etTeamBottariJoinInviteCode.doAfterTextChanged {
            viewModel.updateInviteCode(it?.toString().orEmpty())
            handleDescriptionTextVisibility("")
        }
        binding.etTeamBottariJoinInviteCode.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != IME_ACTION_DONE) return@setOnEditorActionListener false
            if (binding.btnTeamBottariJoin.isEnabled.not()) return@setOnEditorActionListener false
            viewModel.joinTeamBottari()
            true
        }
        binding.btnTeamBottariJoinClose.setOnClickListener { dismiss() }
        binding.btnTeamBottariJoin.setOnClickListener {
            viewModel.joinTeamBottari()
        }
    }

    private fun setInviteCodeFromClipboard() {
        if (binding.etTeamBottariJoinInviteCode.text?.isNotBlank() == true) return
        val clipData = clipboardManager.primaryClip ?: return
        if (clipData.itemCount <= 0) return

        val clipItem = clipData.getItemAt(0)
        val clipText = clipItem.text?.toString().orEmpty()
        val code = DeeplinkHelper.getInviteCode(clipText) ?: return
        viewModel.updateInviteCode(code)
    }

    private fun handleDescriptionTextVisibility(message: String) {
        binding.teamBottariJoinDialogDescriptionText.isVisible = message.isNotBlank()
        binding.teamBottariJoinDialogDescriptionText.text = message
    }

    private fun handleEditTextState(inviteCode: String) {
        if (binding.etTeamBottariJoinInviteCode.text.toString() == inviteCode) return
        binding.etTeamBottariJoinInviteCode.setText(inviteCode)
        binding.etTeamBottariJoinInviteCode.setSelection(inviteCode.length)
    }

    private fun handleJoinButtonState(isCanJoin: Boolean) {
        binding.btnTeamBottariJoin.isEnabled = isCanJoin
        binding.btnTeamBottariJoin.alpha =
            if (isCanJoin) ENABLED_ALPHA_VALUE else DISABLED_ALPHA_VALUE
    }

    private fun handleJoinTeamBottariSuccess() {
        setFragmentResult(REQUEST_KEY_REQUIRE_REFRESH, Bundle.EMPTY)
        dismiss()
    }

    private fun handleJoinTeamBottariFailure(
        @StringRes messageResId: Int,
    ) {
        binding.etTeamBottariJoinInviteCode.text.clear()
        handleDescriptionTextVisibility(getString(messageResId))
    }

    companion object {
        private const val WIDTH_RATIO = 0.9
        private const val DISABLED_ALPHA_VALUE = 0.4f
        private const val ENABLED_ALPHA_VALUE = 1f

        fun newInstance(): TeamBottariJoinDialog = TeamBottariJoinDialog()
    }
}
