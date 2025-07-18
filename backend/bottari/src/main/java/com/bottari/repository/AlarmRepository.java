package com.bottari.repository;

import com.bottari.domain.Alarm;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Optional<Alarm> findByBottariId(final Long id);
}
