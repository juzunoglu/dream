package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.CreateUserRequest;

import java.util.List;

public interface UserService {

    User createUser(CreateUserRequest createUserRequest);

    User updateUserLevel(Long updateLevelRequest);

    User findUserById(Long userId);

    User payTournamentFee(User user, Long tournamentFee);

    List<User> findAll();

}
