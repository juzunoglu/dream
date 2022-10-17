package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.redis.model.LeaderBoard;

import java.util.List;

public interface LeaderBoardService {

    LeaderBoard save(LeaderBoard leaderBoard);

    LeaderBoard findById(Long id);

    List<Object> findAll();

    boolean delete(Long id);
}
