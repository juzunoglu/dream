package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaderBoardDao extends CrudRepository<LeaderBoard, Long> {
    Optional<LeaderBoard> findByUserId(Long userId);
    Optional<LeaderBoard> findByUserIdAndTournamentId(Long userId, Long tournamentId);

}
