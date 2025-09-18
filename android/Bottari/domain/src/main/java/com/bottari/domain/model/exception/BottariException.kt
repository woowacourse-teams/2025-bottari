package com.bottari.domain.model.exception

/**
 * 보따리 프로젝트에서 사용되는 최상위 예외 클래스입니다.
 * 모든 커스텀 예외는 [BottariException]을 상속받습니다.
 *
 * @property message 예외 메시지
 */
sealed class BottariException(
    override val message: String,
) : Throwable(message) {
    /**
     * 사용자(Member) 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class MemberException(
        message: String,
    ) : BottariException(message) {
        /** 사용자를 찾을 수 없는 경우 발생합니다. */
        data object NotFoundException : MemberException("사용자를 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }

        /** 이미 존재하는 이름을 등록하려고 할 때 발생합니다. */
        data object NameAlreadyExistsException : MemberException("이미 사용 중인 이름입니다.") {
            private fun readResolve(): Any = NameAlreadyExistsException
        }

        /** 이미 존재하는 ssaid를 등록하려고 할 때 발생합니다. */
        data object IdentifierAlreadyExistsException : MemberException("이미 사용 중인 식별자입니다.") {
            private fun readResolve(): Any = IdentifierAlreadyExistsException
        }

        /** 기존 사용자 이름과 동일한 이름으로 변경할 때 발생합니다. */
        data object NameUnchangedException : MemberException("기존의 사용자 이름과 동일한 이름으로는 변경할 수 없습니다.") {
            private fun readResolve(): Any = NameUnchangedException
        }

        /** 사용자 이름이 너무 짧을 때 발생합니다. */
        data object NameTooShortException : MemberException("사용자 이름이 너무 짧습니다.") {
            private fun readResolve(): Any = NameTooShortException
        }

        /** 사용자 이름이 너무 길 때 발생합니다. */
        data object NameTooLongException : MemberException("사용자 이름이 너무 깁니다.") {
            private fun readResolve(): Any = NameTooLongException
        }

        /** 이름에 비속어가 포함되어 있을 때 발생합니다. */
        data object NameOffensiveException : MemberException("이름에 비속어를 입력할 수 없습니다.") {
            private fun readResolve(): Any = NameOffensiveException
        }

        /** 임시 닉네임 생성에 실패했을 때 발생합니다. */
        data object NameGenerationFailedException :
            MemberException("고유한 임시 닉네임을 생성하는 데 실패했습니다. (관리자 문의 필요)") {
            private fun readResolve(): Any = NameGenerationFailedException
        }

        /** 요청에 사용자 식별자가 존재하지 않을 때 발생합니다. */
        data object IdentifierNotFoundInRequestException :
            MemberException("요청에 사용자 식별자가 존재하지 않습니다.") {
            private fun readResolve(): Any = IdentifierNotFoundInRequestException
        }
    }

    /**
     * 보따리(Bottari) 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class PersonalBottariException(
        message: String,
    ) : BottariException(message) {
        /** 보따리를 찾을 수 없을 때 발생합니다. */
        data object NotFoundException : PersonalBottariException("보따리를 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }

        /** 보따리 소유자가 아닌 경우 접근할 때 발생합니다. */
        data object NotOwnedException : PersonalBottariException("해당 보따리에 접근할 수 있는 권한이 없습니다.") {
            private fun readResolve(): Any = NotOwnedException
        }
    }

    /**
     * 보따리 제목 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class BottariTitleException(
        message: String,
    ) : BottariException(message) {
        /** 기존 제목과 동일한 제목으로 변경하려 할 때 발생합니다. */
        data object UnchangedException :
            BottariTitleException("기존의 보따리 이름과 동일한 이름으로는 변경할 수 없습니다.") {
            private fun readResolve(): Any = UnchangedException
        }

        /** 보따리 제목이 공백일 때 발생합니다. */
        data object BlankException : BottariTitleException("보따리 제목은 공백일 수 없습니다.") {
            private fun readResolve(): Any = BlankException
        }

        /** 보따리 제목이 너무 길 때 발생합니다. */
        data object TooLongException : BottariTitleException("보따리 제목이 너무 깁니다.") {
            private fun readResolve(): Any = TooLongException
        }

        /** 제목에 비속어가 포함되어 있을 때 발생합니다. */
        data object OffensiveException : BottariTitleException("보따리 제목에 비속어를 입력할 수 없습니다.") {
            private fun readResolve(): Any = OffensiveException
        }
    }

    /**
     * 물품 이름(Item Name) 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class ItemNameException(
        message: String,
    ) : BottariException(message) {
        /** 물품명이 공백일 때 발생합니다. */
        data object BlankException : ItemNameException("물품명은 공백일 수 없습니다.") {
            private fun readResolve(): Any = BlankException
        }

        /** 물품명이 너무 길 때 발생합니다. */
        data object TooLongException : ItemNameException("물품명이 너무 깁니다.") {
            private fun readResolve(): Any = TooLongException
        }

        /** 물품명에 비속어가 포함될 때 발생합니다. */
        data object OffensiveException : ItemNameException("물품명에 비속어를 입력할 수 없습니다.") {
            private fun readResolve(): Any = OffensiveException
        }
    }

    /**
     * 보따리 물품(Bottari Item) 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class BottariItemException(
        message: String,
    ) : BottariException(message) {
        /** 보따리 물품을 찾을 수 없을 때 발생합니다. */
        data object NotFoundException : BottariItemException("보따리 물품을 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }

        /** 이미 존재하는 물품을 추가하려 할 때 발생합니다. */
        data object AlreadyExistsException : BottariItemException("이미 존재하는 보따리 물품입니다.") {
            private fun readResolve(): Any = AlreadyExistsException
        }

        /** 요청에 중복된 물품이 있을 때 발생합니다. */
        data object DuplicateInRequestException : BottariItemException("요청에 중복된 보따리 물품이 있습니다.") {
            private fun readResolve(): Any = DuplicateInRequestException
        }

        /** 물품 개수 제한을 초과했을 때 발생합니다. */
        data object MaximumExceededException : BottariItemException("보따리 물품의 최대 개수를 초과했습니다.") {
            private fun readResolve(): Any = MaximumExceededException
        }

        /** 보따리에 존재하지 않는 물품에 접근할 때 발생합니다. */
        data object NotInBottariException : BottariItemException("보따리 안에 없는 물품입니다.") {
            private fun readResolve(): Any = NotInBottariException
        }

        /** 이미 체크된 물품을 다시 체크하려 할 때 발생합니다. */
        data object AlreadyCheckedException : BottariItemException("해당 보따리 물품은 이미 체크되어 있습니다.") {
            private fun readResolve(): Any = AlreadyCheckedException
        }

        /** 이미 체크 해제된 물품을 다시 체크 해제하려 할 때 발생합니다. */
        data object AlreadyUncheckedException :
            BottariItemException("해당 보따리 물품은 이미 체크 해제되어 있습니다.") {
            private fun readResolve(): Any = AlreadyUncheckedException
        }

        /** 체크 상태가 유효하지 않을 때 발생합니다. */
        data object CheckStateInvalidException :
            BottariItemException("해당 보따리 물품의 체크 상태가 이미 요청된 상태입니다.") {
            private fun readResolve(): Any = CheckStateInvalidException
        }

        /** 물품 소유자가 아닌 경우 접근할 때 발생합니다. */
        data object NotOwnedException : BottariItemException("해당 보따리 물품에 접근할 수 있는 권한이 없습니다.") {
            private fun readResolve(): Any = NotOwnedException
        }
    }

    /**
     * 보따리 템플릿 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class BottariTemplateException(
        message: String,
    ) : BottariException(message) {
        /** 템플릿을 찾을 수 없을 때 발생합니다. */
        data object NotFoundException : BottariTemplateException("보따리 템플릿을 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }

        /** 템플릿 소유자가 아닌 경우 접근할 때 발생합니다. */
        data object NotOwnedException : BottariTemplateException("해당 보따리 템플릿에 접근할 수 있는 권한이 없습니다.") {
            private fun readResolve(): Any = NotOwnedException
        }

        /** 템플릿 제목이 공백일 때 발생합니다. */
        data object TitleBlankException : BottariTemplateException("보따리 템플릿 제목은 공백일 수 없습니다.") {
            private fun readResolve(): Any = TitleBlankException
        }

        /** 템플릿 제목이 너무 길 때 발생합니다. */
        data object TitleTooLongException :
            BottariTemplateException("보따리 템플릿 제목이 너무 깁니다. 최대 15자까지 입력 가능합니다.") {
            private fun readResolve(): Any = TitleTooLongException
        }

        /** 제목에 비속어가 포함될 때 발생합니다. */
        data object TitleOffensiveException :
            BottariTemplateException("보따리 템플릿 제목에 비속어를 입력할 수 없습니다.") {
            private fun readResolve(): Any = TitleOffensiveException
        }

        /** 유효하지 않은 정렬 타입일 때 발생합니다. */
        data object InvalidSortTypeException :
            BottariTemplateException("유효하지 않은 보따리 템플릿 정렬 타입입니다.") {
            private fun readResolve(): Any = InvalidSortTypeException
        }

        /** 요청에 중복된 보따리 템플릿 물품이 있을 때 발생합니다. */
        data object ItemDuplicateInRequestException :
            BottariTemplateException("요청에 중복된 보따리 템플릿 물품이 있습니다.") {
            private fun readResolve(): Any = ItemDuplicateInRequestException
        }
    }

    /**
     * 팀 보따리 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class TeamBottariException(
        message: String,
    ) : BottariException(message) {
        /** 팀 보따리를 찾을 수 없을 때 발생합니다. */
        data object NotFoundException : TeamBottariException("팀 보따리를 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }
    }

    /**
     * 팀 보따리 물품 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class TeamBottariItemException(
        message: String,
    ) : BottariException(message) {
        /** 팀 보따리 물품을 찾을 수 없을 때 발생합니다. */
        data object NotFoundException : TeamBottariItemException("팀 보따리 물품을 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }

        /** 물품명이 공백일 때 발생합니다. */
        data object NameBlankException : TeamBottariItemException("팀 보따리 물품명은 공백일 수 없습니다.") {
            private fun readResolve(): Any = NameBlankException
        }

        /** 물품명이 너무 길 때 발생합니다. */
        data object NameTooLongException : TeamBottariItemException("팀 보따리 물품명이 너무 깁니다.") {
            private fun readResolve(): Any = NameTooLongException
        }

        /** 이미 존재하는 팀 보따리 물품일 때 발생합니다. */
        data object AlreadyExistsException : TeamBottariItemException("이미 존재하는 팀 보따리 물품입니다.") {
            private fun readResolve(): Any = AlreadyExistsException
        }

        /** 팀 보따리 물품 소유자가 아닐 때 발생합니다. */
        data object NotOwnedException :
            TeamBottariItemException("해당 팀 보따리 물품에 접근할 수 있는 권한이 없습니다.") {
            private fun readResolve(): Any = NotOwnedException
        }

        /** 이미 체크된 팀 보따리 물품을 다시 체크하려 할 때 발생합니다. */
        data object AlreadyCheckedException :
            TeamBottariItemException("해당 팀 보따리 물품은 이미 체크되어 있습니다.") {
            private fun readResolve(): Any = AlreadyCheckedException
        }

        /** 이미 체크 해제된 팀 보따리 물품을 다시 체크 해제하려 할 때 발생합니다. */
        data object AlreadyUncheckedException :
            TeamBottariItemException("해당 팀 보따리 물품은 이미 체크 해제되어 있습니다.") {
            private fun readResolve(): Any = AlreadyUncheckedException
        }

        /** 담당 물품에 팀원이 지정되지 않았을 때 발생합니다. */
        data object NoAssignedMembersException :
            TeamBottariItemException("팀 보따리 담당 물품에 팀원이 지정되지 않았습니다.") {
            private fun readResolve(): Any = NoAssignedMembersException
        }

        /** 적절하지 않은 아이템 타입일 때 발생합니다. */
        data object InappropriateTypeException :
            TeamBottariItemException("적절하지 않은 아이템 타입입니다.") {
            private fun readResolve(): Any = InappropriateTypeException
        }
    }

    /**
     * 팀 보따리 물품 정보 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class TeamBottariItemInfoException(
        message: String,
    ) : BottariException(message) {
        /** 팀 보따리 물품 정보를 찾을 수 없을 때 발생합니다. */
        data object NotFoundException :
            TeamBottariItemInfoException("팀 보따리 물품 정보를 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }
    }

    /**
     * 팀 멤버 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class TeamMemberException(
        message: String,
    ) : BottariException(message) {
        /** 팀 멤버를 찾을 수 없을 때 발생합니다. */
        data object NotFoundException : TeamMemberException("팀 멤버를 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }

        /** 해당 팀 보따리의 멤버가 아닐 때 발생합니다. */
        data object NotInTeamBottariException : TeamMemberException("해당 팀 보따리의 팀 멤버가 아닙니다.") {
            private fun readResolve(): Any = NotInTeamBottariException
        }

        /** 이미 팀 보따리에 참여한 멤버일 때 발생합니다. */
        data object AlreadyInTeamBottariException :
            TeamMemberException("이미 해당 팀 보따리에 참여한 멤버입니다.") {
            private fun readResolve(): Any = AlreadyInTeamBottariException
        }

        /** 팀 멤버가 모든 팀 보따리 물품을 체크했을 때 발생합니다. */
        data object AlreadyCheckedAllException :
            TeamMemberException("해당 팀 멤버는 모든 팀 보따리 물품을 체크했습니다.") {
            private fun readResolve(): Any = AlreadyCheckedAllException
        }

        /** 본인에게 보채기 알림을 보낼 수 없을 때 발생합니다. */
        data object CannotSendRemindToSelfException :
            TeamMemberException("본인에게 보채기 알림을 보낼 수 없습니다.") {
            private fun readResolve(): Any = CannotSendRemindToSelfException
        }
    }

    /**
     * 알람(Alarm) 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class AlarmException(
        message: String,
    ) : BottariException(message) {
        /** 알람을 찾을 수 없을 때 발생합니다. */
        data object NotFoundException : AlarmException("알람을 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }

        /** 이미 활성화된 알람을 다시 활성화하려 할 때 발생합니다. */
        data object AlreadyActiveException : AlarmException("알람이 이미 활성화되어 있습니다.") {
            private fun readResolve(): Any = AlreadyActiveException
        }

        /** 이미 비활성화된 알람을 다시 비활성화하려 할 때 발생합니다. */
        data object AlreadyInactiveException : AlarmException("알람이 이미 비활성화되어 있습니다.") {
            private fun readResolve(): Any = AlreadyInactiveException
        }

        /** 루틴 알람이 설정되지 않으면 위치 알람을 설정할 수 없을 때 발생합니다. */
        data object LocationRequiresRoutineException :
            AlarmException("루틴 알람이 설정되지 않으면 위치 알람을 설정할 수 없습니다.") {
            private fun readResolve(): Any = LocationRequiresRoutineException
        }
    }

    /**
     * 신고(Report) 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class ReportException(
        message: String,
    ) : BottariException(message) {
        /** 이미 신고한 기록이 있을 때 발생합니다. */
        data object AlreadyExistsException : ReportException("이미 해당 템플릿에 대한 신고 기록이 있습니다.") {
            private fun readResolve(): Any = AlreadyExistsException
        }
    }

    /**
     * FCM(Firebase Cloud Messaging) 관련 예외를 나타내는 최상위 클래스입니다.
     */
    sealed class FcmException(
        message: String,
    ) : BottariException(message) {
        /** FCM 토큰 정보가 존재하지 않을 때 발생합니다. */
        data object NotFoundException : FcmException("FCM 토큰 정보를 찾을 수 없습니다.") {
            private fun readResolve(): Any = NotFoundException
        }

        /** FCM 토큰이 유효하지 않을 때 발생합니다. */
        data object InvalidTokenException : FcmException("유효하지 않은 FCM 토큰입니다.") {
            private fun readResolve(): Any = InvalidTokenException
        }
    }

    /** 리소스를 생성 후 생성된 리소스의 아이디를 전달받지 못한 경우 발생합니다. */
    data object NotFoundCreatedIdException : BottariException("생성된 ID를 찾을 수 없습니다.") {
        private fun readResolve(): Any = NotFoundCreatedIdException
    }
}
