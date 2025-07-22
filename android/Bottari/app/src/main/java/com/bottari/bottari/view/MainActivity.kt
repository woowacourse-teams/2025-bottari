package com.bottari.bottari.view

import android.os.Bundle
import androidx.activity.viewModels
import com.bottari.bottari.databinding.ActivityMainBinding
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.base.UiState
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.view.home.HomeActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory(this.getSSAID()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.loginState.observe(this, ::handleLoginState)
    }

    private fun handleLoginState(uiState: UiState<Unit>) {
        when(uiState) {
            is UiState.Loading -> Unit
            is UiState.Success -> navigateToHome()
            is UiState.Failure -> Unit
        }
    }

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
    }
}
