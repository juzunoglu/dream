package com.dreamgames.alihan.game.repository;

import com.dreamgames.alihan.game.entity.LeaderBoard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderBoardDao extends CrudRepository<LeaderBoard, Long> {
}
