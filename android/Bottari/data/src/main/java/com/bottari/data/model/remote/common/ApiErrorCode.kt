package com.bottari.data.model.remote.common

import com.bottari.domain.model.exception.BottariException

enum class ApiErrorCode {
    // ===== MEMBER 관련 =====
    MEMBER_NOT_FOUND,
    MEMBER_NAME_ALREADY_EXISTS,
    MEMBER_SSAID_ALREADY_EXISTS,
    MEMBER_NAME_UNCHANGED,
    MEMBER_NAME_TOO_SHORT,
    MEMBER_NAME_TOO_LONG,
    MEMBER_NAME_OFFENSIVE,
    MEMBER_NAME_GENERATION_FAILED,
    MEMBER_IDENTIFIER_NOT_FOUND_IN_REQUEST,

    // ===== BOTTARI 관련 =====
    BOTTARI_NOT_FOUND,
    BOTTARI_NOT_OWNED,

    // ===== BOTTARI TITLE 관련 =====
    BOTTARI_TITLE_UNCHANGED,
    BOTTARI_TITLE_BLANK,
    BOTTARI_TITLE_TOO_LONG,
    BOTTARI_TITLE_OFFENSIVE,

    // ===== ITEM_NAME 관련 =====
    ITEM_NAME_BLANK,
    ITEM_NAME_TOO_LONG,
    ITEM_NAME_OFFENSIVE,

    // ===== BOTTARI_ITEM 관련 =====
    BOTTARI_ITEM_NOT_FOUND,
    BOTTARI_ITEM_ALREADY_EXISTS,
    BOTTARI_ITEM_DUPLICATE_IN_REQUEST,
    BOTTARI_ITEM_MAXIMUM_EXCEEDED,
    BOTTARI_ITEM_NOT_IN_BOTTARI,
    BOTTARI_ITEM_ALREADY_CHECKED,
    BOTTARI_ITEM_ALREADY_UNCHECKED,
    BOTTARI_ITEM_CHECK_STATE_INVALID,
    BOTTARI_ITEM_NOT_OWNED,

    // ===== BOTTARI_TEMPLATE 관련 =====
    BOTTARI_TEMPLATE_NOT_FOUND,
    BOTTARI_TEMPLATE_NOT_OWNED,
    BOTTARI_TEMPLATE_TITLE_BLANK,
    BOTTARI_TEMPLATE_TITLE_TOO_LONG,
    BOTTARI_TEMPLATE_TITLE_OFFENSIVE,
    BOTTARI_TEMPLATE_INVALID_SORT_TYPE,

    // ===== BOTTARI_TEMPLATE_ITEM 관련 =====
    BOTTARI_TEMPLATE_ITEM_DUPLICATE_IN_REQUEST,

    // ===== TEAM_BOTTARI 관련 =====
    TEAM_BOTTARI_NOT_FOUND,

    // ===== TEAM_BOTTARI_ITEM 관련 =====
    TEAM_BOTTARI_ITEM_NOT_FOUND,
    TEAM_BOTTARI_ITEM_NAME_BLANK,
    TEAM_BOTTARI_ITEM_NAME_TOO_LONG,
    TEAM_BOTTARI_ITEM_ALREADY_EXISTS,
    TEAM_BOTTARI_ITEM_NOT_OWNED,
    TEAM_BOTTARI_ITEM_ALREADY_CHECKED,
    TEAM_BOTTARI_ITEM_ALREADY_UNCHECKED,
    TEAM_BOTTARI_ITEM_NO_ASSIGNED_MEMBERS,
    TEAM_BOTTARI_ITEM_INAPPROPRIATE_TYPE,

    // ===== TEAM_BOTTARI_ITEM_INFO 관련 =====
    TEAM_BOTTARI_ITEM_INFO_NOT_FOUND,

    // ===== TEAM_MEMBER 관련 =====
    TEAM_MEMBER_NOT_FOUND,
    MEMBER_NOT_IN_TEAM_BOTTARI,
    MEMBER_ALREADY_IN_TEAM_BOTTARI,
    TEAM_MEMBER_ALREADY_CHECKED_ALL,
    CANNOT_SEND_REMIND_TO_SELF,

    // ===== ALARM 관련 =====
    ALARM_NOT_FOUND,
    ALARM_ALREADY_ACTIVE,
    ALARM_ALREADY_INACTIVE,
    ALARM_LOCATION_REQUIRES_ROUTINE,

    // ===== REPORT 관련 =====
    REPORT_ALREADY_EXISTS,

    // ===== FCM 관련 =====
    FCM_TOKEN_NOT_FOUND,
    FCM_INVALID_TOKEN,
    ;

    fun toException(): BottariException =
        when (this) {
            MEMBER_NOT_FOUND -> BottariException.MemberException.NotFoundException
            MEMBER_NAME_ALREADY_EXISTS -> BottariException.MemberException.NameAlreadyExistsException
            MEMBER_SSAID_ALREADY_EXISTS -> BottariException.MemberException.IdentifierAlreadyExistsException
            MEMBER_NAME_UNCHANGED -> BottariException.MemberException.NameUnchangedException
            MEMBER_NAME_TOO_SHORT -> BottariException.MemberException.NameTooShortException
            MEMBER_NAME_TOO_LONG -> BottariException.MemberException.NameTooLongException
            MEMBER_NAME_OFFENSIVE -> BottariException.MemberException.NameOffensiveException
            MEMBER_NAME_GENERATION_FAILED -> BottariException.MemberException.NameGenerationFailedException
            MEMBER_IDENTIFIER_NOT_FOUND_IN_REQUEST -> BottariException.MemberException.IdentifierNotFoundInRequestException

            BOTTARI_NOT_FOUND -> BottariException.PersonalBottariException.NotFoundException
            BOTTARI_NOT_OWNED -> BottariException.PersonalBottariException.NotOwnedException

            BOTTARI_TITLE_UNCHANGED -> BottariException.BottariTitleException.UnchangedException
            BOTTARI_TITLE_BLANK -> BottariException.BottariTitleException.BlankException
            BOTTARI_TITLE_TOO_LONG -> BottariException.BottariTitleException.TooLongException
            BOTTARI_TITLE_OFFENSIVE -> BottariException.BottariTitleException.OffensiveException

            ITEM_NAME_BLANK -> BottariException.ItemNameException.BlankException
            ITEM_NAME_TOO_LONG -> BottariException.ItemNameException.TooLongException
            ITEM_NAME_OFFENSIVE -> BottariException.ItemNameException.OffensiveException

            BOTTARI_ITEM_NOT_FOUND -> BottariException.BottariItemException.NotFoundException
            BOTTARI_ITEM_ALREADY_EXISTS -> BottariException.BottariItemException.AlreadyExistsException
            BOTTARI_ITEM_DUPLICATE_IN_REQUEST -> BottariException.BottariItemException.DuplicateInRequestException
            BOTTARI_ITEM_MAXIMUM_EXCEEDED -> BottariException.BottariItemException.MaximumExceededException
            BOTTARI_ITEM_NOT_IN_BOTTARI -> BottariException.BottariItemException.NotInBottariException
            BOTTARI_ITEM_ALREADY_CHECKED -> BottariException.BottariItemException.AlreadyCheckedException
            BOTTARI_ITEM_ALREADY_UNCHECKED -> BottariException.BottariItemException.AlreadyUncheckedException
            BOTTARI_ITEM_CHECK_STATE_INVALID -> BottariException.BottariItemException.CheckStateInvalidException
            BOTTARI_ITEM_NOT_OWNED -> BottariException.BottariItemException.NotOwnedException

            BOTTARI_TEMPLATE_NOT_FOUND -> BottariException.BottariTemplateException.NotFoundException
            BOTTARI_TEMPLATE_NOT_OWNED -> BottariException.BottariTemplateException.NotOwnedException
            BOTTARI_TEMPLATE_TITLE_BLANK -> BottariException.BottariTemplateException.TitleBlankException
            BOTTARI_TEMPLATE_TITLE_TOO_LONG -> BottariException.BottariTemplateException.TitleTooLongException
            BOTTARI_TEMPLATE_TITLE_OFFENSIVE -> BottariException.BottariTemplateException.TitleOffensiveException
            BOTTARI_TEMPLATE_INVALID_SORT_TYPE -> BottariException.BottariTemplateException.InvalidSortTypeException
            BOTTARI_TEMPLATE_ITEM_DUPLICATE_IN_REQUEST -> BottariException.BottariTemplateException.ItemDuplicateInRequestException

            TEAM_BOTTARI_NOT_FOUND -> BottariException.TeamBottariException.NotFoundException

            TEAM_BOTTARI_ITEM_NOT_FOUND -> BottariException.TeamBottariItemException.NotFoundException
            TEAM_BOTTARI_ITEM_NAME_BLANK -> BottariException.TeamBottariItemException.NameBlankException
            TEAM_BOTTARI_ITEM_NAME_TOO_LONG -> BottariException.TeamBottariItemException.NameTooLongException
            TEAM_BOTTARI_ITEM_ALREADY_EXISTS -> BottariException.TeamBottariItemException.AlreadyExistsException
            TEAM_BOTTARI_ITEM_NOT_OWNED -> BottariException.TeamBottariItemException.NotOwnedException
            TEAM_BOTTARI_ITEM_ALREADY_CHECKED -> BottariException.TeamBottariItemException.AlreadyCheckedException
            TEAM_BOTTARI_ITEM_ALREADY_UNCHECKED -> BottariException.TeamBottariItemException.AlreadyUncheckedException
            TEAM_BOTTARI_ITEM_NO_ASSIGNED_MEMBERS -> BottariException.TeamBottariItemException.NoAssignedMembersException
            TEAM_BOTTARI_ITEM_INAPPROPRIATE_TYPE -> BottariException.TeamBottariItemException.InappropriateTypeException

            TEAM_BOTTARI_ITEM_INFO_NOT_FOUND -> BottariException.TeamBottariItemInfoException.NotFoundException

            TEAM_MEMBER_NOT_FOUND -> BottariException.TeamMemberException.NotFoundException
            MEMBER_NOT_IN_TEAM_BOTTARI -> BottariException.TeamMemberException.NotInTeamBottariException
            MEMBER_ALREADY_IN_TEAM_BOTTARI -> BottariException.TeamMemberException.AlreadyInTeamBottariException
            TEAM_MEMBER_ALREADY_CHECKED_ALL -> BottariException.TeamMemberException.AlreadyCheckedAllException
            CANNOT_SEND_REMIND_TO_SELF -> BottariException.TeamMemberException.CannotSendRemindToSelfException

            ALARM_NOT_FOUND -> BottariException.AlarmException.NotFoundException
            ALARM_ALREADY_ACTIVE -> BottariException.AlarmException.AlreadyActiveException
            ALARM_ALREADY_INACTIVE -> BottariException.AlarmException.AlreadyInactiveException
            ALARM_LOCATION_REQUIRES_ROUTINE -> BottariException.AlarmException.LocationRequiresRoutineException

            REPORT_ALREADY_EXISTS -> BottariException.ReportException.AlreadyExistsException

            FCM_TOKEN_NOT_FOUND -> BottariException.FcmException.NotFoundException
            FCM_INVALID_TOKEN -> BottariException.FcmException.InvalidTokenException
        }
}
