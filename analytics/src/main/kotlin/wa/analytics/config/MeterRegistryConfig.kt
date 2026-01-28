package wa.analytics.config

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MeterRegistryConfig {

    @Bean
    fun meterRegistry(): MeterRegistry {
        return SimpleMeterRegistry() // Usa SimpleMeterRegistry per uso locale
    }
}