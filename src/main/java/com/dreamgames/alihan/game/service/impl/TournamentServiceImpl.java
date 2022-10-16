package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.repository.TournamentDao;
import com.dreamgames.alihan.game.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TournamentServiceImpl implements TournamentService {


    @Autowired
    private final TournamentDao tournamentDao;

    public TournamentServiceImpl(TournamentDao tournamentDao) {
        this.tournamentDao = tournamentDao;
    }

    @Override
    public void startTournament() {

    }

    //	 Between 07:00 PM and 08:59 PM, Monday through Friday -> 0 0 19-20 * * MON-FRI
//    @Override
//    @Scheduled(cron = "0 0 9-17 * * MON-FRI")
//    public void startTournament() {
//        Tournament.builder()
//                .name("Dream-Tournament")
//                .maxParticipantNumber(100)
//                .
//    }
    // TODO next
}
