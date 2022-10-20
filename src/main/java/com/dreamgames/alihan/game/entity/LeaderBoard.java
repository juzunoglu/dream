package com.dreamgames.alihan.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "leader_board")
public class LeaderBoard  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long position;

    private Double score;

    @JsonIgnore
    @OneToOne(mappedBy = "leaderBoard")
    private Team team;

}