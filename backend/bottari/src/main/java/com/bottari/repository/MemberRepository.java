package com.bottari.repository;

import com.bottari.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsBySsaid(final String ssaid);
}
