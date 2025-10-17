package com.bottari.bottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.support.BaseTimeEntity;
import com.bottari.vo.BottariTitle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE bottari SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bottari extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BottariTitle title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Bottari(
            final String title,
            final Member member
    ) {
        this.title = new BottariTitle(title);
        this.member = member;
    }

    public boolean isOwner(final String ssaid) {
        return member.isSameBySsaid(ssaid);
    }

    public void updateTitle(final String newTitle) {
        if (title.title().equals(newTitle)) {
            throw new BusinessException(ErrorCode.BOTTARI_TITLE_UNCHANGED);
        }
        this.title = new BottariTitle(newTitle);
    }

    public String getTitle() {
        return title.title();
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Bottari bottari)) {
            return false;
        }

        return Objects.equals(getId(), bottari.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
