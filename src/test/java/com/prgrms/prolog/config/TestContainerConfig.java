package com.prgrms.prolog.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainerConfig {

	@Container
	public static MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer("mysql:8")
		.withDatabaseName("test");

	@BeforeAll
	static void beforeAll() {
		MY_SQL_CONTAINER.start();
	}

	@AfterAll
	static void afterAll() {
		MY_SQL_CONTAINER.stop();
	}
}
