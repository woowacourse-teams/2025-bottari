package com.bottari.presentation.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

inline fun <T> MutableLiveData<T>.update(block: T.() -> T) {
    value = value?.block()
}

fun <T> LiveData<T>.requireValue(): T = requireNotNull(value)
