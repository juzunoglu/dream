package com.dreamgames.alihan.game.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "dream_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "coin")
    private Long coin = 5_000L;

    @Column(name = "level")
    private int level = 0;


    @Column(name = "rank")
    @Schema(hidden = true)
    private Double rank;

    @Column(name = "tournament_score")
    @Schema(hidden = true)
    @JsonIgnore
    private Long tournamentScore;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "is_reward_claimed", nullable = false)
    @Schema(hidden = true)
    @JsonIgnore
    private boolean isRewardClaimed;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    @Schema(hidden = true)
    @ToString.Exclude
    @JsonIgnore
    private TournamentGroup tournamentGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    @Schema(hidden = true)
    @ToString.Exclude
    @JsonIgnore
    private Tournament tournament;


    public void incrementLevel() {
        this.setLevel(++this.level);
    }

    public void addCoin(Long coin) {
        this.setCoin(this.coin + coin);
    }

    public void payTournamentFee(Long coin) {
         this.setCoin(this.coin - coin);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
