package com.bottari.repository;

import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Optional<Alarm> findByBottariId(final Long id);

    List<Alarm> findAllByBottariIn(final List<Bottari> bottaries);

    @Modifying
    @Query("""
            DELETE FROM Alarm a
            WHERE a.bottari.id = :bottariId
            """)
    void deleteByBottariId(final Long bottariId);
}
