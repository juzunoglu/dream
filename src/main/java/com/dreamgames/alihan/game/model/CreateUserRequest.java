package com.dreamgames.alihan.game.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequest {

    @NotBlank(message = "User name cannot be null or empty")
    @Size(min = 2, max = 30, message = "User name must be in between 2 and 30 characters")
    @Schema(description = "Name of the user",
            name = "name",
            required = true,
            example = "alihan")

    private String name;
}
