package br.com.contas.service;

import br.com.contas.domain.*; // for static metamodels
import br.com.contas.domain.Caixa;
import br.com.contas.repository.CaixaRepository;
import br.com.contas.service.criteria.CaixaCriteria;
import br.com.contas.service.dto.CaixaDTO;
import br.com.contas.service.mapper.CaixaMapper;
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
 * Service for executing complex queries for {@link Caixa} entities in the database.
 * The main input is a {@link CaixaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CaixaDTO} or a {@link Page} of {@link CaixaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CaixaQueryService extends QueryService<Caixa> {

    private final Logger log = LoggerFactory.getLogger(CaixaQueryService.class);

    private final CaixaRepository caixaRepository;

    private final CaixaMapper caixaMapper;

    public CaixaQueryService(CaixaRepository caixaRepository, CaixaMapper caixaMapper) {
        this.caixaRepository = caixaRepository;
        this.caixaMapper = caixaMapper;
    }

    /**
     * Return a {@link List} of {@link CaixaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CaixaDTO> findByCriteria(CaixaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Caixa> specification = createSpecification(criteria);
        return caixaMapper.toDto(caixaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CaixaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CaixaDTO> findByCriteria(CaixaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Caixa> specification = createSpecification(criteria);
        return caixaRepository.findAll(specification, page).map(caixaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CaixaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Caixa> specification = createSpecification(criteria);
        return caixaRepository.count(specification);
    }

    /**
     * Function to convert {@link CaixaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Caixa> createSpecification(CaixaCriteria criteria) {
        Specification<Caixa> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Caixa_.id));
            }
            if (criteria.getNomeCaixa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeCaixa(), Caixa_.nomeCaixa));
            }
        }
        return specification;
    }
}
