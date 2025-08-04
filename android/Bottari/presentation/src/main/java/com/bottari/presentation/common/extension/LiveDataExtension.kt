package com.bottari.presentation.common.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

inline fun <T> MutableLiveData<T>.update(block: T.() -> T) {
    value = value?.block()
}

fun <T> LiveData<T>.observeOnce(
    owner: LifecycleOwner,
    observer: Observer<T>,
) {
    observe(
        owner,
        object : Observer<T> {
            override fun onChanged(value: T) {
                observer.onChanged(value)
                removeObserver(this)
            }
        },
    )
}
