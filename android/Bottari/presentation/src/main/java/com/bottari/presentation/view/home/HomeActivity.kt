package com.bottari.presentation.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.common.extension.applyWindowInsetsWithBottomNavigation
import com.bottari.presentation.databinding.ActivityHomeBinding
import com.bottari.presentation.view.home.personal.BottariFragment
import com.bottari.presentation.view.home.profile.ProfileFragment
import com.bottari.presentation.view.home.team.TeamBottariFragment
import com.bottari.presentation.view.home.template.TemplateFragment
import com.bottari.presentation.view.setting.SettingActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    private val deeplinkFlag: Boolean by lazy { intent.getBooleanExtra(KEY_DEEPLINK, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupListener()
        if (savedInstanceState == null) {
            binding.bnvHome.selectedItemId =
                if (deeplinkFlag) R.id.menu_team_bottari else R.id.menu_personal_bottari
        }
    }

    private fun setupUI() {
        setBottomNavigationView()
    }

    private fun setupListener() {
        binding.btnSetting.setOnClickListener { startActivity(SettingActivity.newIntent(this)) }
    }

    private fun setBottomNavigationView() {
        binding.root.applyWindowInsetsWithBottomNavigation()
        binding.bnvHome.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_personal_bottari -> showFragment(BottariFragment::class.java)
                R.id.menu_team_bottari -> showFragment(TeamBottariFragment::class.java)
                R.id.menu_template -> showFragment(TemplateFragment::class.java)
                R.id.menu_more -> showFragment(ProfileFragment::class.java)
            }
            changeToolbarTitle(item)
            true
        }
    }

    private fun showFragment(clazz: Class<out Fragment>) {
        val transaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.fragments.forEach(transaction::hide)
        supportFragmentManager.findFragmentByTag(clazz.name)?.let {
            transaction.show(it)
        } ?: transaction.add(R.id.fcv_home, clazz, null, clazz.name)

        transaction.commit()
    }

    private fun changeToolbarTitle(item: MenuItem) {
        binding.toolbarHome.title = item.title
    }

    companion object {
        private const val KEY_DEEPLINK = "KEY_DEEPLINK"

        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)

        fun newIntentForDeeplink(context: Context) =
            Intent(context, HomeActivity::class.java).apply {
                putExtra(KEY_DEEPLINK, true)
            }
    }
}
