package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.model.CreateUserRequest;

public interface UserService {

    User createUser(CreateUserRequest createUserRequest);

    User updateUserLevel(Long updateLevelRequest);
}
