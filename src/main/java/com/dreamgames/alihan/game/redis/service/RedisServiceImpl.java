package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;
import com.dreamgames.alihan.game.service.LeaderBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import javax.transaction.Transactional;
import java.util.*;

import static redis.clients.jedis.ScanParams.SCAN_POINTER_START;


@Service
@Transactional
@Slf4j
public class RedisServiceImpl implements RedisService {

    private static final String USER_GLOBAL_RANK = "user::globalRank";

    public static final String REDIS_HASH_KEY = "user::";
    private static Map<String, Double> userMap = new HashMap<>();

    private static final Double INITIAL_SCORE = 0.0;

    @Autowired
    private Jedis jedis;

    @Autowired
    private LeaderBoardService leaderBoardService;

    @Override
    public void save(User user) {
        Map<String, Double> groupMap = new HashMap<>();
        if (user.getLevel() >= 20 && user.getLevel() <= 100) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(REDIS_HASH_KEY.concat(user.getId().toString()), "id", user.getId().toString());
            jedis.hset(REDIS_HASH_KEY.concat(user.getId().toString()), "username", user.getName());
            jedis.zadd(REDIS_HASH_KEY.concat(user.getTournamentGroup().getName()), groupMap);
        } else if (user.getLevel() > 100 && user.getLevel() <= 200) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(REDIS_HASH_KEY.concat(user.getId().toString()), "id", user.getId().toString());
            jedis.hset(REDIS_HASH_KEY.concat(user.getId().toString()), "username", user.getName());
            jedis.zadd(REDIS_HASH_KEY.concat(user.getId().toString()).concat(user.getTournamentGroup().getName()), groupMap);
        } else if (user.getLevel() > 200 && user.getLevel() <= 300) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(REDIS_HASH_KEY.concat(user.getId().toString()), "id", user.getId().toString());
            jedis.hset(REDIS_HASH_KEY.concat(user.getId().toString()), "username", user.getName());
            jedis.zadd(REDIS_HASH_KEY.concat(user.getTournamentGroup().getName()), groupMap);
        } else if (user.getLevel() > 300 && user.getLevel() <= 400) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(REDIS_HASH_KEY.concat(user.getId().toString()), "id", user.getId().toString());
            jedis.hset(REDIS_HASH_KEY.concat(user.getId().toString()), "username", user.getName());
            jedis.zadd(REDIS_HASH_KEY.concat(user.getTournamentGroup().getName()), groupMap);
        }
        leaderBoardService.save(LeaderBoard.builder()
                .tournamentId(user.getTournament().getId())
                .groupName(REDIS_HASH_KEY.concat(user.getTournamentGroup().getName()))
                .userId(user.getId())
                .position(0L)
                .build());

        saveToGlobalLeaderboard(user);
    }

    @Override
    public void updateUserScore(User user) { // should be async?
        User userFromRedis = getUserFromHashSet(user.getId().toString());
        jedis.zincrby(REDIS_HASH_KEY.concat(user.getTournamentGroup().getName()), 1D, userFromRedis.getId().toString());
        jedis.zincrby(USER_GLOBAL_RANK, 1D, userFromRedis.getId().toString());

        List<LeaderBoardDTO> leaderBoardDTOS = this.getGlobalLeaderBoard();
        leaderBoardDTOS.forEach(leaderBoardDTO -> leaderBoardService.update(leaderBoardDTO.userId(), leaderBoardDTO.position()));
    }

    private void saveToGlobalLeaderboard(User user) {
        userMap.put(user.getId().toString(), INITIAL_SCORE);
        jedis.zadd(USER_GLOBAL_RANK, userMap);
    }

    @Override
    public List<LeaderBoardDTO> getGroupLeaderBoard(String groupName) {
        List<LeaderBoardDTO> groupLeaderBoard = new ArrayList<>();
        Set<Tuple> tupleList = jedis.zrevrangeWithScores(groupName, 0, 20);
        long position = 0;
        for (Tuple tuple : tupleList) {
            position++;
            LeaderBoardDTO leaderBoardDTO = LeaderBoardDTO.builder()
                    .userId(getUserFromHashSet(tuple.getElement()).getId())
                    .username(getUserFromHashSet(tuple.getElement()).getName())
                    .tournamentScore(tuple.getScore())
                    .position(position)
                    .build();
            groupLeaderBoard.add(leaderBoardDTO);
        }
        return groupLeaderBoard;
    }

    @Override
    public Long getUserRank(String groupName, Long userId) {
        // zrevrank retrieves the user position at the sorted set
        Long rank = jedis.zrevrank(groupName, userId.toString());
        // Adding one to the position because it starts at 0
        if (rank != null) {
            rank = rank + 1; //rank starts at 0
        }
        return rank;
    }

    @Override
    public List<LeaderBoardDTO> getGlobalLeaderBoard() {
        List<LeaderBoardDTO> leaderboardList = new ArrayList<>();
        Set<Tuple> tupleList = jedis.zrevrangeWithScores(USER_GLOBAL_RANK, 0, -1);
        long position = 0;
        for (Tuple tuple : tupleList) {
            position++;
            LeaderBoardDTO leaderBoardDTO = LeaderBoardDTO.builder()
                    .userId(getUserFromHashSet(tuple.getElement()).getId())
                    .username(getUserFromHashSet(tuple.getElement()).getName())
                    .tournamentScore(tuple.getScore())
                    .position(position)
                    .build();
            leaderboardList.add(leaderBoardDTO);
        }
        return leaderboardList;
    }

    @Override
    @Async
    public void clearOldLeaderBoard() {
        ScanParams scanParams = new ScanParams().count(10).match("user::*");
        String cur = SCAN_POINTER_START;
        do {
            ScanResult<String> scanResult = jedis.scan(cur, scanParams);
            scanResult.getResult().forEach(System.out::println);
            scanResult.getResult().forEach(key -> jedis.del(key));
            cur = scanResult.getCursor();
        } while (!cur.equals(SCAN_POINTER_START));
    }

    private User getUserFromHashSet(String userId) {
        User user = new User();
        if (jedis.exists(REDIS_HASH_KEY.concat(userId))) {
            user.setId(Long.parseLong(jedis.hget(REDIS_HASH_KEY.concat(userId), "id")));
            user.setName(jedis.hget(REDIS_HASH_KEY.concat(userId), "username"));
        }
        return user;
    }

}
