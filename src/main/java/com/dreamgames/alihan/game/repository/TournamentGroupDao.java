package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentGroupDao extends JpaRepository<TournamentGroup, Long> {

    @Query(value = "SELECT * FROM tournament_group tg where tg.level = :level", nativeQuery = true)
    Optional<TournamentGroup> getTournamentByLevel(@Param("level") int level);
}
