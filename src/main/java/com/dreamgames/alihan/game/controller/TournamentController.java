package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.CreateTournamentRequest;
import com.dreamgames.alihan.game.model.EnterTournamentRequest;
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
    @Autowired
    private RedisService redisService;

    @Operation(summary = "Creates a tournament and returns its id for internal testing purposes")
    @PostMapping("/create")
    public ResponseEntity<Long> createTournament(CreateTournamentRequest createTournamentRequest) {
        log.info("createTournament is called with: {}", createTournamentRequest);
        return ResponseEntity.ok(tournamentService.startTournament(createTournamentRequest));
    }

    @Operation(summary = "Gets full leaderboard data of a tournament group")
    @GetMapping("/getGlobalLeaderBoard")
    public ResponseEntity<List<LeaderBoard>> getGlobalLeaderBoard() {
        log.info("getGlobalLeader is called");
        return ResponseEntity.ok(redisService.getGlobalLeaderBoard());
    }

    @Operation(summary = "Gets user specific leaderboard data of the tournament")
    @GetMapping("/getGlobalLeaderBoard")
    public ResponseEntity<LeaderBoard> getUserSpecificLeaderBoard(Long userId) {
        log.info("getGlobalLeader is called");
        return ResponseEntity.ok(redisService.getUserLeaderBoard(userId.toString()));
    }


    @Operation(summary = "Enters current tournament and returns current group leaderboard")
    @PostMapping("/join")
    public ResponseEntity<?> enterTournament(@Valid @RequestBody EnterTournamentRequest enterTournamentRequest) {
        // TODO(simply logic to the service layer)
        log.info("enterTournament is called with: {}", enterTournamentRequest);
        User user = userService.findUserById(enterTournamentRequest.getUserId());
//        if (user.getCoin() < 1_000L || user.getLevel() < 20) {
//            return ResponseEntity.ok("You are not eligible to enter to the tournament");
//        }
        Tournament currentTournament = tournamentService.getTournamentById(enterTournamentRequest.getTournamentId());
        currentTournament.addUser(user);
//
//        Team team = Team.builder().id(Long.parseLong(UUID.randomUUID().toString())).build();
//        team.addUser(user);
        tournamentService.save(currentTournament);
        redisService.save(user);
        return ResponseEntity.ok(redisService.getGroupLeaderBoard(user.getId().toString()));
    }
}
