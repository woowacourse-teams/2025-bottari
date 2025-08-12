package com.bottari.member.repository;

import com.bottari.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsBySsaid(final String ssaid);

    Optional<Member> findBySsaid(final String ssaid);

    boolean existsByName(final String name);
}
