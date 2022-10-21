package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.entity.enumaration.GroupLevelMapping;
import com.dreamgames.alihan.game.exception.UserNotFoundException;
import com.dreamgames.alihan.game.model.CreateUserRequest;
import com.dreamgames.alihan.game.repository.UserDao;
import com.dreamgames.alihan.game.service.TournamentGroupService;
import com.dreamgames.alihan.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    private TournamentGroupService tournamentGroupService;

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
        userToBeUpdated.addCoin(25L);
        userToBeUpdated.incrementLevel();
        assignUserToAGroup(userToBeUpdated);
        return userDao.save(userToBeUpdated);
    }
    
    private void assignUserToAGroup(User user) {
        if (user.getLevel() < GroupLevelMapping.GROUP1.getLevelLimit()) {
            tournamentGroupService.findByGroupLevel(1).addUser(user);
        } else if (user.getLevel() < GroupLevelMapping.GROUP2.getLevelLimit()) {
            tournamentGroupService.findByGroupLevel(2).addUser(user);
        } else if (user.getLevel() < GroupLevelMapping.GROUP3.getLevelLimit()) {
            tournamentGroupService.findByGroupLevel(3).addUser(user);
        }
    }

    @Override
    public User findUserById(Long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
    }

    @Override
    public User payTournamentFee(User user, Long tournamentFee) {
        user.payTournamentFee(tournamentFee);
        return userDao.save(user);
    }

}
