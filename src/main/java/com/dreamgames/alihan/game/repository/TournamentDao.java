package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.Tournament;
import com.dreamgames.alihan.game.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface TournamentDao extends JpaRepository<Tournament, Long> {

    @Query(value = "SELECT * FROM tournament t where t.state = 'STARTED'", nativeQuery = true)
    Optional<Tournament> getCurrentTournament();

    @Query(value = "SELECT * FROM tournament t where t.state = 'STARTED' and t.id = :id", nativeQuery = true)
    Optional<Tournament> getTournamentById(@Param("id") Long id);

}
