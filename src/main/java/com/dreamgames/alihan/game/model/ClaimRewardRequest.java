package com.dreamgames.alihan.game.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data

public class ClaimRewardRequest {

    @NotBlank(message = "User ID  cannot be null or empty")
    @Schema(description = "Id of the User claiming the reward",
            name = "userId",
            required = true)
    private Long userId;

    @NotBlank(message = "Tournament id cannot be null or empty")
    @Schema(description = "Id of the Tournament",
            name = "tournamentId",
            required = true)
    private Long tournamentId;
}
