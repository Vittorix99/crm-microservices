package org.example.walab4

import org.apache.camel.ProducerTemplate
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.KafkaProducer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.Properties
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate

@SpringBootApplication
class WaLab4Application{
	@Bean
	fun topic() = NewTopic("topic1", 10, 1)

	@Bean
	fun runner(template: KafkaTemplate<String?, String?>) = ApplicationRunner{
		template.send("topicA", "Proviamo il nuovo Kafka")
		template.send("topicB", "Proviamo anche il secondo listener")
	}
}

fun main(args: Array<String>) {

	runApplication<WaLab4Application>(*args)

}


