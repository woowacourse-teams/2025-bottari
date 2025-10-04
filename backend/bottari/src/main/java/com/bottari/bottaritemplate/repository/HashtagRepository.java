package com.bottari.bottaritemplate.repository;

import com.bottari.bottaritemplate.domain.Hashtag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    List<Hashtag> findAllByNameIn(final List<String> hashtagNames);
}
