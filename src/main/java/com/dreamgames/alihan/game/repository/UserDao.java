package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    @Query(value = "SELECT * from dream_user u INNER JOIN tournament t ON u.tournament_id =:tournamentId where t.state = 'STARTED'",
            nativeQuery = true)
    Set<User> usersInTournament(@Param("tournamentId") Long tournamentId);
}
