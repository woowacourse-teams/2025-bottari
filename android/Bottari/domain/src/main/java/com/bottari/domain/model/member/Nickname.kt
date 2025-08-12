package com.bottari.domain.model.member

@JvmInline
value class Nickname(
    val value: String,
) {
    init {
        require(value.length in MIN_NICKNAME_LENGTH..MAX_NICKNAME_LENGTH) { NICKNAME_LENGTH_ERROR }
    }

    companion object {
        private const val MIN_NICKNAME_LENGTH = 2
        private const val MAX_NICKNAME_LENGTH = 10
        private const val NICKNAME_LENGTH_ERROR =
            "닉네임은 최소 ${MIN_NICKNAME_LENGTH}자에서 최대 ${MAX_NICKNAME_LENGTH}자까지 가능해요."
    }
}
