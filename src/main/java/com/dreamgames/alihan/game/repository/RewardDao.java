package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface RewardDao extends JpaRepository<Reward, Long> {

    @Query(value = "SELECT * FROM reward r WHERE r.user_id = :userId AND r.tournament_id = :tournamentId", nativeQuery = true)
    Optional<Reward> getRewardByUserAndTournamentId(@Param("userId") Long userId, @Param("tournamentId") Long tournamentId);

    @Query(value = "SELECT * FROM reward r WHERE r.user_id = :userId", nativeQuery = true)
    Optional<Reward> getRewardByUserId(@Param("userId") Long userId);
}
