package com.dreamgames.alihan.game.redis.service;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import com.dreamgames.alihan.game.entity.Team;
import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.redis.service.enumaration.LevelTeamDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private Jedis jedis;

    @Override
    public void save(User user) {
        Map<String, Double> groupMap = new HashMap<>();
        if (user.getLevel() < 20) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(user.getId().toString(), "id", user.getId().toString());
            jedis.hset(user.getId().toString(), "username", user.getName());
            jedis.zadd(LevelTeamDecider.LEVEL_6.getGroupName(), groupMap);
        }
        if (user.getLevel() > 20 && user.getLevel() <= 100) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(user.getId().toString(), "id", user.getId().toString());
            jedis.hset(user.getId().toString(), "username", user.getName());
            jedis.zadd(LevelTeamDecider.LEVEL_1.getGroupName(), groupMap);
        } else if (user.getLevel() > 100 && user.getLevel() <= 200) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(user.getId().toString(), "id", user.getId().toString());
            jedis.hset(user.getId().toString(), "username", user.getName());
            jedis.zadd(LevelTeamDecider.LEVEL_2.getGroupName(), groupMap);
        } else if (user.getLevel() > 200 && user.getLevel() <= 300) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(user.getId().toString(), "id", user.getId().toString());
            jedis.hset(user.getId().toString(), "username", user.getName());
            jedis.zadd(LevelTeamDecider.LEVEL_3.getGroupName(), groupMap);
        } else if (user.getLevel() > 300 && user.getLevel() <= 400) {
            groupMap.put(user.getId().toString(), INITIAL_SCORE);
            jedis.hset(user.getId().toString(), "id", user.getId().toString());
            jedis.hset(user.getId().toString(), "username", user.getName());
            jedis.zadd(LevelTeamDecider.LEVEL_4.getGroupName(), groupMap);
        }
        saveToGlobalLeaderboard(user);
    }

    private void saveToGlobalLeaderboard(User user) {
        userMap.put(user.getId().toString(), INITIAL_SCORE); // for global score !
        jedis.hset(user.getId().toString(), "id", user.getId().toString());
        jedis.hset(user.getId().toString(), "username", user.getName());
        jedis.zadd(globalLeaderboard, userMap);
    }

    @Override
    public List<LeaderBoard> getGroupLeaderBoard(String userId) {
        // TODO(return a list maybe? can do the same as the globalLeaderBoard?)
        LeaderBoard leaderBoard = new LeaderBoard();
        User user = getUserFromHashSet(userId);
        Long position = jedis.zrevrank(globalLeaderboard, userId);
        ++position;
        Double value = jedis.zscore(globalLeaderboard, userId);
        leaderBoard.setPosition(position);
        leaderBoard.setScore(value);
        Team team = new Team();
        team.setTeamParticipant(List.of(user));
        leaderBoard.setTeam(team);
        return List.of(leaderBoard);
    }

    @Override
    public List<LeaderBoard> getGlobalLeaderBoard() {
        List<LeaderBoard> leaderboardList = new ArrayList<>();
        Set<Tuple> tupleList = jedis.zrevrangeWithScores(globalLeaderboard, 0, -1);
        long position = 0;
        for (Tuple tuple : tupleList) {
            // When using zrevrange I need to handle the positions
            position++;
            LeaderBoard leaderboard = new LeaderBoard();
            // Retrieving the user from hash set
            leaderboard.setTeam(Team.builder()
                    .teamParticipant(
                            List.of(getUserFromHashSet(tuple.getElement()))
                    ).build());
            // Setting the position
            leaderboard.setPosition(position);

            // Setting the score from the tuple
            leaderboard.setScore(tuple.getScore());
            // Adding the LeaderboardList
            leaderboardList.add(leaderboard);
        }
        return leaderboardList;
    }

    @Override
    public LeaderBoard getUserLeaderBoard(String userId) {
        LeaderBoard leaderBoard = new LeaderBoard();
        User user = getUserFromHashSet(userId);
        Long position = jedis.zrevrank(globalLeaderboard, userId);
        ++position;
        Double value = jedis.zscore(globalLeaderboard, userId);
        leaderBoard.setPosition(position);
        leaderBoard.setScore(value);
        Team team = new Team();
        team.setTeamParticipant(List.of(user));
        leaderBoard.setTeam(team);
        return leaderBoard;
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
