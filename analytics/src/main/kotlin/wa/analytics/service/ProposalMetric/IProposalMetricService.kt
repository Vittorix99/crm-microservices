package wa.analytics.service.ProposalMetric

import wa.analytics.dto.ProposalMetricDTO

interface IProposalMetricService {

    fun addProposalMetric(description: String, status: String, id: String): ProposalMetricDTO

    fun getAllProposals(): List<ProposalMetricDTO>

    fun updateProposalMetric(id: String, newStatus: String): ProposalMetricDTO

    fun deleteProposalMetric(id: String)
}