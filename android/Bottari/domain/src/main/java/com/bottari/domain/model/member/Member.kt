package com.bottari.domain.model.member

data class Member(
    val ssaid: String,
    val nickname: String = "",
) {
    init {
        require(ssaid.isNotBlank()) { SSAID_BLANK_ERROR }
        require(nickname.length in MIN_NICKNAME_LENGTH..MAX_NICKNAME_LENGTH) { NICKNAME_LENGTH_ERROR }
    }

    companion object {
        private const val MIN_NICKNAME_LENGTH = 3
        private const val MAX_NICKNAME_LENGTH = 10
        private const val SSAID_BLANK_ERROR = "ssaid는 공백일 수 없습니다."
        private const val NICKNAME_LENGTH_ERROR =
            "닉네임은 최소 $MIN_NICKNAME_LENGTH 최대 $MAX_NICKNAME_LENGTH 글자 가능합니다."
    }
}
