package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.redis.service.LeaderBoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/v1/rank")
@Slf4j
public class RankController {

    @Autowired
    private LeaderBoardService leaderBoardService;

    @Operation(summary = "Gets rank of a specific player for a specific tournament")
    @GetMapping(value = "/{userId}/{tournamentId}")
    public ResponseEntity<Long> getTournamentRank(@PathVariable Long userId, @PathVariable Long tournamentId) {
        log.info("getTournamentRank() is called for user: {}, and for tournament: {}", userId, tournamentId);
        return new ResponseEntity<>(leaderBoardService.getPositionInTournament(userId, tournamentId), HttpStatus.OK);
    }
}
