package wa.analytics.service.JobOfferMetrics

import wa.analytics.dto.AnalyticMetricDTO

interface IJobOfferMetricsService{

    fun addSkill(name: String): AnalyticMetricDTO;

    fun retrieveSkills(): List<AnalyticMetricDTO>;

    fun getSkill(): AnalyticMetricDTO;

    fun removeSkill(name: String);
}