package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.Reward;
import com.dreamgames.alihan.game.entity.User;

public interface RewardService {

    Reward save(Reward reward);

    boolean isRewardClaimed(Long userId, Long tournamentId);
    User claimReward(Long userId, Long tournamentId);
}
