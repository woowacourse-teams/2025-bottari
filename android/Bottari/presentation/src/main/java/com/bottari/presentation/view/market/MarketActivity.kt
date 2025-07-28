package com.bottari.presentation.view.market

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.databinding.ActivityMarketBinding
import com.bottari.presentation.view.market.detail.MarketBottariDetailFragment
import com.bottari.presentation.view.market.my.MyBottariTemplateFragment

class MarketActivity :
    BaseActivity<ActivityMarketBinding>(ActivityMarketBinding::inflate),
    MarketNavigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToScreen()
    }

    override fun navigateToDetail(
        bottariTemplateId: Long,
        isMyTemplate: Boolean,
    ) {
        supportFragmentManager.beginTransaction().apply {
            replace(
                R.id.fcv_market,
                MarketBottariDetailFragment::class.java,
                MarketBottariDetailFragment.newBundle(bottariTemplateId, isMyTemplate),
            )
            commit()
        }
    }

    override fun navigateToMyTemplate() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fcv_market, MyBottariTemplateFragment::class.java, null)
            commit()
        }
    }

    private fun navigateToScreen() {
        val type =
            MarketDestinationType.valueOf(
                intent.getStringExtra(EXTRA_DESTINATION_TYPE) ?: MarketDestinationType.MY_TEMPLATE.name,
            )
        when (type) {
            MarketDestinationType.MY_TEMPLATE -> navigateToMyTemplate()
            MarketDestinationType.DETAIL -> navigateToDetailScreen()
        }
    }

    private fun navigateToDetailScreen() {
        val bottariTemplateId = intent.getLongExtra(EXTRA_BOTTARI_TEMPLATE_ID, -1L)
        if (bottariTemplateId == -1L) finish()

        navigateToDetail(bottariTemplateId)
    }

    companion object {
        private const val EXTRA_DESTINATION_TYPE = "EXTRA_DESTINATION_TYPE"
        private const val EXTRA_BOTTARI_TEMPLATE_ID = "EXTRA_BOTTARI_ID"

        fun newIntentForDetail(
            context: Context,
            bottariId: Long,
        ): Intent =
            Intent(context, MarketActivity::class.java).apply {
                putExtra(EXTRA_DESTINATION_TYPE, MarketDestinationType.DETAIL.name)
                putExtra(EXTRA_BOTTARI_TEMPLATE_ID, bottariId)
            }

        fun newIntentForMyTemplate(context: Context): Intent =
            Intent(context, MarketActivity::class.java).apply {
                putExtra(EXTRA_DESTINATION_TYPE, MarketDestinationType.MY_TEMPLATE.name)
            }
    }
}
