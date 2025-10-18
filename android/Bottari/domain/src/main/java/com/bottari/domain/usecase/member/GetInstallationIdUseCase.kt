package com.bottari.domain.usecase.member

import com.bottari.domain.repository.MemberRepository
import javax.inject.Inject

class GetInstallationIdUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(): Result<String> = memberRepository.getInstallationId()
}
