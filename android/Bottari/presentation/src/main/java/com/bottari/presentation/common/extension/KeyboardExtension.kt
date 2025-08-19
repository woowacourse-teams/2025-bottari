@file:JvmName("KeyboardExtension")

package com.bottari.presentation.common.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.MainThread
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment

@MainThread
fun Activity.showKeyboard(target: View) {
    if (target.requestFocus().not()) return
    try {
        WindowInsetsControllerCompat(window, target).show(WindowInsetsCompat.Type.ime())
    } catch (_: Throwable) {
        imm().showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
    }
}

@MainThread
fun Activity.hideKeyboard() {
    val current = currentFocus ?: findViewById(android.R.id.content) ?: return
    try {
        WindowInsetsControllerCompat(window, current).hide(WindowInsetsCompat.Type.ime())
    } catch (_: Throwable) {
        imm().hideSoftInputFromWindow(current.windowToken, 0)
    }
}

@MainThread
fun Fragment.showKeyboard(target: View) {
    activity?.showKeyboard(target)
}

@MainThread
fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

@MainThread
fun View.showKeyboardFallback() {
    if (requestFocus().not()) return
    context.imm().showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

@MainThread
fun View.hideKeyboardFallback(clearFocus: Boolean = false) {
    if (clearFocus) clearFocus()
    context.imm().hideSoftInputFromWindow(windowToken, 0)
}

private fun Context.imm(): InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
