package br.com.contas.service;

import br.com.contas.domain.*; // for static metamodels
import br.com.contas.domain.CategoriaRecebimento;
import br.com.contas.repository.CategoriaRecebimentoRepository;
import br.com.contas.service.criteria.CategoriaRecebimentoCriteria;
import br.com.contas.service.dto.CategoriaRecebimentoDTO;
import br.com.contas.service.mapper.CategoriaRecebimentoMapper;
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
 * Service for executing complex queries for {@link CategoriaRecebimento} entities in the database.
 * The main input is a {@link CategoriaRecebimentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CategoriaRecebimentoDTO} or a {@link Page} of {@link CategoriaRecebimentoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoriaRecebimentoQueryService extends QueryService<CategoriaRecebimento> {

    private final Logger log = LoggerFactory.getLogger(CategoriaRecebimentoQueryService.class);

    private final CategoriaRecebimentoRepository categoriaRecebimentoRepository;

    private final CategoriaRecebimentoMapper categoriaRecebimentoMapper;

    public CategoriaRecebimentoQueryService(
        CategoriaRecebimentoRepository categoriaRecebimentoRepository,
        CategoriaRecebimentoMapper categoriaRecebimentoMapper
    ) {
        this.categoriaRecebimentoRepository = categoriaRecebimentoRepository;
        this.categoriaRecebimentoMapper = categoriaRecebimentoMapper;
    }

    /**
     * Return a {@link List} of {@link CategoriaRecebimentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CategoriaRecebimentoDTO> findByCriteria(CategoriaRecebimentoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CategoriaRecebimento> specification = createSpecification(criteria);
        return categoriaRecebimentoMapper.toDto(categoriaRecebimentoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CategoriaRecebimentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoriaRecebimentoDTO> findByCriteria(CategoriaRecebimentoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoriaRecebimento> specification = createSpecification(criteria);
        return categoriaRecebimentoRepository.findAll(specification, page).map(categoriaRecebimentoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoriaRecebimentoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CategoriaRecebimento> specification = createSpecification(criteria);
        return categoriaRecebimentoRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoriaRecebimentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoriaRecebimento> createSpecification(CategoriaRecebimentoCriteria criteria) {
        Specification<CategoriaRecebimento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoriaRecebimento_.id));
            }
            if (criteria.getNomeCategoria() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getNomeCategoria(), CategoriaRecebimento_.nomeCategoria));
            }
        }
        return specification;
    }
}
