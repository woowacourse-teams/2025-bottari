package com.bottari.presentation.view.home.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bottari.presentation.base.BaseFragment
import com.bottari.presentation.base.UiState
import com.bottari.presentation.databinding.FragmentProfileBinding
import com.bottari.presentation.extension.getSSAID

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory(requireContext().getSSAID()) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.nickname.observe(viewLifecycleOwner, ::handleNicknameState)
    }

    private fun handleNicknameState(uiState: UiState<String>) {
        when (uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> binding.tvProfileNickname.text = uiState.data
            is UiState.Failure -> Unit
        }
    }
}
