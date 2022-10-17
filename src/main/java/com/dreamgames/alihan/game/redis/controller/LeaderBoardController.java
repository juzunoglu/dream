package com.dreamgames.alihan.game.redis.controller;

import com.dreamgames.alihan.game.redis.model.LeaderBoard;
import com.dreamgames.alihan.game.redis.service.LeaderBoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/leaderboard")
public class LeaderBoardController {

    private final LeaderBoardService leaderBoardService;

    public LeaderBoardController(LeaderBoardService leaderBoardService) {
        this.leaderBoardService = leaderBoardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaderBoard> getLeaderBoardById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(leaderBoardService.findById(id));
    }


    @GetMapping("/all")
    public ResponseEntity<Object> getAllLeaderBoards() {
        return ResponseEntity.ok(leaderBoardService.findAll());
    }


    @PostMapping
    public ResponseEntity<LeaderBoard> createLeaderBoard(@RequestBody LeaderBoard leaderBoard) {
        return ResponseEntity.ok(leaderBoardService.save(leaderBoard));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Boolean> deleteLeaderBoard(@PathVariable("id") Long id) {
        return ResponseEntity.ok(leaderBoardService.delete(id));
    }
}
