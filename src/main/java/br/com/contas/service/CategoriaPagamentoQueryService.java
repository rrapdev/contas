package br.com.contas.service;

import br.com.contas.domain.*; // for static metamodels
import br.com.contas.domain.CategoriaPagamento;
import br.com.contas.repository.CategoriaPagamentoRepository;
import br.com.contas.service.criteria.CategoriaPagamentoCriteria;
import br.com.contas.service.dto.CategoriaPagamentoDTO;
import br.com.contas.service.mapper.CategoriaPagamentoMapper;
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
 * Service for executing complex queries for {@link CategoriaPagamento} entities in the database.
 * The main input is a {@link CategoriaPagamentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CategoriaPagamentoDTO} or a {@link Page} of {@link CategoriaPagamentoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoriaPagamentoQueryService extends QueryService<CategoriaPagamento> {

    private final Logger log = LoggerFactory.getLogger(CategoriaPagamentoQueryService.class);

    private final CategoriaPagamentoRepository categoriaPagamentoRepository;

    private final CategoriaPagamentoMapper categoriaPagamentoMapper;

    public CategoriaPagamentoQueryService(
        CategoriaPagamentoRepository categoriaPagamentoRepository,
        CategoriaPagamentoMapper categoriaPagamentoMapper
    ) {
        this.categoriaPagamentoRepository = categoriaPagamentoRepository;
        this.categoriaPagamentoMapper = categoriaPagamentoMapper;
    }

    /**
     * Return a {@link List} of {@link CategoriaPagamentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CategoriaPagamentoDTO> findByCriteria(CategoriaPagamentoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CategoriaPagamento> specification = createSpecification(criteria);
        return categoriaPagamentoMapper.toDto(categoriaPagamentoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CategoriaPagamentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoriaPagamentoDTO> findByCriteria(CategoriaPagamentoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoriaPagamento> specification = createSpecification(criteria);
        return categoriaPagamentoRepository.findAll(specification, page).map(categoriaPagamentoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoriaPagamentoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CategoriaPagamento> specification = createSpecification(criteria);
        return categoriaPagamentoRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoriaPagamentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoriaPagamento> createSpecification(CategoriaPagamentoCriteria criteria) {
        Specification<CategoriaPagamento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoriaPagamento_.id));
            }
            if (criteria.getNomeCategoria() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeCategoria(), CategoriaPagamento_.nomeCategoria));
            }
        }
        return specification;
    }
}
