package com.bottari.fcm.repository;

import com.bottari.fcm.domain.FcmToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    Optional<FcmToken> findByMemberSsaid(final String ssaid);

    Optional<FcmToken> findByMemberId(final Long memberId);

    List<FcmToken> findByMemberIdIn(final List<Long> memberIds);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE FcmToken f SET f.deletedAt = CURRENT_TIMESTAMP WHERE f.id IN :ids")
    void deleteByIds(final List<Long> ids);
}
