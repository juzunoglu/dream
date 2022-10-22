package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.service.TournamentService;
import com.dreamgames.alihan.game.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/reward")
@Slf4j
public class RewardController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private TournamentService tournamentService;

    @GetMapping(name = "/{userId}/{tournamentId}")
    public ResponseEntity<?> claimReward(@PathVariable("userId")
                                             Long userId, @PathVariable("tournamentId") Long tournamentId) {

//        String userTeam = userService.findUserById(userId)
//                .getTeam()
//                .getName();
//        redisService.
        return null;
    }
}
