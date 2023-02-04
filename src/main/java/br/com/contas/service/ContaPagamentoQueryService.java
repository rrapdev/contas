package br.com.contas.service;

import br.com.contas.domain.*; // for static metamodels
import br.com.contas.domain.ContaPagamento;
import br.com.contas.repository.ContaPagamentoRepository;
import br.com.contas.service.criteria.ContaPagamentoCriteria;
import br.com.contas.service.dto.ContaPagamentoDTO;
import br.com.contas.service.mapper.ContaPagamentoMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ContaPagamento} entities in the database.
 * The main input is a {@link ContaPagamentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContaPagamentoDTO} or a {@link Page} of {@link ContaPagamentoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContaPagamentoQueryService extends QueryService<ContaPagamento> {

    private final Logger log = LoggerFactory.getLogger(ContaPagamentoQueryService.class);

    private final ContaPagamentoRepository contaPagamentoRepository;

    private final ContaPagamentoMapper contaPagamentoMapper;

    public ContaPagamentoQueryService(ContaPagamentoRepository contaPagamentoRepository, ContaPagamentoMapper contaPagamentoMapper) {
        this.contaPagamentoRepository = contaPagamentoRepository;
        this.contaPagamentoMapper = contaPagamentoMapper;
    }

    /**
     * Return a {@link List} of {@link ContaPagamentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContaPagamentoDTO> findByCriteria(ContaPagamentoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContaPagamento> specification = createSpecification(criteria);
        return contaPagamentoMapper.toDto(contaPagamentoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContaPagamentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContaPagamentoDTO> findByCriteria(ContaPagamentoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContaPagamento> specification = createSpecification(criteria);
        return contaPagamentoRepository.findAll(specification, page).map(contaPagamentoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContaPagamentoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContaPagamento> specification = createSpecification(criteria);
        return contaPagamentoRepository.count(specification);
    }

    /**
     * Function to convert {@link ContaPagamentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContaPagamento> createSpecification(ContaPagamentoCriteria criteria) {
        Specification<ContaPagamento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContaPagamento_.id));
            }
            if (criteria.getDataVencimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataVencimento(), ContaPagamento_.dataVencimento));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), ContaPagamento_.descricao));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), ContaPagamento_.valor));
            }
            if (criteria.getDataPagamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataPagamento(), ContaPagamento_.dataPagamento));
            }
            if (criteria.getValorPago() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorPago(), ContaPagamento_.valorPago));
            }
            if (criteria.getObservacoes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacoes(), ContaPagamento_.observacoes));
            }
            if (criteria.getPeriodicidade() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodicidade(), ContaPagamento_.periodicidade));
            }
            if (criteria.getBeneficiarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBeneficiarioId(),
                            root -> root.join(ContaPagamento_.beneficiario, JoinType.LEFT).get(Beneficiario_.id)
                        )
                    );
            }
            if (criteria.getCategoriaPagamentoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaPagamentoId(),
                            root -> root.join(ContaPagamento_.categoriaPagamento, JoinType.LEFT).get(CategoriaPagamento_.id)
                        )
                    );
            }
            if (criteria.getCaixaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCaixaId(), root -> root.join(ContaPagamento_.caixa, JoinType.LEFT).get(Caixa_.id))
                    );
            }
        }
        return specification;
    }
}
