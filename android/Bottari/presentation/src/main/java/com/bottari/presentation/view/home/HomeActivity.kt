package com.bottari.presentation.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.common.extension.applyWindowInsetsWithBottomNavigation
import com.bottari.presentation.databinding.ActivityHomeBinding
import com.bottari.presentation.view.home.more.MoreFragment
import com.bottari.presentation.view.home.personal.BottariFragment
import com.bottari.presentation.view.home.team.TeamBottariFragment
import com.bottari.presentation.view.home.template.TemplateFragment
import com.google.android.material.snackbar.Snackbar

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    private val deeplinkFlag: Boolean by lazy { intent.getBooleanExtra(KEY_DEEPLINK, false) }
    private var isBackPressedOnce: Boolean = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI(savedInstanceState)
        setupListener()
    }

    private fun setupUI(savedInstanceState: Bundle?) {
        setBottomNavigationView()
        if (savedInstanceState == null) {
            binding.bnvHome.selectedItemId =
                if (deeplinkFlag) R.id.menu_team_bottari else R.id.menu_personal_bottari
            return
        }
        binding.bnvHome.menu.findItem(binding.bnvHome.selectedItemId)?.let {
            changeToolbarTitle(it)
        }
    }

    private fun setupListener() {
        onBackPressedDispatcher.addCallback(this) {
            showExitSnackbar()
        }
    }

    private fun setBottomNavigationView() {
        binding.root.applyWindowInsetsWithBottomNavigation()
        binding.bnvHome.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_personal_bottari -> showFragment(BottariFragment::class.java)
                R.id.menu_team_bottari -> showFragment(TeamBottariFragment::class.java)
                R.id.menu_template -> showFragment(TemplateFragment::class.java)
                R.id.menu_more -> showFragment(MoreFragment::class.java)
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

    private fun showExitSnackbar() {
        if (isBackPressedOnce) {
            finish()
            return
        }
        isBackPressedOnce = true
        Snackbar.make(binding.root, getString(R.string.bottari_home_exit_confirm_text), Snackbar.LENGTH_SHORT).show()
        handler.postDelayed({
            isBackPressedOnce = false
        }, EXIT_DELAY_TIME)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val KEY_DEEPLINK = "KEY_DEEPLINK"
        private const val EXIT_DELAY_TIME = 2000L

        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)

        fun newIntentForDeeplink(context: Context) =
            Intent(context, HomeActivity::class.java).apply {
                putExtra(KEY_DEEPLINK, true)
            }
    }
}
