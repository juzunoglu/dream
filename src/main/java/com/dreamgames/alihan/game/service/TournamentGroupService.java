package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.TournamentGroup;
import com.dreamgames.alihan.game.entity.enumaration.GroupLevelMapping;

import java.util.List;

public interface TournamentGroupService {

    TournamentGroup save(TournamentGroup tournamentGroup);

    TournamentGroup update(Long id, TournamentGroup tournamentGroup);

    boolean delete(Long id);

    TournamentGroup findById(Long id);

    List<TournamentGroup> findAll();

    TournamentGroup findByGroupLevel(int level);

    TournamentGroup findByGroupName(GroupLevelMapping groupLevelMapping);

}
