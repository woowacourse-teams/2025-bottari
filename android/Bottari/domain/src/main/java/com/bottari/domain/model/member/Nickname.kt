package com.bottari.domain.model.member

import com.bottari.domain.model.exception.BottariException

@JvmInline
value class Nickname(
    val value: String,
) {
    init {
        if (value.length !in MIN_NICKNAME_LENGTH..MAX_NICKNAME_LENGTH) {
            throw BottariException.InvalidException
        }
    }

    companion object {
        private const val MIN_NICKNAME_LENGTH = 2
        private const val MAX_NICKNAME_LENGTH = 10
    }
}
