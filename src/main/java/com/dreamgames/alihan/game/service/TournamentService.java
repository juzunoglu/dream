package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.model.CreateTournamentRequest;

public interface TournamentService {

    Long startTournament(CreateTournamentRequest createTournamentRequest);

    Tournament getTournamentById(Long id);

    Tournament getCurrentTournament();

    Tournament save(Tournament tournament);

    boolean delete(Long id);
}
