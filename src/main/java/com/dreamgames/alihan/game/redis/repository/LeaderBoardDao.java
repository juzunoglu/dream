package com.dreamgames.alihan.game.redis.repository;

import com.dreamgames.alihan.game.redis.model.LeaderBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LeaderBoardDao {

    public static final String HASH_KEY = "leaderboard";
    @Autowired
    private RedisTemplate redisTemplate;

    public LeaderBoardDao(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public LeaderBoard save(LeaderBoard leaderBoard) {
        redisTemplate.opsForHash().put(HASH_KEY, leaderBoard.getId(), leaderBoard);
        return leaderBoard;
    }

    public List<Object> findAll() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public LeaderBoard findLeaderboardById(Long id) {
        return (LeaderBoard) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public boolean deleteLeaderBoard(Long id) {
        redisTemplate.opsForHash().delete(HASH_KEY, id);
        return true;
    }
}
