package org.example.walab4.services.kafka

interface IKafkaService {

    fun sendMessage(topic: String, message: String)
}