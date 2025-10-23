package com.bottari.presentation.view.template

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityTemplateBinding
import com.bottari.presentation.view.template.detail.TemplateDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemplateActivity : BaseActivity<ActivityTemplateBinding>(ActivityTemplateBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToDetail()
    }

    private fun navigateToDetail() {
        val bottariTemplateId = intent.getLongExtra(EXTRA_BOTTARI_TEMPLATE_ID, -1L)
        val isMyTemplate = intent.getBooleanExtra(EXTRA_IS_MY_TEMPLATE, false)
        val isBookmark = intent.getBooleanExtra(EXTRA_IS_BOOKMARK, false)

        if (bottariTemplateId == -1L) finish()

        supportFragmentManager.commit {
            replace(
                R.id.fcv_template,
                TemplateDetailFragment::class.java,
                TemplateDetailFragment.newBundle(bottariTemplateId, isMyTemplate, isBookmark),
            )
        }
    }

    companion object {
        private const val EXTRA_BOTTARI_TEMPLATE_ID = "EXTRA_BOTTARI_TEMPLATE_ID"
        private const val EXTRA_IS_MY_TEMPLATE = "EXTRA_IS_MY_TEMPLATE"
        private const val EXTRA_IS_BOOKMARK = "EXTRA_IS_BOOKMARK"

        fun newIntentForDetail(
            context: Context,
            bottariId: Long,
            isMyTemplate: Boolean,
        ): Intent =
            Intent(context, TemplateActivity::class.java).putExtras(
                bundleOf(
                    EXTRA_BOTTARI_TEMPLATE_ID to bottariId,
                    EXTRA_IS_MY_TEMPLATE to isMyTemplate,
                ),
            )

        fun newIntentForBookmark(
            context: Context,
            templateId: Long,
            isBookmark: Boolean,
        ): Intent =
            Intent(context, TemplateActivity::class.java).putExtras(
                bundleOf(
                    EXTRA_BOTTARI_TEMPLATE_ID to templateId,
                    EXTRA_IS_BOOKMARK to isBookmark,
                ),
            )
    }
}
