package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.model.CreateTournamentRequest;
import com.dreamgames.alihan.game.repository.TournamentDao;
import com.dreamgames.alihan.game.service.TournamentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static com.dreamgames.alihan.game.entity.enumaration.TournamentState.STARTED;

@Service
@Slf4j
@Transactional
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private final TournamentDao tournamentDao;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public TournamentServiceImpl(TournamentDao tournamentDao) {
        this.tournamentDao = tournamentDao;
    }

    @Override
//    @Scheduled(cron = "* * * * * *") // -> Every sec
    public Long startTournament(CreateTournamentRequest createTournamentRequest) {
        Tournament newTournament = Tournament.builder()
                .name(createTournamentRequest.getTournamentName())
                .participants(Collections.emptyList())
                .state(STARTED)
                .build();
        log.info("The time is now {}", dateFormat.format(new Date()));
        return tournamentDao.save(newTournament).getId();
    }

    @Override
    public Tournament getTournamentById(Long id) {
        return tournamentDao.getTournamentById(id);
    }

    @Override
    public Tournament getCurrentTournament() {
        return tournamentDao.getCurrentTournament();
    }

    @Override
    public Tournament save(Tournament tournament) {
        return tournamentDao.save(tournament);
    }

    @Override
    public boolean delete(Long id) {
       Tournament tournament =  tournamentDao.findById(id)
                        .orElseThrow(() -> new RuntimeException("Not found"));

       tournamentDao.delete(tournament);
       return true;
    }
}
