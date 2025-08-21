package com.bottari.presentation.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    timeMillis: Long,
    coroutineScope: CoroutineScope,
    block: (T) -> Unit,
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob =
            coroutineScope.launch {
                delay(timeMillis)
                block(param)
            }
    }
}

fun debounce(
    timeMillis: Long,
    coroutineScope: CoroutineScope,
    action: () -> Unit,
): () -> Unit {
    var debounceJob: Job? = null
    return {
        debounceJob?.cancel()
        debounceJob =
            coroutineScope.launch {
                delay(timeMillis)
                action()
            }
    }
}
