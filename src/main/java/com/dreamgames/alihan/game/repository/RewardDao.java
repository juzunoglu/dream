package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardDao extends JpaRepository<Reward, Long> {

    @Query(value = "SELECT * FROM reward r WHERE r.tournament_id = :tournamentId AND r.user_id = :userId", nativeQuery = true)
    Optional<Reward> getRewardByUserAndTournamentId(@Param("tournamentId") Long tournamentId, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM reward r WHERE r.user_id = :userId", nativeQuery = true)
    Optional<Reward> getRewardByUserId(@Param("userId") Long userId);
}
