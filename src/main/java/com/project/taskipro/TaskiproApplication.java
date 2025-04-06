package com.project.taskipro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskiproApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskiproApplication.class, args);
	}
}