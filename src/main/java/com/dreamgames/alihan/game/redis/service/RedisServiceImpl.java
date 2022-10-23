package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.args.FlushMode;

import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
@Slf4j
public class RedisServiceImpl implements RedisService {

    private static final String globalLeaderboard = "globalRank";
    private static Map<String, Double> userMap = new HashMap<>();

    private static final Double INITIAL_SCORE = 0.0;

    @Autowired
    private Jedis jedis;

    @Override
    public void save(User user) {
        Map<String, Double> groupMap = new HashMap<>();
        if (user.getLevel() >= 20 && user.getLevel() <= 100) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(user.getId().toString(), "id", user.getId().toString());
            jedis.hset(user.getId().toString(), "username", user.getName());
            jedis.zadd(user.getTournamentGroup().getName(), groupMap);
        } else if (user.getLevel() > 100 && user.getLevel() <= 200) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(user.getId().toString(), "id", user.getId().toString());
            jedis.hset(user.getId().toString(), "username", user.getName());
            jedis.zadd(user.getTournamentGroup().getName(), groupMap);
        } else if (user.getLevel() > 200 && user.getLevel() <= 300) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(user.getId().toString(), "id", user.getId().toString());
            jedis.hset(user.getId().toString(), "username", user.getName());
            jedis.zadd(user.getTournamentGroup().getName(), groupMap);
        } else if (user.getLevel() > 300 && user.getLevel() <= 400) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(user.getId().toString(), "id", user.getId().toString());
            jedis.hset(user.getId().toString(), "username", user.getName());
            jedis.zadd(user.getTournamentGroup().getName(), groupMap);
        }
        saveToGlobalLeaderboard(user);
    }

    @Override
    public void updateUserScore(User user) { //todo remove userDao? unncessary dependency?
        User userFromRedis = getUserFromHashSet(user.getId().toString());
        jedis.zincrby(user.getTournamentGroup().getName(), 1D, userFromRedis.getId().toString());
        jedis.zincrby(globalLeaderboard, 1D, userFromRedis.getId().toString());
    }

    private void saveToGlobalLeaderboard(User user) {
        userMap.put(user.getId().toString(), INITIAL_SCORE); // for global score !
        jedis.hset(user.getId().toString(), "id", user.getId().toString());
        jedis.hset(user.getId().toString(), "username", user.getName());
        jedis.zadd(globalLeaderboard, userMap);
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
        Set<Tuple> tupleList = jedis.zrevrangeWithScores(globalLeaderboard, 0, -1);
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
    public void clearOldLeaderBoard() {
        jedis.flushDB(FlushMode.ASYNC);
    }

    private User getUserFromHashSet(String userId) {
        User user = new User();
        if (jedis.exists(userId)) {
            user.setId(Long.parseLong(jedis.hget(userId, "id")));
            user.setName(jedis.hget(userId, "username"));
        }
        return user;
    }

}
