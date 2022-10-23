package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.Reward;
import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.exception.*;
import com.dreamgames.alihan.game.model.CreateTournamentRequest;
import com.dreamgames.alihan.game.model.EnterTournamentRequest;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;
import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.repository.RewardDao;
import com.dreamgames.alihan.game.repository.TournamentDao;
import com.dreamgames.alihan.game.service.LeaderBoardService;
import com.dreamgames.alihan.game.service.RewardService;
import com.dreamgames.alihan.game.service.TournamentService;
import com.dreamgames.alihan.game.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.dreamgames.alihan.game.entity.enumaration.TournamentState.DONE;
import static com.dreamgames.alihan.game.entity.enumaration.TournamentState.STARTED;
import static com.dreamgames.alihan.game.redis.service.RedisServiceImpl.REDIS_HASH_KEY;

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
    private LeaderBoardService leaderBoardService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @Override
    public Long startTournament(CreateTournamentRequest createTournamentRequest) {
        tournamentDao.getCurrentTournament()
                .ifPresent(tournament -> {
                    tournament.removeUsers(userService.getAllUsersByTournamentId(tournament.getId()));
                    redisService.clearOldLeaderBoard();
                    tournament.setState(DONE);
                    tournamentDao.save(tournament);
                }); //soft delete the old tournament and clear the redis set

        Tournament newTournament = Tournament.builder()
                .name(createTournamentRequest.getTournamentName())
                .createdAt(new Date())
                .state(STARTED)
                .build();

        log.info("The time is now {}", dateFormat.format(new Date()));
        return tournamentDao.save(newTournament).getId();
    }

//    @Override
//    @Scheduled(cron = "0 0 0-20 * * MON-SUN", zone = "UTC")
//    // -> Between 00:00(UTC) AM and 20:00(UTC) PM, Monday through Sunday
//    public void startScheduledTournament() {
//        tournamentDao.getCurrentTournament()
//                .ifPresent(tournament -> {
//                    tournament.removeUsers(tournament.getParticipants());
//                    redisService.clearOldLeaderBoard();
//                    tournament.setState(DONE);
//                    tournamentDao.save(tournament);
//                }); //soft delete the old tournament and clear the redis set
//
//
//        Tournament newTournament = Tournament.builder()
//                .name(randomStringGenerator())
//                .createdAt(new Date())
//                .participants(Collections.emptyList())
//                .state(STARTED)
//                .build();
//
//        tournamentDao.save(newTournament);
//    }

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
    public List<LeaderBoardDTO> enterTournament(EnterTournamentRequest enterTournamentRequest) {
        User user = userService.findUserById(enterTournamentRequest.getUserId());
        if (userService.isUserAlreadyInTournament(user)) {
            throw new AlreadyInTournamentException("You're already in the tournament");
        }
        Tournament currentTournament = this.getCurrentTournament();
        if (user.getLevel() < MINIMUM_LEVEL_REQUIRED_TO_ENTER) {
            throw new InsufficientLevelException("You have to be at least " + MINIMUM_LEVEL_REQUIRED_TO_ENTER + " level to enter to the tournament");
        }
        if (user.getCoin() < TOURNAMENT_FEE) {
            throw new InsufficientCoinException("You must have at least " + TOURNAMENT_FEE + " coins to enter to the tournament");
        }

        if (!rewardService.isRewardClaimed(user.getId())) {
            throw new RewardNotClaimedException("You have to claim the reward from the past tournament");
        }
        currentTournament.addUser(user);
        tournamentDao.save(currentTournament);

        userService.payTournamentFee(user, TOURNAMENT_FEE);

        rewardService.save(Reward.builder()
                .user(user)
                .tournament(currentTournament).build());

        redisService.save(user);
        return redisService.getGroupLeaderBoard(REDIS_HASH_KEY.concat(user.getTournamentGroup().getName()));
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
