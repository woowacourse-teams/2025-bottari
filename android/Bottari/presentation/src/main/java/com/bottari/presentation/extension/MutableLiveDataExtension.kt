package com.bottari.presentation.extension

import androidx.lifecycle.MutableLiveData

inline fun <T> MutableLiveData<T>.update(block: T.() -> T) {
    value = value?.block()
}
