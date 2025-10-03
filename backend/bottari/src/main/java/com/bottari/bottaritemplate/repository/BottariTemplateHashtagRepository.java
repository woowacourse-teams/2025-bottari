package com.bottari.bottaritemplate.repository;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateHashtag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BottariTemplateHashtagRepository extends JpaRepository<BottariTemplateHashtag, Long> {

    @Query("""
             SELECT bth
             FROM BottariTemplateHashtag bth
             JOIN FETCH bth.hashtag h
             WHERE bth.bottariTemplate.id = :bottariTemplateId
            """)
    List<BottariTemplateHashtag> findAllByBottariTemplateId(final Long bottariTemplateId);

    List<BottariTemplateHashtag> findAllByBottariTemplateIn(final List<BottariTemplate> bottariTemplateItems);

    @Query("""
            SELECT bth
            FROM BottariTemplateHashtag bth
            JOIN FETCH bth.hashtag h
            WHERE bth.bottariTemplate.id IN :templateIds
            """)
    List<BottariTemplateHashtag> findAllByBottariTemplateIds(final List<Long> templateIds);
}
