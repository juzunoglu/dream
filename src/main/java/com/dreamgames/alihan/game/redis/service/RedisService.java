package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import com.dreamgames.alihan.game.entity.User;
public interface RedisService {
    void save(User user);

    LeaderBoard getUserLeaderBoard(String userId);
}
