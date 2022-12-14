package com.dreamgames.alihan.game.service.impl;

import com.dreamgames.alihan.game.entity.User;
import com.dreamgames.alihan.game.entity.enumaration.GroupLevelMapping;
import com.dreamgames.alihan.game.exception.UserNotFoundException;
import com.dreamgames.alihan.game.model.CreateUserRequest;
import com.dreamgames.alihan.game.redis.service.RedisService;
import com.dreamgames.alihan.game.repository.UserDao;
import com.dreamgames.alihan.game.service.TournamentGroupService;
import com.dreamgames.alihan.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TournamentGroupService tournamentGroupService;

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        User userToBeSaved = User.builder()
                .name(createUserRequest.getName())
                .tournamentScore(0L)
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
        if (isUserInTournament(userToBeUpdated)) {
            userToBeUpdated.incrementTournamentScore();
            redisService.updateUserScore(userToBeUpdated);
            return userDao.save(userToBeUpdated);
        }
        assignUserToAGroup(userToBeUpdated);
        return userDao.save(userToBeUpdated);
    }

    private boolean isUserInTournament(User user) {
        if (user.getTournament() == null) return false;
        return userDao.usersInTournament(user.getTournament().getId()).size() >= 1;
    }

    private void assignUserToAGroup(User user) { //todo??
        if (user.getLevel() < GroupLevelMapping.GROUP1.getLevelLimit()) {
            tournamentGroupService.findByGroupLevel(1).addUser(user);
        } else if (user.getLevel() < GroupLevelMapping.GROUP2.getLevelLimit()) {
            tournamentGroupService.findByGroupLevel(2).addUser(user);
        } else if (user.getLevel() < GroupLevelMapping.GROUP3.getLevelLimit()) {
            tournamentGroupService.findByGroupLevel(3).addUser(user);
        } else if (user.getLevel() < GroupLevelMapping.GROUP4.getLevelLimit()) {
            tournamentGroupService.findByGroupLevel(4).addUser(user);
        } else if (user.getLevel() < GroupLevelMapping.GROUP5.getLevelLimit()) {
            tournamentGroupService.findByGroupLevel(5).addUser(user);
        } else if (user.getLevel() < GroupLevelMapping.GROUP6.getLevelLimit()) {
            tournamentGroupService.findByGroupLevel(6).addUser(user);
        }
    }

    @Override
    public User findUserById(Long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
    }

    @Override
    public void payTournamentFee(User user, Long tournamentFee) {
        user.payTournamentFee(tournamentFee);
        userDao.save(user);
    }

    @Override
    public boolean isUserAlreadyInTournament(User user) {
        return user.getTournament() != null;
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> getAllUsersByTournamentId(Long tournamentId) {
       return userDao.usersInTournament(tournamentId).stream().toList();
    }

}
