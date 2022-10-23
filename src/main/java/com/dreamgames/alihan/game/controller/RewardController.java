package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.ClaimRewardRequest;
import com.dreamgames.alihan.game.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/reward")
@Slf4j
public class RewardController {


    @Autowired
    private RewardService rewardService;
    @Operation(summary = "Claims reward of a specific tournament and returns updated progress data")
    @PutMapping(name = "/claim")
    public ResponseEntity<User> claimReward(ClaimRewardRequest claimRewardRequest) {
        return new ResponseEntity<>(rewardService.claimReward(claimRewardRequest.getUserId(), claimRewardRequest.getTournamentId()), HttpStatus.OK);
    }
}
