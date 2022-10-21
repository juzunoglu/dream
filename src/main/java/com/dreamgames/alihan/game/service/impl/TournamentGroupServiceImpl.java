package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.TournamentGroup;
import com.dreamgames.alihan.game.entity.enumaration.GroupLevelMapping;
import com.dreamgames.alihan.game.repository.TournamentGroupDao;
import com.dreamgames.alihan.game.service.TournamentGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TournamentGroupServiceImpl implements TournamentGroupService {

    @Autowired
    private TournamentGroupDao tournamentGroupDao;

    @Override
    public TournamentGroup save(TournamentGroup tournamentGroup) {
        return tournamentGroupDao.save(tournamentGroup);
    }

    @Override
    public TournamentGroup update(Long id, TournamentGroup tournamentGroup) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        AtomicBoolean res = new AtomicBoolean(true);
        tournamentGroupDao.findById(id)
                .ifPresentOrElse(
                        (tournamentGroup) -> tournamentGroupDao.delete(tournamentGroup),
                        () -> res.set(false)
        );
        return res.get();
    }

    @Override
    public TournamentGroup findById(Long id) {
        return tournamentGroupDao.findById(id)
                .orElseThrow(() -> new RuntimeException("No tournament group is found"));
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

    @Override
    public TournamentGroup findByGroupName(GroupLevelMapping groupLevelMapping) {
        return null;
    }
}
