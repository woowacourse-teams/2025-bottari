package com.bottari.repository;

import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Optional<Alarm> findByBottariId(final Long id);

    List<Alarm> findAllByBottariIn(final List<Bottari> bottaries);
}
