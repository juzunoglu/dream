package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.model.CreateTournamentRequest;
import com.dreamgames.alihan.game.model.EnterTournamentRequest;
import com.dreamgames.alihan.game.model.LeaderBoardDTO;
import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.service.TournamentService;
import com.dreamgames.alihan.game.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/tournament")
@Slf4j
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private UserService userService;

    @Operation(summary = "Creates a tournament and returns its id for internal testing purposes")
    @PostMapping("/create")
    public ResponseEntity<Long> createTournament(CreateTournamentRequest createTournamentRequest) {
        log.info("createTournament is called with: {}", createTournamentRequest);
        return ResponseEntity.ok(tournamentService.startTournament(createTournamentRequest));
    }

    @Operation(summary = "Gets full leaderboard data of a tournament group")
    @GetMapping("/getGlobalLeaderBoard")
    public ResponseEntity<List<LeaderBoardDTO>> getGlobalLeaderBoard() {
        log.info("getGlobalLeader is called");
        return ResponseEntity.ok(tournamentService.getGlobalLeaderBoard());
    }

    @Operation(summary = "Enters current tournament and returns current group leaderboard")
    @PostMapping("/join")
    public ResponseEntity<List<LeaderBoardDTO>> enterTournament(@Valid @RequestBody EnterTournamentRequest enterTournamentRequest) {
        log.info("enterTournament is called with: {}", enterTournamentRequest);
        return ResponseEntity.ok(tournamentService.enterTournament(enterTournamentRequest));
    }
}
