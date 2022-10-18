package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.model.EnterTournamentRequest;
import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/tournament")
@Slf4j
public class TournamentController {


    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private RedisService redisService;

    @Operation(summary = "Enters current tournament and returns current group leaderboard")
    @PostMapping("/join")
    public ResponseEntity<?> enterTournament(@Valid @RequestBody EnterTournamentRequest enterTournamentRequest) {
        log.info("createUser is called with: {}", enterTournamentRequest);
        if (enterTournamentRequest.getUser().getCoin() < 1_000L || enterTournamentRequest.getUser().getLevel() < 20) {
            return ResponseEntity.ok("You are not eligible to enter to the tournament");
        }
        Tournament currentTournament = tournamentService.getCurrentTournament();
        currentTournament.setParticipants(List.of(enterTournamentRequest.getUser()));
        redisService.save(enterTournamentRequest.getUser());
        return ResponseEntity
                .ok(redisService.getUserLeaderBoard(enterTournamentRequest.getUser().getId().toString()));
    }
}
