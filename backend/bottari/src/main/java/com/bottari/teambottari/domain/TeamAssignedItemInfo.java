package com.bottari.teambottari.domain;

import com.bottari.support.BaseTimeEntity;
import com.bottari.vo.ItemName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamAssignedItemInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ItemName name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_bottari_id")
    private TeamBottari teamBottari;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public TeamAssignedItemInfo(
            final String name,
            final TeamBottari teamBottari
    ) {
        this.name = new ItemName(name);
        this.teamBottari = teamBottari;
    }

    public boolean isSameByName(final String otherName) {
        return name.equals(new ItemName(otherName));
    }

    public void updateName(final String updatedName) {
        if (name.value().equals(updatedName)) {
            return;
        }
        this.name = new ItemName(updatedName);
    }

    public String getName() {
        return name.value();
    }
}
