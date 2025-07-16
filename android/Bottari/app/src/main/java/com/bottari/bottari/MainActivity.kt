package com.bottari.bottari

import android.content.Intent
import android.os.Bundle
import com.bottari.bottari.databinding.ActivityMainBinding
import com.bottari.presentation.base.BaseActivity
import com.bottari.presentation.view.home.HomeActivity

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
