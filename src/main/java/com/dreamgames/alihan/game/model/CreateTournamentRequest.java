package com.dreamgames.alihan.game.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateTournamentRequest {

    @NotBlank(message = "Tournament name cannot be null or empty")
    @Size(min = 2, max = 30, message = "Tournament name must be in between 2 and 30 characters")
    @Schema(description = "Name of the Tournament",
            name = "tournamentName",
            required = true,
            example = "First Tournament")
    private String tournamentName;
}
