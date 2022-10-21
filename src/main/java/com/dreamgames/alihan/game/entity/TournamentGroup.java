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
@Table(name = "tournament_group")
public class TournamentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonIgnore
    private String name;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "level", updatable = false, nullable = false)
    @JsonIgnore
    private int level;

    @Column(name = "team_participant")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tournamentGroup")
    @ToString.Exclude
    private List<User> teamParticipant = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leaderboard_id")
    @JsonIgnore
    private LeaderBoard leaderBoard;

    public void addUser(User user) {
        teamParticipant.add(user);
        user.setTournamentGroup(this);
    }

    public void removeUser(User user) {
        teamParticipant.remove(user);
        user.setTournamentGroup(null);
    }
}
