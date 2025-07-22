package com.bottari.domain.model.member

data class Member(val ssaid: String, val nickname: String) {
    init {
        require(ssaid.isNotBlank()) { ERR_MSG_SSAID_BLANK }
        require(nickname.length in MIN_NICKNAME_LENGTH..MAX_NICKNAME_LENGTH) { ERR_MSG_NICKNAME_LENGTH }
    }

    companion object {
        private const val MIN_NICKNAME_LENGTH = 2
        private const val MAX_NICKNAME_LENGTH = 10
        private const val ERR_MSG_SSAID_BLANK = "ssaid는 공백일 수 없습니다."
        private const val ERR_MSG_NICKNAME_LENGTH =
            "닉네임은 최소 $MIN_NICKNAME_LENGTH 최대 $MAX_NICKNAME_LENGTH 글자 가능합니다."
    }
}
