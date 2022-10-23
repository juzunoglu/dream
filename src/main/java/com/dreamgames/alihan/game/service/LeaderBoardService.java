package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.LeaderBoard;


public interface LeaderBoardService {

    LeaderBoard save(LeaderBoard leaderBoard);
    boolean delete(LeaderBoard leaderBoard);
    LeaderBoard findByGroupId(Long groupId);
    LeaderBoard findGlobal();
}
