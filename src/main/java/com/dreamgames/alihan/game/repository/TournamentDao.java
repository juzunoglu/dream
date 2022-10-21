package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentDao extends JpaRepository<Tournament, Long> {

    @Query(value = "SELECT * FROM tournament t where t.state = 'STARTED'", nativeQuery = true)
    Optional<Tournament> getCurrentTournament();

    @Query(value = "SELECT * FROM tournament t where t.state = 'STARTED' and t.id = :id", nativeQuery = true)
    Tournament getTournamentById(@Param("id") Long id);

}
