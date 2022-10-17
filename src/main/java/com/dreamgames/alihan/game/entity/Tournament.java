package com.dreamgames.alihan.game.entity;


import com.dreamgames.alihan.game.entity.enumaration.TournamentState;
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
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "max_participant_number")
    private int maxParticipantNumber;

    @Column(name = "participants")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tournament")
    private List<User> participants = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leader_board_id")
    private LeaderBoard leaderBoard;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private TournamentState state;

}
