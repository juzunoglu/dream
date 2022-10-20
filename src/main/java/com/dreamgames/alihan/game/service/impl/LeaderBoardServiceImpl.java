package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import com.dreamgames.alihan.game.repository.LeaderBoardDao;
import com.dreamgames.alihan.game.service.LeaderBoardService;
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
    public boolean delete(LeaderBoard leaderBoard) {
        return false;
    }

    @Override
    public LeaderBoard findByGroupId(Long groupId) {
        return null;
    }

    @Override
    public LeaderBoard findGlobal() {
        return null;
    }
}
