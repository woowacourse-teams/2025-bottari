package com.bottari.presentation.view.home

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.bottari.presentation.R
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.databinding.ActivityHomeBinding
import com.bottari.presentation.view.home.bottari.BottariFragment
import com.bottari.presentation.view.home.market.MarketFragment
import com.bottari.presentation.view.home.profile.ProfileFragment

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNavigationView()
        if (savedInstanceState == null) {
            binding.bnvHome.selectedItemId = R.id.menu_bottari
        }
    }

    private fun setBottomNavigationView() {
        binding.bnvHome.setOnItemSelectedListener { item ->
            if (isSameNavItem(item)) return@setOnItemSelectedListener false
            when (item.itemId) {
                R.id.menu_market -> showFragment(MarketFragment::class.java)
                R.id.menu_bottari -> showFragment(BottariFragment::class.java)
                R.id.menu_profile -> showFragment(ProfileFragment::class.java)
            }
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
}
