package com.bottari.domain.model.exception

/**
 * 보따리 프로젝트에서 사용되는 최상위 예외 클래스입니다.
 *
 * 모든 커스텀 예외는 [BottariException]을 상속받으며,
 * 도메인 전반에서 발생할 수 있는 예외 상황을 표현합니다.
 *
 * @property message 예외 메시지
 */
sealed class BottariException(
    override val message: String,
) : Throwable(message) {
    /**
     * 요청한 대상을 찾을 수 없을 때 발생하는 예외입니다.
     *
     * 예: 존재하지 않는 사용자, 게시글, 리소스 등을 조회한 경우
     */
    data object NotFoundException : BottariException("대상을 찾을 수 없습니다.") {
        private fun readResolve(): Any = NotFoundException
    }

    /**
     * 이미 동일한 값이 존재하여 중복이 발생했을 때 발생하는 예외입니다.
     *
     * 예: 회원가입 시 동일한 닉네임/이메일이 이미 존재하는 경우
     */
    data object DuplicatedException : BottariException("중복된 값이 존재합니다.") {
        private fun readResolve(): Any = DuplicatedException
    }

    /**
     * 입력된 값에 비속어가 포함되어 있을 때 발생하는 예외입니다.
     *
     * 예: 닉네임, 게시글 작성 시 금칙어가 포함된 경우
     */
    data object OffensiveException : BottariException("비속어가 포함되어 있습니다.") {
        private fun readResolve(): Any = OffensiveException
    }

    /**
     * 유효하지 않은 값이 전달되었을 때 발생하는 예외입니다.
     *
     * 예: 형식에 맞지 않는 이메일, 범위를 벗어난 값 등이 전달된 경우
     */
    data object InvalidException : BottariException("유효하지 않은 값입니다.") {
        private fun readResolve(): Any = InvalidException
    }

    /**
     * 사용자가 권한이 없는 작업을 시도했을 때 발생하는 예외입니다.
     *
     * 예: 권한 없는 사용자가 리소스를 수정/삭제하려는 경우
     */
    data object PermissionException : BottariException("권한이 없습니다.") {
        private fun readResolve(): Any = PermissionException
    }

    /**
     * 허용된 최댓값을 초과했을 때 발생하는 예외입니다.
     *
     * 예: 업로드 가능한 파일 개수, 입력 길이 제한을 초과한 경우
     */
    data object MaximumExceededException : BottariException("최댓값을 초과했습니다.") {
        private fun readResolve(): Any = MaximumExceededException
    }

    /**
     * FCM(Firebase Cloud Messaging)과 관련된 문제가 발생했을 때 발생하는 예외입니다.
     *
     * 예: FCM 토큰 등록 실패, 알림 전송 실패 등
     */
    data object FcmException : BottariException("FCM 문제가 발생했습니다.") {
        private fun readResolve(): Any = FcmException
    }

    /**
     * 리소스를 생성한 후, 생성된 리소스의 ID를 전달받지 못했을 때 발생하는 예외입니다.
     *
     * 예: 서버에서 생성 요청은 성공했으나 ID를 응답하지 않은 경우
     */
    data object NotFoundCreatedIdException : BottariException("생성된 ID를 찾을 수 없습니다.") {
        private fun readResolve(): Any = NotFoundCreatedIdException
    }
}
