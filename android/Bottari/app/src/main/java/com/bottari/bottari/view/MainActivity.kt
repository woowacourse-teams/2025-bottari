package com.bottari.bottari.view

import android.os.Bundle
import androidx.activity.viewModels
import com.bottari.bottari.databinding.ActivityMainBinding
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.common.extension.getSSAID
import com.bottari.presentation.view.home.HomeActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory(this.getSSAID()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.uiEvent.observe(this) { uiEvent ->
            when (uiEvent) {
                MainUiEvent.LoginSuccess -> navigateToHome()
                MainUiEvent.LoginFailure -> Unit
                MainUiEvent.RegisterFailure -> Unit
            }
        }
    }

    private fun navigateToHome() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
        finish()
    }
}
