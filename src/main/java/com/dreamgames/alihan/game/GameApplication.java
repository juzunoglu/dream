package com.dreamgames.alihan.game;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "Dream Games API", description = "Short Demo API for Dream Games interview"))
public class GameApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameApplication.class, args);
	}

}
