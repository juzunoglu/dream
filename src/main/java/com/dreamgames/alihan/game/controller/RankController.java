package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/rank")
@Slf4j
public class RankController {

    @Operation(summary = "Gets rank of a specific player for a specific tournament")
    @GetMapping(name = "/{userId}/{tournamentId}")
    public ResponseEntity<User> getTournamentRank(@PathVariable("userId") Long userId, @PathVariable("tournamentId") Long tournamentId) {
        return null;
    }

}
