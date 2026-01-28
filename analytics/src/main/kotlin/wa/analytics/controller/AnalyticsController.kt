package wa.analytics.controller

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import wa.analytics.dto.AnalyticMetricDTO
import wa.analytics.dto.ProposalMetricDTO
import wa.analytics.service.JobOfferMetrics.IJobOfferMetricsService
import wa.analytics.service.ProposalMetric.ProposalMetricService

@Validated
@RestController
@RequestMapping("/analytics")
class AnalyticsController(
    private val analyticService: IJobOfferMetricsService,
    private val proposalMetricService: ProposalMetricService
) {

    private val LOGGER: Logger = LogManager.getLogger()

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllSkills(): List<AnalyticMetricDTO>{

        val skills = analyticService.retrieveSkills()
        LOGGER.info("[GET - API/analytics] - SUCCESS - Analytics retrieved correctly")

        return skills;
    }

    @GetMapping("/proposals")
    @ResponseStatus(HttpStatus.OK)
    fun getAllProposals(): List<ProposalMetricDTO>{

        val proposals = proposalMetricService.getAllProposals()
        LOGGER.info("[GET - API/analytics/proposals] - SUCCESS - Proposals retrieved correctly")
        return proposals
    }


}