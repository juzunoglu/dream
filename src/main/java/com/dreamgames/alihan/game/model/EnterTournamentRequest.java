package com.dreamgames.alihan.game.model;

import com.dreamgames.alihan.game.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EnterTournamentRequest {

    @Schema(description = "User that will enter to the tournament",
            name = "user",
            required = true)
    private User user;
}
