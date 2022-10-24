package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.TournamentGroup;

import java.util.List;

public interface TournamentGroupService {

    TournamentGroup save(TournamentGroup tournamentGroup);

    List<TournamentGroup> findAll();

    TournamentGroup findByGroupLevel(int level);

}
