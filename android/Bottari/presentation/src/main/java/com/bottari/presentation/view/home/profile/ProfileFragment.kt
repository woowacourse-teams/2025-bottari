package com.bottari.presentation.view.home.profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseFragment
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.common.extension.showSnackbar
import com.bottari.presentation.databinding.FragmentProfileBinding

class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate),
    TextWatcher {
    private val viewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory(requireContext().getSSAID()) }
    private val inputManager: InputMethodManager by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupListener()
    }

    override fun beforeTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int,
    ) {
    }

    override fun onTextChanged(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Int,
    ) {
    }

    override fun afterTextChanged(p0: Editable?) {
        viewModel.updateNickname(p0.toString().trim())
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            toggleLoadingIndicator(uiState.isLoading)
            if (binding.etNicknameEdit.text.toString() != uiState.editingNickname) {
                binding.etNicknameEdit.setText(uiState.editingNickname)
                binding.etNicknameEdit.setSelection(uiState.editingNickname.length)
            }
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                ProfileUiEvent.FetchMemberInfoFailure -> requireView().showSnackbar(R.string.profile_fetch_failure_text)
                ProfileUiEvent.SaveMemberNicknameSuccess -> requireView().showSnackbar(R.string.profile_nickname_save_success_text)
                ProfileUiEvent.SaveMemberNicknameFailure -> requireView().showSnackbar(R.string.profile_nickname_save_failure_text)
                ProfileUiEvent.InvalidNicknameRule -> requireView().showSnackbar(R.string.profile_invalid_nickname_rule_text)
            }
        }
    }

    private fun setupListener() {
        setupNicknameEditListener()
        setupNicknameEditButtonClickListener()
    }

    private fun setupNicknameEditListener() {
        binding.etNicknameEdit.addTextChangedListener(this)
        binding.etNicknameEdit.setOnEditorActionListener { _, actionId, _ ->
            handleEditorAction(actionId)
        }
        binding.etNicknameEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) confirmNicknameEdit()
        }
    }

    private fun setupNicknameEditButtonClickListener() {
        binding.btnNicknameEdit.setOnClickListener {
            if (binding.etNicknameEdit.isFocused) {
                setEditMode(false)
                binding.btnNicknameEdit.setImageResource(R.drawable.btn_edit)
                return@setOnClickListener
            }
            setEditMode(true)
            binding.btnNicknameEdit.setImageResource(R.drawable.btn_confirm)
        }
    }

    private fun handleEditorAction(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_NEXT ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED
        ) {
            setEditMode(false)
            return true
        }
        return false
    }

    private fun confirmNicknameEdit() {
        binding.btnNicknameEdit.setImageResource(R.drawable.btn_edit)
        viewModel.saveNickname()
    }

    private fun setEditMode(enabled: Boolean) {
        binding.etNicknameEdit.apply {
            isFocusable = enabled
            isFocusableInTouchMode = enabled
            isCursorVisible = enabled
            isClickable = enabled
            isLongClickable = enabled
            isEnabled = true
            if (enabled) {
                requestFocus()
                setSelection(text.length)
                showKeyboard(this)
                return
            }
            clearFocus()
            hideKeyboard(this)
        }
    }

    private fun showKeyboard(view: View) {
        view.post { inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT) }
    }

    private fun hideKeyboard(view: View) {
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}
