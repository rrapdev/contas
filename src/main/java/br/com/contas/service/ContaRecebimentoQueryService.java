package br.com.contas.service;

import br.com.contas.domain.*; // for static metamodels
import br.com.contas.domain.ContaRecebimento;
import br.com.contas.repository.ContaRecebimentoRepository;
import br.com.contas.service.criteria.ContaRecebimentoCriteria;
import br.com.contas.service.dto.ContaRecebimentoDTO;
import br.com.contas.service.mapper.ContaRecebimentoMapper;
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
 * Service for executing complex queries for {@link ContaRecebimento} entities in the database.
 * The main input is a {@link ContaRecebimentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContaRecebimentoDTO} or a {@link Page} of {@link ContaRecebimentoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContaRecebimentoQueryService extends QueryService<ContaRecebimento> {

    private final Logger log = LoggerFactory.getLogger(ContaRecebimentoQueryService.class);

    private final ContaRecebimentoRepository contaRecebimentoRepository;

    private final ContaRecebimentoMapper contaRecebimentoMapper;

    public ContaRecebimentoQueryService(
        ContaRecebimentoRepository contaRecebimentoRepository,
        ContaRecebimentoMapper contaRecebimentoMapper
    ) {
        this.contaRecebimentoRepository = contaRecebimentoRepository;
        this.contaRecebimentoMapper = contaRecebimentoMapper;
    }

    /**
     * Return a {@link List} of {@link ContaRecebimentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContaRecebimentoDTO> findByCriteria(ContaRecebimentoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContaRecebimento> specification = createSpecification(criteria);
        return contaRecebimentoMapper.toDto(contaRecebimentoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContaRecebimentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContaRecebimentoDTO> findByCriteria(ContaRecebimentoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContaRecebimento> specification = createSpecification(criteria);
        return contaRecebimentoRepository.findAll(specification, page).map(contaRecebimentoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContaRecebimentoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContaRecebimento> specification = createSpecification(criteria);
        return contaRecebimentoRepository.count(specification);
    }

    /**
     * Function to convert {@link ContaRecebimentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContaRecebimento> createSpecification(ContaRecebimentoCriteria criteria) {
        Specification<ContaRecebimento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContaRecebimento_.id));
            }
            if (criteria.getDataVencimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataVencimento(), ContaRecebimento_.dataVencimento));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), ContaRecebimento_.descricao));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), ContaRecebimento_.valor));
            }
            if (criteria.getDataRecebimento() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDataRecebimento(), ContaRecebimento_.dataRecebimento));
            }
            if (criteria.getValorRecebido() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorRecebido(), ContaRecebimento_.valorRecebido));
            }
            if (criteria.getObservacoes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacoes(), ContaRecebimento_.observacoes));
            }
            if (criteria.getPeriodicidade() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodicidade(), ContaRecebimento_.periodicidade));
            }
            if (criteria.getPagadorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPagadorId(),
                            root -> root.join(ContaRecebimento_.pagador, JoinType.LEFT).get(Pagador_.id)
                        )
                    );
            }
            if (criteria.getCategoriaRecebimentoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaRecebimentoId(),
                            root -> root.join(ContaRecebimento_.categoriaRecebimento, JoinType.LEFT).get(CategoriaRecebimento_.id)
                        )
                    );
            }
            if (criteria.getCaixaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCaixaId(), root -> root.join(ContaRecebimento_.caixa, JoinType.LEFT).get(Caixa_.id))
                    );
            }
        }
        return specification;
    }
}
