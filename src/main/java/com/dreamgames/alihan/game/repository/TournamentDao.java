package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface TournamentDao extends JpaRepository<Tournament, Long> {

    @Query(value = "SELECT * FROM tournament t where t.state = 'STARTED'", nativeQuery = true)
    Optional<Tournament> getCurrentTournament();
}
