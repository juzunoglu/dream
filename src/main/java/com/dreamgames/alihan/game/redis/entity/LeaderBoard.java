package com.dreamgames.alihan.game.redis.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.*;
import java.io.Serializable;

@RedisHash("leaderboard")
@Data
@Builder
public class LeaderBoard implements Serializable {

    @Id
    private Long id;

    @Indexed
    private Long tournamentId;
    @Indexed
    private Long userId;
    private String groupName;

    @Indexed
    private Long position;
}