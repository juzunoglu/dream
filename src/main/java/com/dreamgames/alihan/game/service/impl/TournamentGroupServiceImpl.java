package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.TournamentGroup;
import com.dreamgames.alihan.game.repository.TournamentGroupDao;
import com.dreamgames.alihan.game.service.TournamentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentGroupServiceImpl implements TournamentGroupService {

    @Autowired
    private TournamentGroupDao tournamentGroupDao;

    @Override
    public TournamentGroup save(TournamentGroup tournamentGroup) {
        return tournamentGroupDao.save(tournamentGroup);
    }

    @Override
    public List<TournamentGroup> findAll() {
        return tournamentGroupDao.findAll();
    }

    @Override
    public TournamentGroup findByGroupLevel(int level) {
        return tournamentGroupDao.getTournamentByLevel(level)
                .orElseThrow(() ->  new RuntimeException("No tournament group is found"));
    }

}
