package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderBoardDao extends JpaRepository<LeaderBoard, Long> {
}
