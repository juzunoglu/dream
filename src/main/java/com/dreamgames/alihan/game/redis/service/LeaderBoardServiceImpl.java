package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.exception.PositionDoesNotExistException;
import com.dreamgames.alihan.game.redis.entity.LeaderBoard;
import com.dreamgames.alihan.game.redis.repository.LeaderBoardDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Slf4j
@Transactional
public class LeaderBoardServiceImpl implements LeaderBoardService {

    @Autowired
    private LeaderBoardDao leaderBoardDao;

    @Override
    public LeaderBoard save(LeaderBoard leaderBoard) {
        return leaderBoardDao.save(leaderBoard);
    }
    @Override
    public void update(Long userId, Long position) {
        LeaderBoard leaderBoard = leaderBoardDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No leaderboard is found"));
        leaderBoard.setPosition(position);
        leaderBoardDao.save(leaderBoard);
    }

    @Override
    public Long getPositionInTournament(Long userId, Long tournamentId) {
        return leaderBoardDao.findByUserIdAndTournamentId(userId, tournamentId)
                .orElseThrow(() -> new PositionDoesNotExistException("Cannot get the position for a specified tournament"))
                .getPosition();
    }
}
