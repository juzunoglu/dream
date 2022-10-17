package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.redis.model.LeaderBoard;
import com.dreamgames.alihan.game.redis.repository.LeaderBoardDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService {

    private final LeaderBoardDao leaderBoardDao;

    public LeaderBoardServiceImpl(LeaderBoardDao leaderBoardDao) {
        this.leaderBoardDao = leaderBoardDao;
    }

    @Override
    public LeaderBoard save(LeaderBoard leaderBoard) {
        return leaderBoardDao.save(leaderBoard);
    }

    @Override
    public LeaderBoard findById(Long id) {
        return leaderBoardDao.findLeaderboardById(id);
    }

    @Override
    public List<Object> findAll() {
        return leaderBoardDao.findAll();
    }

    @Override
    public boolean delete(Long id) {
        return leaderBoardDao.deleteLeaderBoard(id);
    }
}
