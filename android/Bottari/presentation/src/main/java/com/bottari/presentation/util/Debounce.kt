package com.bottari.presentation.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
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

/**
 * suspend 요청을 디바운스 처리하는 범용 확장함수
 * - 버퍼 초과 없이 안전하게 최신 요청만 실행
 * - collectLatest를 사용해 이전 요청 취소
 *
 * @param debounceMillis 디바운스 시간 (기본 500ms)
 * @param scope CoroutineScope (보통 viewModelScope)
 * @param action 실제 suspend 요청
 */
@OptIn(FlowPreview::class)
fun <T> CoroutineScope.debounce(
    debounceMillis: Long = 500,
    action: suspend (T) -> Unit,
): (T) -> Unit {
    val flow =
        MutableSharedFlow<T>(
            replay = 0,
            extraBufferCapacity = 0,
        )

    launch {
        flow
            .debounce(debounceMillis)
            .collectLatest { value ->
                action(value)
            }
    }

    return { value ->
        launch {
            flow.emit(value)
        }
    }
}
