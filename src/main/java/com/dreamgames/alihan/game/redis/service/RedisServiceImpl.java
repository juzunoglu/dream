package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;
import com.dreamgames.alihan.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
public class RedisServiceImpl implements RedisService {

    private static final String globalLeaderboard = "globalRank";
    private static Map<String, Double> userMap = new HashMap<>();

    private static final Double INITIAL_SCORE = 0.0;

    @Autowired
    private Jedis jedis;

    @Autowired
    private UserService userService;

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
    public void updateUserScore(Long userId) {
        User userFromRedis = getUserFromHashSet(userId.toString());
        User user = userService.findUserById(userId);
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
    public List<LeaderBoardDTO> getGroupLeaderBoard(String userId) {
        List<LeaderBoardDTO> groupLeaderBoard = new ArrayList<>();
        User user = userService.findUserById(Long.parseLong(userId));
        Set<Tuple> tupleList = jedis.zrevrangeWithScores(user.getTournamentGroup().getName(), 0, 20);
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

    private User getUserFromHashSet(String userId) {
        User user = new User();
        if (jedis.exists(userId)) {
            user.setId(Long.parseLong(jedis.hget(userId, "id")));
            user.setName(jedis.hget(userId, "username"));
        }
        return user;
    }

}
