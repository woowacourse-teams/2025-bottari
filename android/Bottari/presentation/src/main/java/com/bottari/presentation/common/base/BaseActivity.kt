package com.bottari.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.viewbinding.ViewBinding
import com.bottari.logger.BottariLogger
import com.bottari.logger.LogEventHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseActivity<VB : ViewBinding>(
    private val bindingFactory: (LayoutInflater) -> VB,
) : AppCompatActivity() {
    protected lateinit var binding: VB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BottariLogger.lifecycle(javaClass.simpleName)
        LogEventHelper.logScreenEnter(javaClass.simpleName)

        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        setWindowInsets()
        setupStatusBar()
        setupNavigationBar()
    }

    override fun onStart() {
        super.onStart()
        BottariLogger.lifecycle(javaClass.simpleName)
    }

    override fun onResume() {
        super.onResume()
        BottariLogger.lifecycle(javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        BottariLogger.lifecycle(javaClass.simpleName)
    }

    override fun onStop() {
        super.onStop()
        BottariLogger.lifecycle(javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        BottariLogger.lifecycle(javaClass.simpleName)
    }

    private fun setWindowInsets() {
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val bottomPadding =
                if (hasBottomNavigationView()) DEFAULT_BOTTOM_INSET else systemBars.bottom

            view.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
            insets
        }
    }

    private fun setupStatusBar() {
        WindowCompat
            .getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true
    }

    private fun setupNavigationBar() {
        WindowCompat
            .getInsetsController(window, window.decorView)
            .isAppearanceLightNavigationBars = true
    }

    private fun hasBottomNavigationView(): Boolean =
        (binding.root as? ViewGroup)
            ?.children
            ?.filterIsInstance<BottomNavigationView>()
            ?.count() != 0

    companion object {
        private const val DEFAULT_BOTTOM_INSET = 0
    }
}
