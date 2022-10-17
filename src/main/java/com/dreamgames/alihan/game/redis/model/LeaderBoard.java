package com.dreamgames.alihan.game.redis.model;

import com.dreamgames.alihan.game.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString
@RedisHash("leaderboard")
public class LeaderBoard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private List<User> userList;


}
