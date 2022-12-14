package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.model.CreateTournamentRequest;
import com.dreamgames.alihan.game.model.EnterTournamentRequest;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;

import java.util.List;

public interface TournamentService {

    Long startTournament(CreateTournamentRequest createTournamentRequest);

//    void startScheduledTournament();
    Tournament getCurrentTournament();

    Tournament save(Tournament tournament);
    List<LeaderBoardDTO> enterTournament(EnterTournamentRequest enterTournamentRequest);

    List<LeaderBoardDTO> getGlobalLeaderBoard();

    List<LeaderBoardDTO> getGroupLeaderBoard(String groupName);
}
