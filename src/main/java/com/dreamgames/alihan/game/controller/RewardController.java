package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.service.RewardService;
import com.dreamgames.alihan.game.service.TournamentService;
import com.dreamgames.alihan.game.service.UserService;
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
    @GetMapping(name = "/{userId}/{tournamentId}")
    public ResponseEntity<User> claimReward(@PathVariable("userId")
                                             Long userId, @PathVariable("tournamentId") Long tournamentId) {
        return new ResponseEntity<>(rewardService.claimReward(userId, tournamentId), HttpStatus.OK);
    }
}
