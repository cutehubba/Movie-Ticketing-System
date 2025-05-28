package com.movie.ticketing.userservice;

import com.movie.ticketing.userservice.config.CorsGlobalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import(CorsGlobalConfig.class)  // 新增此行
public class MovieBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(MovieBackendApplication.class, args);
	}
}