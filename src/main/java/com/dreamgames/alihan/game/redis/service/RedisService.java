package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import com.dreamgames.alihan.game.entity.User;

import java.util.List;

public interface RedisService {
    void save(User user);

    List<LeaderBoard> getGroupLeaderBoard(String groupId);

    List<LeaderBoard> getGlobalLeaderBoard();

    LeaderBoard getUserLeaderBoard(String userId);
}
