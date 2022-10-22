package com.dreamgames.alihan.game.entity;


import com.dreamgames.alihan.game.entity.enumaration.TournamentState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tournament")
    private List<User> participants = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tournament")
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

    public void removeUser(User user) {
        participants.clear();
        user.setTournament(null);
    }

}
