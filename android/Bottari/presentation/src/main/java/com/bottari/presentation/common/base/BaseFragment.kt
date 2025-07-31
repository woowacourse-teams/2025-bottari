package com.bottari.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.bottari.presentation.common.component.LoadingDialog
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> VB,
) : Fragment() {
    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = bindingFactory(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupWindowInsets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showSnackbar(
        @StringRes message: Int,
        duration: Int = Snackbar.LENGTH_SHORT,
    ) {
        view?.let { Snackbar.make(it, message, duration).show() }
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
