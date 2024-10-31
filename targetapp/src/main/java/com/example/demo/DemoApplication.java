package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import t1.homework.services.AdequateTestingService;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication implements CommandLineRunner {
	private final ApplicationContext ctx;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//
		// для начала оттестируем что наш стартер адекватно приплыл и проперти свои притащил
		// (без заданных свойств оно у нас тащить должен те что из стартера, с заданными из цели)
		var adequateService = ctx.getBean(AdequateTestingService.class);
		adequateService.printProperties();

	}
}
