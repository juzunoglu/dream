package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.LeaderBoard;

public interface LeaderBoardService {
    LeaderBoard save(LeaderBoard leaderBoard);

    void update(Long userId, Long position);

    Long getPositionInTournament(Long userId, Long tournamentId);

}
