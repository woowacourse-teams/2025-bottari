package com.bottari.core.domain.model.bottari.template

import com.bottari.core.domain.extension.isEnglishLetter
import com.bottari.core.domain.extension.isHangulJamoAny
import com.bottari.core.domain.extension.isNotHangulSyllable

@JvmInline
value class HashtagName private constructor(
    val value: String,
) {
    companion object {
        private const val MIN_LENGTH = 2
        private const val MAX_LENGTH = 8
        val UNKNOWN_HASHTAG_NAME = HashtagName("unknown")

        fun create(raw: String): Result<HashtagName> {
            val error = validate(raw)
            if (error != null) return Result.failure(IllegalArgumentException(error.message))
            return Result.success(HashtagName(raw))
        }

        fun validate(raw: String): HashtagNameError? {
            // 1) 길이 제약: TooLong
            if (raw.length > MAX_LENGTH) return HashtagNameError.TooLong

            // 2) 공백
            if (raw.any { it.isWhitespace() }) return HashtagNameError.ContainsWhitespace

            // 3) 숫자
            if (raw.any { it.isDigit() }) return HashtagNameError.ContainsDigit

            // 4) 영어
            if (raw.any { it.isEnglishLetter() }) return HashtagNameError.ContainsEnglish

            // 5) 한글 자모
            if (raw.any { it.isHangulJamoAny() }) return HashtagNameError.ContainsHangulJamo

            // 6) 완성형 한글 외 문자 금지
            if (raw.any { it.isNotHangulSyllable() }) return HashtagNameError.ContainsNonHangul

            // 7) 길이 제약: TooShort
            if (raw.length < MIN_LENGTH) return HashtagNameError.TooShort

            return null
        }
    }
}

sealed interface HashtagNameError {
    val message: String

    data object TooShort : HashtagNameError {
        override val message = "해시태그는 최소 2글자 이상이어야 합니다."
    }

    data object TooLong : HashtagNameError {
        override val message = "해시태그는 최대 8글자까지 가능합니다."
    }

    data object ContainsWhitespace : HashtagNameError {
        override val message = "공백은 포함될 수 없습니다."
    }

    data object ContainsDigit : HashtagNameError {
        override val message = "숫자는 포함될 수 없습니다."
    }

    data object ContainsEnglish : HashtagNameError {
        override val message = "영문자는 포함될 수 없습니다."
    }

    data object ContainsHangulJamo : HashtagNameError {
        override val message =
            "한글 자모(ㄱ-ㅎ, ㅏ-ㅣ 등)는 사용할 수 없습니다. 완성형 한글(가-힣)만 허용됩니다."
    }

    data object ContainsNonHangul : HashtagNameError {
        override val message =
            "특수문자 또는 허용되지 않는 문자가 포함되어 있습니다. 완성형 한글(가-힣)만 사용할 수 있습니다."
    }
}
