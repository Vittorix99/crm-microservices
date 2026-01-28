package org.example.walab4.services.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class KafkaService(private val kafkaTemplate: KafkaTemplate<String, String>): IKafkaService{
    override fun sendMessage(topic: String, message: String) {
        kafkaTemplate.send(topic, message)
        println("Message sent to Kafka. $message")
    }
}