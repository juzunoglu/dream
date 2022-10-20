package com.dreamgames.alihan.game.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "team_participant")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    @ToString.Exclude
    private List<User> teamParticipant = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leaderboard_id")
    @JsonIgnore
    private LeaderBoard leaderBoard;

    public void addUser(User user) {
        teamParticipant.add(user);
        user.setTeam(this);
    }

    public void removeUser(User user) {
        teamParticipant.remove(user);
        user.setTeam(null);
    }
}
