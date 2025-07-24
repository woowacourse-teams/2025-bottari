package com.bottari.presentation.view.home.market

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.databinding.ActivityMarketBottariDetailBinding
import com.bottari.presentation.extension.getSSAID
import com.bottari.presentation.extension.takeSuccess
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import com.bottari.presentation.view.home.market.adapter.MarketBottariDetailAdapter

class MarketBottariDetailActivity : BaseActivity<ActivityMarketBottariDetailBinding>(ActivityMarketBottariDetailBinding::inflate) {
    private val viewModel: MarketBottariDetailViewModel by viewModels {
        MarketBottariDetailViewModel.Factory(ssaid = getSSAID(), bottarID = fetchBottariId())
    }

    private val adapter by lazy { MarketBottariDetailAdapter() }

    private fun fetchBottariId(): Long = intent?.getLongExtra(EXTRA_BOTTARI_ID, INVALID_BOTTARI_ID) ?: INVALID_BOTTARI_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        setupUI()
        setupListener()
    }

    private fun setupObserver() {
        viewModel.bottariTemplate.observe(this) { uiState ->
            uiState.takeSuccess()?.let { template ->
                binding.tvBottariTitle.text = template.title
                adapter.submitList(template.items)
            }
        }
        viewModel.createSuccess.observe(this) { uiState ->
            uiState.takeSuccess()?.let {
                navigateBottariEdit(uiState.takeSuccess()!!)
            }
        }
    }

    private fun setupUI() {
        binding.rvMarketBottariDetail.adapter = adapter
        binding.rvMarketBottariDetail.layoutManager = LinearLayoutManager(this)
    }

    private fun setupListener() {
        binding.btnPrevious.setOnClickListener {
            finish()
        }
        binding.btnTakeTemplate.setOnClickListener {
            takeBottariTemplate()
        }
    }

    private fun takeBottariTemplate() {
        viewModel.takeBottariTemplate()
    }

    private fun navigateBottariEdit(bottariId: Long) {
        startActivity(PersonalBottariEditActivity.newIntent(this, bottariId))
        finish()
    }

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val INVALID_BOTTARI_ID = -1L

        fun newIntent(
            context: Context,
            bottariId: Long,
        ): Intent =
            Intent(context, MarketBottariDetailActivity::class.java).apply {
                putExtra(EXTRA_BOTTARI_ID, bottariId)
            }
    }
}
