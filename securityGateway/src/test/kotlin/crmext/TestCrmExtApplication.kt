package org.example.crmext

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import wa.lab5.Lab5Application

@TestConfiguration(proxyBeanMethods = false)
class TestCrmExtApplication {
	@Bean
	@ServiceConnection
	fun postgresContainer(): PostgreSQLContainer<*> {
		return PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
			.apply {
				withDatabaseName("mydatabase")
				withUsername("myuser")
				withPassword("secret")

			}
	}
}


fun main(args: Array<String>) {
	fromApplication<Lab5Application>().with(TestCrmExtApplication::class).run(*args)
}
