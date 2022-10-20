package com.dreamgames.alihan.game.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EnterTournamentRequest {

    @Schema(description = "User that will enter to the tournament",
            name = "userId",
            required = true)
    private Long userId;

    @Schema(description = "Tournament id to be entered",
            name = "tournamentId",
            required = true)
    private Long tournamentId;
}
