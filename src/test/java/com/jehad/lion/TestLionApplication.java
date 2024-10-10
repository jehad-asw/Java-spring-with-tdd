package com.jehad.lion;

import org.springframework.boot.SpringApplication;

public class TestLionApplication {

	public static void main(String[] args) {
		SpringApplication.from(LionApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
