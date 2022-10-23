package com.dreamgames.alihan.game;


import com.dreamgames.alihan.game.entity.TournamentGroup;
import com.dreamgames.alihan.game.service.TournamentGroupService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
@EnableRedisRepositories
@EnableAsync
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Dream Games API", description = "Short Demo API for Dream Games interview"))
public class GameApplication implements CommandLineRunner {

	@Autowired
	private TournamentGroupService tournamentGroupService;

	public static void main(String[] args) {
		SpringApplication.run(GameApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("RedisHash-");
		executor.initialize();
		return executor;
	}


	@Override
	public void run(String... args) {
		if (tournamentGroupService.findAll().size() < 1) {
			for (int i = 1; i < 20; i++) {
				TournamentGroup tournamentGroup = TournamentGroup.builder()
						.name("Group".concat(Integer.toString(i)))
						.level(i)
						.build();
				tournamentGroupService.save(tournamentGroup);
			}
		}
		List<TournamentGroup> tournamentGroupList = tournamentGroupService.findAll();
		log.info("Defined Tournament groups are: {}", tournamentGroupList);
	}
}
