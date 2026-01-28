package wa.analytics.manager

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap


@Component
class MetricManager(private val meterRegistry: MeterRegistry) {

    private val counters: MutableMap<String, Counter> = ConcurrentHashMap()

    fun incrementCounter(topic: String){
        counters.computeIfAbsent(topic){
            meterRegistry.counter("kafka.messages.consumed", "topic", it)
        }.increment()
    }

    fun incrementErrorCounter(topic: String){
        counters.computeIfAbsent("$topic.errors"){
            meterRegistry.counter("Kafka.messages.errors", "topic", it.replace(".errors", ""))
        }.increment()
    }

    fun getCounter(topic: String): String?{
        return counters[topic]?.count().toString()
    }

    fun getErrorCounter(topic: String): String?{
        return counters[topic]?.count().toString()
    }
}