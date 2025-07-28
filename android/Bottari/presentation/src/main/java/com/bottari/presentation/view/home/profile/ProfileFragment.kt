package com.bottari.presentation.view.home.profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentProfileBinding
import com.bottari.presentation.extension.getSSAID

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
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

    private fun setupObserver() {
        viewModel.nickname.observe(viewLifecycleOwner, ::handleNicknameState)
    }

    private fun setupListener() {
        setupRootClickListener()
        setupNicknameEditListener()
        setupNicknameEditButtonClickListener()
    }

    private fun handleNicknameState(uiState: UiState<String>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> {
                binding.etNicknameEdit.text =
                    Editable.Factory.getInstance().newEditable(uiState.data)
            }

            is UiState.Failure -> {
                binding.etNicknameEdit.text =
                    Editable.Factory.getInstance().newEditable(uiState.message)
            }
        }
    }

    private fun setupRootClickListener() {
        binding.root.setOnClickListener {
            if (binding.etNicknameEdit.isFocused) {
                binding.etNicknameEdit.clearFocus()
            }
        }
    }

    private fun setupNicknameEditListener() {
        binding.etNicknameEdit.setOnEditorActionListener { _, actionId, _ ->
            handleEditorAction(actionId)
        }
        binding.etNicknameEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                confirmNicknameEdit()
            }
        }
    }

    private fun setupNicknameEditButtonClickListener() {
        binding.btnNicknameEdit.setOnClickListener {
            setEditMode(true)
        }
    }

    private fun handleEditorAction(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_NEXT ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED
        ) {
            hideKeyboard(binding.etNicknameEdit)
            return true
        }
        return false
    }

    private fun confirmNicknameEdit() {
        val newNickname = binding.etNicknameEdit.text.toString()
        setEditMode(false)
        viewModel.updateNickName(newNickname)
    }

    private fun showKeyboard(view: View) {
        view.post { inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT) }
    }

    private fun hideKeyboard(view: View) {
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
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
}
