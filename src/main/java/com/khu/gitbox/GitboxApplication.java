package com.khu.gitbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GitboxApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitboxApplication.class, args);
    }

}
