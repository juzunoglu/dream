package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.Reward;
import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.exception.InsufficientCoinException;
import com.dreamgames.alihan.game.exception.InsufficientLevelException;
import com.dreamgames.alihan.game.exception.RewardNotClaimedException;
import com.dreamgames.alihan.game.exception.TournamentNotFound;
import com.dreamgames.alihan.game.model.CreateTournamentRequest;
import com.dreamgames.alihan.game.model.EnterTournamentRequest;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;
import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.repository.RewardDao;
import com.dreamgames.alihan.game.repository.TournamentDao;
import com.dreamgames.alihan.game.service.RewardService;
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

    private final static Long TOURNAMENT_FEE = 1_000L;
    private final static int MINIMUM_LEVEL_REQUIRED_TO_ENTER = 20;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private TournamentDao tournamentDao;

    @Autowired
    private RewardDao rewardDao;

    @Autowired
    private RewardService rewardService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Override
    public Long startTournament(CreateTournamentRequest createTournamentRequest) {
        //todo what happens when a tournament is finished???? need to clear the tables ?
        tournamentDao.getCurrentTournament()
                .ifPresent(tournament -> tournament.setState(DONE)); //soft delete the old tournament

        Tournament newTournament = Tournament.builder()
                .name(createTournamentRequest.getTournamentName())
                .createdAt(new Date())
                .state(STARTED)
                .build();

        log.info("The time is now {}", dateFormat.format(new Date()));
        return tournamentDao.save(newTournament).getId();
    }

    @Override
    // To make this across the all nodes can use this -> TAM OLARAK 12::00 PM'de çalişti! sanırım saat başı çalişiyor bu şuanda
    @Scheduled(cron = "0 0 0-20 * * MON-SUN", zone = "UTC")
    // -> Between 00:00(UTC) AM and 20:00(UTC) PM, Monday through Sunday
    //todo what happens when a tournament is finished???? need to clear the tables ?
    public void startScheduledTournament() {
        tournamentDao.getCurrentTournament()
                .ifPresent(tournament -> tournament.setState(DONE)); //soft delete the old tournament

        Tournament newTournament = Tournament.builder()
                .name(randomStringGenerator())
                .createdAt(new Date())
                .participants(Collections.emptyList())
                .state(STARTED)
                .build();

        tournamentDao.save(newTournament);
    }

    @Override
    public Tournament getTournamentById(Long id) {
        return tournamentDao.getTournamentById(id)
                .orElseThrow(() -> new TournamentNotFound("No available tournament at the moment"));
    }

    @Override
    public Tournament getCurrentTournament() {
        return tournamentDao.getCurrentTournament()
                .orElseThrow(() -> new TournamentNotFound("No available tournament at the moment"));
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
            throw new InsufficientLevelException("You have to be at least " + MINIMUM_LEVEL_REQUIRED_TO_ENTER + " level to enter to the tournament");
        }
        if (user.getCoin() < TOURNAMENT_FEE) {
            throw new InsufficientCoinException("You must have at least " + TOURNAMENT_FEE + " coins to enter to the tournament");
        }
        currentTournament.addUser(user);
        this.save(currentTournament);
        userService.payTournamentFee(user, TOURNAMENT_FEE);
        rewardService.save(Reward.builder()
                .user(user)
                .tournament(currentTournament).build());

//        if (!rewardService.isRewardClaimed(user.getId())) { //todo
//            throw new RewardNotClaimedException("You have to claim the reward from the past tournament");
//        }
        redisService.save(user);
        return redisService.getGroupLeaderBoard(user.getTournamentGroup().getName());
    }

    @Override
    public List<LeaderBoardDTO> getGlobalLeaderBoard() {
        return redisService.getGlobalLeaderBoard();
    }

    @Override
    public List<LeaderBoardDTO> getGroupLeaderBoard(String groupName) {
        return redisService.getGroupLeaderBoard(groupName);
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
