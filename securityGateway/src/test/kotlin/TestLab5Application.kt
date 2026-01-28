import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.with
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import wa.lab5.Lab5Application
import wa.lab5.TestLab5Application

@TestConfiguration(proxyBeanMethods = false)
class TestLab5Application {

	@Bean
	@ServiceConnection
	fun postgresContainer(): PostgreSQLContainer<*> {
		return PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
	}

}

fun main(args: Array<String>) {
	fromApplication<Lab5Application>().with(TestLab5Application::class).run(*args)
}
