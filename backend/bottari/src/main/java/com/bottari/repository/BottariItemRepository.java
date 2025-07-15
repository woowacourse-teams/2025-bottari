package com.bottari.repository;

import com.bottari.domain.BottariItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottariItemRepository extends JpaRepository<BottariItem, Long> {

    boolean existsByBottariIdAndName(
            final Long bottariId,
            final String name
    );

    boolean existsByBottariIdAndId(
            final Long bottariId,
            final Long id
    );
}
