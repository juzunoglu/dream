package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;

import java.util.List;

public interface RedisService {
    void save(User user);

    void updateUserScore(Long userId);

    List<LeaderBoardDTO> getGroupLeaderBoard(String groupId);

    List<LeaderBoardDTO> getGlobalLeaderBoard();
}
