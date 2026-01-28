package wa.analytics.service.JobOfferMetrics

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import wa.analytics.dto.AnalyticMetricDTO
import wa.analytics.dto.toDto
import wa.analytics.exceptions.AnalyticMetricsNotFoundException
import wa.analytics.model.AnalyticMetric
import wa.analytics.repository.AnalyticMetricsRepository

@Service
@Transactional
class JobOfferMetricsService(
    private val analyticRepo: AnalyticMetricsRepository
) : IJobOfferMetricsService{
    override fun addSkill(name: String): AnalyticMetricDTO {
        val skill = analyticRepo.findAnalyticMetricByName(name) ?: AnalyticMetric(name = name, count = 0)

        skill.apply {
            this.count += 1;
        }

        return analyticRepo.save(skill).toDto()
    }

    override fun retrieveSkills(): List<AnalyticMetricDTO> {

        return analyticRepo.findAll().map { it -> it.toDto() }
    }

    override fun getSkill(): AnalyticMetricDTO {
        TODO("Not yet implemented")
    }

    override fun removeSkill(name: String) {
        val skill = analyticRepo.findAnalyticMetricByName(name) ?: throw AnalyticMetricsNotFoundException(analyticId = name)

        skill.apply {
            this.count -= 1;
        }

        if (skill.count == 0){
            analyticRepo.delete(skill);
        }
    }
}