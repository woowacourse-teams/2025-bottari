package com.bottari.repository;

import com.bottari.domain.Bottari;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottariRepository extends JpaRepository<Bottari, Long> {

    List<Bottari> findAllByMemberIdOrderByCreatedAtDesc(final Long memberId);
}
