package com.bottari.presentation.common.event

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

class SingleLiveEvent<T> : MutableLiveData<Event<T>>() {
    fun observeEvent(
        owner: LifecycleOwner,
        action: (T) -> Unit,
    ) {
        observe(owner) { event ->
            event.getContentIfNotHandled()?.let(action)
        }
    }

    fun observeAlways(
        owner: LifecycleOwner,
        action: (T) -> Unit,
    ) {
        observe(owner) { event ->
            event.peekContent()?.let(action)
        }
    }

    fun emit(value: T?) {
        this.value = Event(value)
    }

    fun postEmit(value: T?) {
        postValue(Event(value))
    }
}
