package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import com.dreamgames.alihan.game.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RedisServiceImpl implements RedisService {

    private static final String globalLeaderboard = "globalRank";


    private static  Map<String, Double> userMap = new HashMap<>();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private Jedis jedis;

    @Override
    public void save(User user) {
        userMap.put(user.getId().toString(), 0.0); // for score !
        jedis.hset(user.getId().toString(), "id", user.getId().toString());
        jedis.hset(user.getId().toString(), "username", user.getName());
        jedis.zadd(globalLeaderboard, userMap);
    }

    @Override
    public LeaderBoard getUserLeaderBoard(String userId) {
        LeaderBoard leaderBoard = new LeaderBoard();
        User user = getUserFromHashSet(userId);
        Long position = jedis.zrevrank(globalLeaderboard, userId);
        ++position;
        Double value = jedis.zscore(globalLeaderboard, userId);
        leaderBoard.setPosition(position);
        leaderBoard.setScore(value);
        leaderBoard.setUserList(List.of(user));
        return leaderBoard;
    }

    private User getUserFromHashSet(String userId) {
        User user = new User();
        if (jedis.exists(userId)) {
            user.setId(Long.parseLong(jedis.hget(userId, "id")));
            user.setName(jedis.hget(userId, "username"));
        }
        return user;

    }

}
