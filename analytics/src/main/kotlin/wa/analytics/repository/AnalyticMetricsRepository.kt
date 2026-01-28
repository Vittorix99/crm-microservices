package wa.analytics.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import wa.analytics.model.AnalyticMetric

@Repository
interface AnalyticMetricsRepository: JpaRepository<AnalyticMetric, Long> {

    fun findAnalyticMetricByName(name: String): AnalyticMetric?;
}