package org.example.walab4.configurations

import org.example.walab4.model.jobOffer.ProposalStatus
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "proposaltransitions")
@EnableConfigurationProperties
class ProposalTransitionsConf {
    var pending = arrayOf(ProposalStatus.ACCEPTED, ProposalStatus.ABORTED)
    var accepted = arrayOf(ProposalStatus.ABORTED)
    var aborted = arrayOf<ProposalStatus>()
}