package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.Reward;
import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.exception.RewardAlreadyClaimedException;
import com.dreamgames.alihan.game.exception.RewardNotFoundException;
import com.dreamgames.alihan.game.repository.RewardDao;
import com.dreamgames.alihan.game.service.LeaderBoardService;
import com.dreamgames.alihan.game.service.RewardService;
import com.dreamgames.alihan.game.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class RewardServiceImpl implements RewardService {

    @Autowired
    private LeaderBoardService leaderBoardService;
    @Autowired
    private RewardDao rewardDao;
    @Autowired
    private UserService userService;

    @Override
    public Reward save(Reward reward) {
        return rewardDao.save(reward);
    }
    @Override
    public boolean isRewardClaimed(Long userId) {
        Reward reward = rewardDao.getRewardByUserId(userId).orElse(null);
        if (reward == null) { // if the reward is null, it's the first time the user is entering the tournament!
            return true;
        }
        return reward.isRewardClaimed();
    }

    @Override
    public User claimReward(Long userId, Long tournamentId) {
       User user = userService.findUserById(userId);
       int rank = leaderBoardService.getPositionInTournament(userId, tournamentId).intValue();
       Reward userReward = rewardDao.getRewardByUserAndTournamentId(userId, tournamentId)
               .orElseThrow(() -> new RewardNotFoundException("Reward is not found"));
       if (userReward.isRewardClaimed()) {
           throw new RewardAlreadyClaimedException("You have already claimed your rewards");
       }
       userReward.setRewardClaimed(true);
       switch (rank) {
           case 1 -> user.addCoin(10_000L);
           case 2 -> user.addCoin(5_000L);
           case 3 -> user.addCoin(3_000L);
           case 4, 5, 6, 7, 8, 9, 10 -> user.addCoin(1_000L);
           default -> log.info("No reward for you :)");
       }
       return user;
    }
}
