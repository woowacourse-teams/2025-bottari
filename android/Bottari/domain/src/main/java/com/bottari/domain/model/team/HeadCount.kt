package com.bottari.domain.model.team

data class HeadCount(
    val value: Int,
    val maxValue: Int = MAXIMUM_HEAD_COUNT,
) {
    init {
        require(value in MINIMUM_HEAD_COUNT..MAXIMUM_HEAD_COUNT) { HEAD_COUNT_ERROR }
    }

    companion object {
        private const val MINIMUM_HEAD_COUNT = 1
        private const val MAXIMUM_HEAD_COUNT = 10
        private const val HEAD_COUNT_ERROR =
            "팀의 인원은 최소 ${MINIMUM_HEAD_COUNT}명에서 최대 ${MAXIMUM_HEAD_COUNT}명까지 가능해요."
    }
}
