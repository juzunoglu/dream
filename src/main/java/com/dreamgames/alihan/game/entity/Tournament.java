package com.dreamgames.alihan.game.entity;


import com.dreamgames.alihan.game.entity.enumaration.TournamentState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "participants")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<User> participants = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tournament")
    @ToString.Exclude
    private List<Reward> rewardList = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private TournamentState state;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    public void addUser(User user) {
        participants.add(user);
        user.setTournament(this);
    }

    public void addRewards(Reward reward) {
        rewardList.add(reward);
        reward.setTournament(this);
    }

    public void removeUsers(List<User> userList) {
        this.participants.removeAll(userList);
        userList.forEach(user -> user.setTournament(null));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Tournament that = (Tournament) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
