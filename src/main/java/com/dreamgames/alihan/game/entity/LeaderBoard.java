package com.dreamgames.alihan.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;

@RedisHash("leaderboard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderBoard implements Serializable {

    private Long id;
    private Long tournamentId;
    private Long userId;
    private String groupName;
    private double rank;
}