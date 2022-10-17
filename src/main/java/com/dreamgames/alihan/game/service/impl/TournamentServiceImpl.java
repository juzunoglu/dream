package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.repository.TournamentDao;
import com.dreamgames.alihan.game.service.TournamentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dreamgames.alihan.game.entity.enumaration.TournamentState.STARTED;

@Service
@Slf4j
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private final TournamentDao tournamentDao;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public TournamentServiceImpl(TournamentDao tournamentDao) {
        this.tournamentDao = tournamentDao;
    }

    @Override
//    @Scheduled(cron = "* * * * * *") // -> Every sec
    public void startTournament() {
        Tournament newTournament = Tournament.builder()
                .name("Tournament")
                .maxParticipantNumber(100)
                .state(STARTED)
                .build();

        log.info("The time is now {}", dateFormat.format(new Date()));
//        tournamentDao.save(newTournament);
//        log.info("Tournament has now started: {}", newTournament);
    }
}
