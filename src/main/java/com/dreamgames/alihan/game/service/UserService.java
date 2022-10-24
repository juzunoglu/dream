package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.CreateUserRequest;

import java.util.List;

public interface UserService {

    User createUser(CreateUserRequest createUserRequest);

    User updateUserLevel(Long updateLevelRequest);

    User findUserById(Long userId);

    void payTournamentFee(User user, Long tournamentFee);

    boolean isUserAlreadyInTournament(User user);

    List<User> findAll();

    List<User> getAllUsersByTournamentId(Long tournamentId);

}
