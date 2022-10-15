package com.dreamgames.alihan.game.entity;


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
@Table(name = "dream_user")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "coin")
    private Long coin;

    @Column(name = "level")
    private int level;

    @Column(name = "rank")
    private int rank;

    @Column(name = "tournament_score")
    private Long tournamentScore;

    @Type(type= "org.hibernate.type.NumericBooleanType")
    @Column(name = "is_reward_claimed", nullable = false)
    private boolean isRewardClaimed = false;

    public int incrementLevel() {
        return ++this.level;
    }

    public Long addCoin(Long coin) {
        return this.coin + coin;
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
