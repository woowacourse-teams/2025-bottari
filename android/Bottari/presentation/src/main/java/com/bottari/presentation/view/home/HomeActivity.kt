package com.bottari.presentation.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityHomeBinding
import com.bottari.presentation.view.home.bottari.BottariFragment
import com.bottari.presentation.view.home.profile.ProfileFragment
import com.bottari.presentation.view.home.template.TemplateFragment
import com.bottari.presentation.view.setting.SettingActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupListener()
        if (savedInstanceState == null) {
            binding.bnvHome.selectedItemId = R.id.menu_bottari
        }
    }

    private fun setupUI() {
        setBottomNavigationView()
    }

    private fun setupListener() {
        binding.btnSetting.setOnClickListener { startActivity(SettingActivity.newIntent(this)) }
    }

    private fun setBottomNavigationView() {
        binding.bnvHome.setOnApplyWindowInsetsListener(null)
        binding.bnvHome.setOnItemSelectedListener { item ->
            if (isSameNavItem(item)) return@setOnItemSelectedListener false
            when (item.itemId) {
                R.id.menu_template -> showFragment(TemplateFragment::class.java)
                R.id.menu_bottari -> showFragment(BottariFragment::class.java)
                R.id.menu_profile -> showFragment(ProfileFragment::class.java)
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

    private fun isSameNavItem(item: MenuItem): Boolean = binding.bnvHome.selectedItemId == item.itemId

    private fun changeToolbarTitle(item: MenuItem) {
        binding.toolbarHome.title = item.title
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}
