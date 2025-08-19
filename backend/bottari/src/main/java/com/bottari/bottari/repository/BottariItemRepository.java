package com.bottari.bottari.repository;

import com.bottari.bottari.domain.Bottari;
import com.bottari.bottari.domain.BottariItem;
import com.bottari.vo.ItemName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BottariItemRepository extends JpaRepository<BottariItem, Long> {

    @Query("""
            SELECT bi
            FROM BottariItem bi
            JOIN FETCH bi.bottari
            WHERE bi.id = :id
            """)
    Optional<BottariItem> findByIdWithBottari(final Long id);

    List<BottariItem> findAllByBottariId(final Long bottariId);

    List<BottariItem> findAllByBottariIn(final List<Bottari> bottaries);

    boolean existsByBottariIdAndName(
            final Long bottariId,
            final ItemName name
    );

    boolean existsByBottariIdAndNameIn(
            final Long bottariId,
            final List<ItemName> itemNames
    );

    int countAllByBottariIdAndIdIn(
            final Long bottariId,
            final List<Long> ids
    );

    int countAllByBottariId(final Long bottariId);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE BottariItem bi
            SET bi.isChecked = false
            WHERE bi.bottari.id = :bottariId
            """)
    void resetCheckStatusByBottariId(final Long bottariId);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE BottariItem bt
            SET bt.deletedAt = CURRENT_TIMESTAMP
            WHERE bt.bottari.id = :bottariId
            """)
    void deleteByBottariId(final Long bottariId);

    void deleteByIdIn(final List<Long> ids);
}
