package com.dreamgames.alihan.game.service;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.exception.UserNotFoundException;
import com.dreamgames.alihan.game.model.CreateUserRequest;
import com.dreamgames.alihan.game.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        User userToBeSaved = User.builder()
                .name(createUserRequest.getName())
                .level(1)
                .coin(5_000L)
                .build();

        return userDao.save(userToBeSaved);
    }

    @Override
    public User updateUserLevel(Long updateLevelRequest) {
        User userToBeUpdated = userDao.findById(updateLevelRequest)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));

        userToBeUpdated.setCoin(userToBeUpdated.addCoin(25L));
        userToBeUpdated.setLevel(userToBeUpdated.incrementLevel());
        return userDao.save(userToBeUpdated);
    }
}
