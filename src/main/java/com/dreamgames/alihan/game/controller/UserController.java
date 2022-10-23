package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.CreateUserRequest;
import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.service.LeaderBoardService;
import com.dreamgames.alihan.game.service.UserService;
import com.dreamgames.alihan.game.websocket.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LeaderBoardService leaderBoardService;

    @Operation(summary = "Creates user and returns unique user id, level, and coin")
    @PostMapping(path = "/create")
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.info("createUser is called with: {}", createUserRequest);
        leaderBoardService.save(LeaderBoard.builder().userId(1L)
                .groupName("selam")
                .rank(1D)
                .tournamentId(2L).build());
        return new ResponseEntity<>(userService.createUser(createUserRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Updates level and coin of the user. Returns updated progress data")
    @PutMapping(path = "/update/{userId}")
    public ResponseEntity<User> updateUserLevel(@PathVariable("userId") Long updateLevelRequest) {
        log.info("updateUserLevel is called for userId: {}", updateLevelRequest);
        return new ResponseEntity<>(userService.updateUserLevel(updateLevelRequest), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get all users for internal testing")
    @GetMapping(path = "/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

}
