package wa.analytics.repository

import org.springframework.data.jpa.repository.JpaRepository
import wa.analytics.model.ProposalMetric

interface ProposalMetricRepository: JpaRepository<ProposalMetric, Long> {

    fun findProposalMetricById(proposalId: Long): ProposalMetric?
}