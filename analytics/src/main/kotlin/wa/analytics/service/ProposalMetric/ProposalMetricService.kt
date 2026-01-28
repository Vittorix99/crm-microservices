package wa.analytics.service.ProposalMetric

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import wa.analytics.dto.ProposalMetricDTO
import wa.analytics.dto.toDto
import wa.analytics.exceptions.InvalidIdException
import wa.analytics.model.ProposalMetric
import wa.analytics.repository.ProposalMetricRepository
import java.time.LocalDate

@Service
@Transactional
class ProposalMetricService(
    private val proposalMetricRepository: ProposalMetricRepository
): IProposalMetricService {
    override fun addProposalMetric(description: String, status: String, id: String): ProposalMetricDTO {
        val proposal = ProposalMetric(
            id = id.toLong(),
            description = description,
            status = status,
            time = LocalDate.now()
        )

        return proposalMetricRepository.save(proposal).toDto()
    }

    override fun getAllProposals(): List<ProposalMetricDTO> {
        return proposalMetricRepository.findAll().map { it.toDto() }
    }

    override fun updateProposalMetric(id: String, newStatus: String): ProposalMetricDTO {
        val proposalMetric = proposalMetricRepository.findProposalMetricById(id.toLong()) ?: throw InvalidIdException();

        proposalMetric.apply {
            proposalMetric.status = newStatus
        }

        proposalMetricRepository.save(proposalMetric)

        return proposalMetric.toDto()
    }

    override fun deleteProposalMetric(id: String) {
        val proposalMetric = proposalMetricRepository.findProposalMetricById(id.toLong()) ?: throw InvalidIdException();

        proposalMetricRepository.delete(proposalMetric)
    }
}