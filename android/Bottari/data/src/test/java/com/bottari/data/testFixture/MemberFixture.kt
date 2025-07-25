package com.bottari.data.testFixture

import com.bottari.domain.model.member.Member

fun memberFixture(
    ssaid: String = "ssaid123",
    nickname: String = "닉네임",
): Member = Member(ssaid = ssaid, nickname = nickname)
