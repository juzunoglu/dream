package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.Reward;
import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.exception.RewardNotFoundException;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;
import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.repository.RewardDao;
import com.dreamgames.alihan.game.service.RewardService;
import com.dreamgames.alihan.game.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RewardServiceImpl implements RewardService {

    @Autowired
    private RewardDao rewardDao;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @Override
    public Reward save(Reward reward) {
        return rewardDao.save(reward);
    }
    @Override
    public boolean isRewardClaimed(Long userId, Long tournamentId) {
        Reward reward = rewardDao.getRewardByUserId(userId)
                .orElseThrow(() -> new RewardNotFoundException("Reward is not found"));
        return reward.isRewardClaimed();
    }

    @Override
    public User claimReward(Long userId, Long tournamentId) {
       User user = userService.findUserById(userId);
       int userRank = redisService.getUserRank(user.getTournamentGroup().getName(), user.getId()).intValue();
       Reward userReward = rewardDao.getRewardByUserAndTournamentId(userId, tournamentId)
               .orElseThrow(() -> new RewardNotFoundException("Reward is not found"));
       userReward.setRewardClaimed(true);
       switch (userRank) {
           case 1 -> user.addCoin(10_000L);
           case 2 -> user.addCoin(5_000L);
           case 3 -> user.addCoin(3_000L);
           case 4, 5, 6, 7, 8, 9, 10 -> user.addCoin(1_000L);
           default -> log.info("No reward for you :)");
       }
       return user;
    }
}
