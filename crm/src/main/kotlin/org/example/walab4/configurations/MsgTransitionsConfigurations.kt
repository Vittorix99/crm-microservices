package org.example.walab4.configurations

import org.example.walab4.model.message.MessageStatus
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "transitions")
@EnableConfigurationProperties
class MsgTransitionsConfigurations() {
    var received = arrayOf(MessageStatus.READ)
    var read = arrayOf(MessageStatus.PROCESSING, MessageStatus.DONE, MessageStatus.FAILED)
    var processing = arrayOf(MessageStatus.DONE, MessageStatus.FAILED)
    var done = arrayOf<MessageStatus>()
    var discarded = arrayOf<MessageStatus>()
    var failed = arrayOf<MessageStatus>()
}