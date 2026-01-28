package org.example.walab4.configurations

import org.example.walab4.model.jobOffer.JobOfferStatus
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "joboffertransitions")
@EnableConfigurationProperties
class JobOfferTransitionsConf {

    var created = arrayOf(JobOfferStatus.ABORTED, JobOfferStatus.SELECTION_PHASE)
    var selectionPhase = arrayOf(JobOfferStatus.ABORTED, JobOfferStatus.CANDIDATE_PROPOSAL)
    var candidateProposal = arrayOf(JobOfferStatus.ABORTED, JobOfferStatus.CONSOLIDATED, JobOfferStatus.SELECTION_PHASE)
    var consolidated = arrayOf(JobOfferStatus.ABORTED, JobOfferStatus.SELECTION_PHASE, JobOfferStatus.DONE)
    var done = arrayOf<JobOfferStatus>()
    var aborted = arrayOf<JobOfferStatus>()
}