package com.bottari.repository;

import com.bottari.domain.BottariItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottariItemRepository extends JpaRepository<BottariItem, Long> {

    List<BottariItem> findAllByBottariId(final Long bottariId);

    boolean existsByBottariIdAndName(
            final Long bottariId,
            final String name
    );
}
