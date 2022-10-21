package com.dreamgames.alihan.game.model;

import lombok.Builder;

@Builder
public record LeaderBoardDTO(
        Long userId,
        String username,
        double tournamentScore,
        long position) {
}
