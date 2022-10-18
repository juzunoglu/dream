package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface TournamentDao extends JpaRepository<Tournament, Long> {

    @Query(value = "SELECT * FROM Tournament t where d.stata = STARTED", nativeQuery = true)
    Tournament getCurrentTournament();

}
