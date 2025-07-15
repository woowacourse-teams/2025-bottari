package com.bottari.repository;

import com.bottari.domain.Bottari;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottariRepository extends JpaRepository<Bottari, Long> {
}
