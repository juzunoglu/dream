package com.dreamgames.alihan.game.controller;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.job.producer.Producer;
import com.dreamgames.alihan.game.model.CreateUserRequest;
import com.dreamgames.alihan.game.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/v1/user")
@Slf4j
public class UserController {

    private final UserService userService;

    private final Producer producer;
    @Autowired
    public UserController(UserService userService, Producer producer) {
        this.userService = userService;
        this.producer = producer;
    }


    @Operation(summary = "Creates user and returns unique user id, level, and coin")
    @PostMapping(path = "/create")
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.info("createUser is called with: {}", createUserRequest);
        this.producer.publishMessage("selam");
        return new ResponseEntity<>(userService.createUser(createUserRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Updates level and coin of the user. Returns updated progress data")
    @PutMapping(path = "/update/{userId}")
    public ResponseEntity<User> updateUserLevel(@PathVariable("userId") Long updateLevelRequest) {
        log.info("updateUserLevel is called for userId: {}", updateLevelRequest);
        return new ResponseEntity<>(userService.updateUserLevel(updateLevelRequest), HttpStatus.ACCEPTED);
    }
}
