package com.bottari.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bottari.logger.BottariLogger
import com.bottari.logger.LogEventHelper
import com.bottari.presentation.view.common.LoadingDialog

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> VB,
) : Fragment() {
    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog() }
    private var enterTime: Long = System.currentTimeMillis()
    private val stayDuration: Long get() = System.currentTimeMillis() - enterTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BottariLogger.lifecycle(javaClass.simpleName)
        LogEventHelper.logScreenEnter(javaClass.simpleName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        BottariLogger.lifecycle(javaClass.simpleName)
        _binding = bindingFactory(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        BottariLogger.lifecycle(javaClass.simpleName)
        setupWindowInsets()
    }

    override fun onStart() {
        super.onStart()
        BottariLogger.lifecycle(javaClass.simpleName)
        enterTime = System.currentTimeMillis()
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
        LogEventHelper.logScreenExit(javaClass.simpleName, stayDuration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BottariLogger.lifecycle(javaClass.simpleName)
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        BottariLogger.lifecycle(javaClass.simpleName)
    }

    protected fun toggleLoadingIndicator(isShow: Boolean) {
        if (isShow) {
            if (loadingDialog.isAdded || loadingDialog.isVisible || loadingDialog.isRemoving) return
            if (childFragmentManager.isStateSaved) return

            loadingDialog.show(childFragmentManager, LoadingDialog::class.java.name)
            return
        }

        if (!loadingDialog.isAdded) return
        loadingDialog.dismissAllowingStateLoss()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val bottomInset = calculateBottomInset(insets, imeVisible)
            view.setPadding(0, 0, 0, bottomInset)
            insets
        }
    }

    private fun calculateBottomInset(
        insets: WindowInsetsCompat,
        imeVisible: Boolean,
    ): Int {
        if (!imeVisible) return DEFAULT_BOTTOM_INSET

        val imeInset = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        val systemBarInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

        return (imeInset - systemBarInset).coerceAtLeast(0)
    }

    companion object {
        private const val DEFAULT_BOTTOM_INSET = 0
    }
}
