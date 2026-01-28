package wa.analytics

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<AnalyticsApplication>().with(TestcontainersConfiguration::class).run(*args)
}
