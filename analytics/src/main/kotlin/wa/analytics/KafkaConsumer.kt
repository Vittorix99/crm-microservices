package wa.analytics

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.Properties

fun consumeMessages(){
    val props = Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ConsumerConfig.GROUP_ID_CONFIG, "kotlin-consumer-group")
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    }

    // Creazione del consumer
    val consumer = KafkaConsumer<String, String>(props)

    // Sottoscrizione al topic
    val topic = "sample-topic"
    consumer.subscribe(listOf(topic))

    // Lettura dei Messaggi
    while (true){
        val records = consumer.poll(Duration.ofMillis(100))
        for (record in records) {
            println("Ricevuto messaggio: chiave = ${record.key()}, valore = ${record.value()}, partizione = ${record.partition()}")
        }
    }
}