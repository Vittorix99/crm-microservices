package wa.analytics.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import wa.analytics.manager.MetricManager
import wa.analytics.service.JobOfferMetrics.IJobOfferMetricsService
import wa.analytics.service.ProposalMetric.ProposalMetricService

@Service
class KafkaListeners(
    private val metricManager: MetricManager,
    private val analyticService: IJobOfferMetricsService,
    private val proposalMetricService: ProposalMetricService
    ) {

    private val objectMapper = jacksonObjectMapper()

    @KafkaListener(topics = ["job_offer_skill"], groupId = "job_offer")
    fun listenNewSkill(message: String){
        try{
            metricManager.incrementCounter(message)
            analyticService.addSkill(message)
        } catch (e: Exception){
            metricManager.incrementErrorCounter("job_offer_skills")
            println("Errore nel processing del messaggio: ${e.message}")
        }
    }

    @KafkaListener(topics = ["delete_job_offer_skill"], groupId = "job_offer")
    fun deleteSkill(message: String){
        try {
            metricManager.incrementCounter(message);
            analyticService.removeSkill(message)
        } catch (e:Exception){
            metricManager.incrementErrorCounter("job_offer_skills")
            println("Errore nel processing del messaggio: ${e.message}")
        }
    }

    @KafkaListener(topics = ["proposals"], groupId = "proposals")
    fun listenNewProposal(message: String){
        try {
            val messageData: Map<String, String> = objectMapper.readValue(message)
            val description = messageData["description"]
            val status = messageData["status"]
            val id = messageData["id"]

            proposalMetricService.addProposalMetric(description ?: "", status ?: "", id ?: "")
        } catch (e: Exception){
            metricManager.incrementErrorCounter("proposals")
            println("Errore nel processing del messaggio: ${e.message}")
        }
    }

    @KafkaListener(topics = ["delete_proposals"], groupId = "proposals")
    fun deleteProposal(message: String){
        try {
            val messageData: Map<String, String> = objectMapper.readValue(message)
            val id = messageData["id"]

            proposalMetricService.deleteProposalMetric(id ?: "")

        } catch (e:Exception){
            metricManager.incrementErrorCounter("proposals")
            println("Errore nel processing del messaggio: ${e.message}")
        }
    }

    @KafkaListener(topics = ["update_proposals"], groupId = "proposals")
    fun updateProposals(message: String){
        try {
            val messageData: Map<String, String> = objectMapper.readValue(message)
            val id = messageData["id"]
            val newStatus = messageData["newStatus"]

            proposalMetricService.updateProposalMetric(id ?: "", newStatus ?: "")

        } catch (e:Exception){
            metricManager.incrementErrorCounter("proposals")
            println("Errore nel processing del messaggio: ${e.message}")
        }
    }
}