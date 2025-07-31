package com.bottari.presentation.view.template

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityMarketBinding
import com.bottari.presentation.view.template.detail.TemplateDetailFragment
import com.bottari.presentation.view.template.my.MyTemplateFragment

class TemplateActivity :
    BaseActivity<ActivityMarketBinding>(ActivityMarketBinding::inflate),
    TemplateNavigator {
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
                R.id.fcv_template,
                TemplateDetailFragment::class.java,
                TemplateDetailFragment.newBundle(bottariTemplateId, isMyTemplate),
            )
            if (isMyTemplate) addToBackStack(null)
            commit()
        }
    }

    override fun navigateToMyTemplate() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fcv_template, MyTemplateFragment::class.java, null)
            commit()
        }
    }

    private fun navigateToScreen() {
        val type =
            TemplateDestinationType.valueOf(
                intent.getStringExtra(EXTRA_DESTINATION_TYPE) ?: TemplateDestinationType.MY_TEMPLATE.name,
            )
        when (type) {
            TemplateDestinationType.MY_TEMPLATE -> navigateToMyTemplate()
            TemplateDestinationType.DETAIL -> navigateToDetailScreen()
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
            Intent(context, TemplateActivity::class.java).apply {
                putExtra(EXTRA_DESTINATION_TYPE, TemplateDestinationType.DETAIL.name)
                putExtra(EXTRA_BOTTARI_TEMPLATE_ID, bottariId)
            }

        fun newIntentForMyTemplate(context: Context): Intent =
            Intent(context, TemplateActivity::class.java).apply {
                putExtra(EXTRA_DESTINATION_TYPE, TemplateDestinationType.MY_TEMPLATE.name)
            }
    }
}
