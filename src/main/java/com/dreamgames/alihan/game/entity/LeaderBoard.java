package com.dreamgames.alihan.game.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "related_users")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "leaderBoard")
    @ToString.Exclude
    private List<User> userList;

    private Long position;

    private Double score;

}