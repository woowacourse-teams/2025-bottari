package com.bottari.repository;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottariItemRepository extends JpaRepository<BottariItem, Long> {

    List<BottariItem> findAllByBottariId(final Long bottariId);

    List<BottariItem> findAllByBottariIn(final List<Bottari> bottaries);

    boolean existsByBottariIdAndName(
            final Long bottariId,
            final String name
    );

    boolean existsByBottariIdAndNameIn(
            final Long bottariId,
            final List<String> itemNames
    );

    int countAllByBottariIdAndIdIn(
            final Long bottariId,
            final List<Long> ids
    );

    void deleteByIdIn(final List<Long> ids);
}
