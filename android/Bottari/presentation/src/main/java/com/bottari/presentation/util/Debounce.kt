package com.bottari.presentation.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    job: Job?,
    timeMillis: Long,
    coroutineScope: CoroutineScope,
    block: (T) -> Unit,
): (T) -> Unit {
    var debounceJob: Job? = job
    return { param: T ->
        debounceJob?.cancel()
        debounceJob =
            coroutineScope.launch {
                delay(timeMillis)
                block(param)
            }
    }
}
