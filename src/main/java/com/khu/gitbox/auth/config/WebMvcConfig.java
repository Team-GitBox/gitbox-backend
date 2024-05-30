package com.khu.gitbox.auth.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		log.info("CORS 설정 적용");
		registry.addMapping("/**")
			.allowedOrigins(getAllowOrigins())
			.allowedHeaders("*")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
			.allowCredentials(true);
	}

	private String[] getAllowOrigins() {
		return Arrays.asList(
			"http://localhost",
			"http://localhost:3000",
			"http://localhost:1234",
			"http://localhost:5173",
			"http://125.250.17.196:1234"
		).toArray(String[]::new);
	}
}

