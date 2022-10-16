package com.dreamgames.alihan.game;

import com.dreamgames.alihan.game.repository.TournamentDao;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableKafka
//@EnableAsync
@OpenAPIDefinition(info = @Info(title = "Dream Games API", description = "Short Demo API for Dream Games interview"))
public class GameApplication implements CommandLineRunner {


	@Autowired
	private final TournamentDao tournamentDao;

	public GameApplication(TournamentDao tournamentDao) {
		this.tournamentDao = tournamentDao;
	}

	public static void main(String[] args) {
		SpringApplication.run(GameApplication.class, args);
	}

	@Override
	public void run(String... args) {

	}
}
