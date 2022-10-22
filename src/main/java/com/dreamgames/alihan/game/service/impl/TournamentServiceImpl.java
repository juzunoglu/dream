package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.exception.InsufficientCoinException;
import com.dreamgames.alihan.game.exception.InsufficientLevelException;
import com.dreamgames.alihan.game.exception.TournamentNotFound;
import com.dreamgames.alihan.game.model.CreateTournamentRequest;
import com.dreamgames.alihan.game.model.EnterTournamentRequest;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;
import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.repository.TournamentDao;
import com.dreamgames.alihan.game.service.TournamentService;
import com.dreamgames.alihan.game.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.dreamgames.alihan.game.entity.enumaration.TournamentState.DONE;
import static com.dreamgames.alihan.game.entity.enumaration.TournamentState.STARTED;

@Service
@Slf4j
@Transactional
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private final TournamentDao tournamentDao;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    private final static Long TOURNAMENT_FEE = 1_000L;
    private final static int MINIMUM_LEVEL_REQUIRED_TO_ENTER = 20;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public TournamentServiceImpl(TournamentDao tournamentDao) {
        this.tournamentDao = tournamentDao;
    }

    @Override
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
    // To make this across the all nodes can use this -> TAM OLARAK 12::00 PM'de çalişti!
    @Scheduled(cron = "0 0 0-20 * * MON-SUN", zone = "UTC") // -> Between 00:00(UTC) AM and 20:00(UTC) PM, Monday through Sunday
    public void startScheduledTournament() {
        tournamentDao.getCurrentTournament()
                .ifPresent(tournament -> tournament.setState(DONE)); //soft delete the old tournament

        Tournament newTournament = Tournament.builder()
                .name(randomStringGenerator())
                .participants(Collections.emptyList())
                .state(STARTED)
                .build();

        tournamentDao.save(newTournament);
    }

    @Override
    public Tournament getTournamentById(Long id) {
        return tournamentDao.getTournamentById(id);
    }

    @Override
    public Tournament getCurrentTournament() {
        return tournamentDao.getCurrentTournament()
                .orElseThrow(() -> new TournamentNotFound("No available tourmanet at the moment"));
    }

    @Override
    public Tournament save(Tournament tournament) {
        return tournamentDao.save(tournament);
    }

    @Override
    public boolean delete(Long id) {
        Tournament tournament = tournamentDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        tournamentDao.delete(tournament);
        return true;
    }

    @Override
    public List<LeaderBoardDTO> enterTournament(EnterTournamentRequest enterTournamentRequest) {
        User user = userService.findUserById(enterTournamentRequest.getUserId());
        Tournament currentTournament = this.getTournamentById(enterTournamentRequest.getTournamentId());
        if (user.getLevel() < MINIMUM_LEVEL_REQUIRED_TO_ENTER) {
            throw new InsufficientLevelException("You have to be at least 20 level to enter to the tournament");
        }
        if (user.getCoin() < TOURNAMENT_FEE) {
            throw new InsufficientCoinException("You must have at least 1000 coins to enter to the tournament");
        }
        // todo(User cannot enter a new tournament if he/she didn’t claim last entered tournament)
        // todo when user is in a tournament and gets level updated the leaderboard should be changed!!!
        currentTournament.addUser(user);
        this.save(currentTournament);
        userService.payTournamentFee(user, TOURNAMENT_FEE);
        redisService.save(user);
        return redisService.getGroupLeaderBoard(user.getId().toString());
    }

    @Override
    public List<LeaderBoardDTO> getGlobalLeaderBoard() {
        return redisService.getGlobalLeaderBoard();
    }

    private String randomStringGenerator() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
