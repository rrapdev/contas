package br.com.contas.service;

import br.com.contas.domain.*; // for static metamodels
import br.com.contas.domain.Pagador;
import br.com.contas.repository.PagadorRepository;
import br.com.contas.service.criteria.PagadorCriteria;
import br.com.contas.service.dto.PagadorDTO;
import br.com.contas.service.mapper.PagadorMapper;
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
 * Service for executing complex queries for {@link Pagador} entities in the database.
 * The main input is a {@link PagadorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PagadorDTO} or a {@link Page} of {@link PagadorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PagadorQueryService extends QueryService<Pagador> {

    private final Logger log = LoggerFactory.getLogger(PagadorQueryService.class);

    private final PagadorRepository pagadorRepository;

    private final PagadorMapper pagadorMapper;

    public PagadorQueryService(PagadorRepository pagadorRepository, PagadorMapper pagadorMapper) {
        this.pagadorRepository = pagadorRepository;
        this.pagadorMapper = pagadorMapper;
    }

    /**
     * Return a {@link List} of {@link PagadorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PagadorDTO> findByCriteria(PagadorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pagador> specification = createSpecification(criteria);
        return pagadorMapper.toDto(pagadorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PagadorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PagadorDTO> findByCriteria(PagadorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pagador> specification = createSpecification(criteria);
        return pagadorRepository.findAll(specification, page).map(pagadorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PagadorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pagador> specification = createSpecification(criteria);
        return pagadorRepository.count(specification);
    }

    /**
     * Function to convert {@link PagadorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pagador> createSpecification(PagadorCriteria criteria) {
        Specification<Pagador> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pagador_.id));
            }
            if (criteria.getNomePagador() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomePagador(), Pagador_.nomePagador));
            }
            if (criteria.getCpfCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpfCnpj(), Pagador_.cpfCnpj));
            }
        }
        return specification;
    }
}
