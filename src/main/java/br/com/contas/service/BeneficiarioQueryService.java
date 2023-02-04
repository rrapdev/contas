package br.com.contas.service;

import br.com.contas.domain.*; // for static metamodels
import br.com.contas.domain.Beneficiario;
import br.com.contas.repository.BeneficiarioRepository;
import br.com.contas.service.criteria.BeneficiarioCriteria;
import br.com.contas.service.dto.BeneficiarioDTO;
import br.com.contas.service.mapper.BeneficiarioMapper;
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
 * Service for executing complex queries for {@link Beneficiario} entities in the database.
 * The main input is a {@link BeneficiarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BeneficiarioDTO} or a {@link Page} of {@link BeneficiarioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BeneficiarioQueryService extends QueryService<Beneficiario> {

    private final Logger log = LoggerFactory.getLogger(BeneficiarioQueryService.class);

    private final BeneficiarioRepository beneficiarioRepository;

    private final BeneficiarioMapper beneficiarioMapper;

    public BeneficiarioQueryService(BeneficiarioRepository beneficiarioRepository, BeneficiarioMapper beneficiarioMapper) {
        this.beneficiarioRepository = beneficiarioRepository;
        this.beneficiarioMapper = beneficiarioMapper;
    }

    /**
     * Return a {@link List} of {@link BeneficiarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BeneficiarioDTO> findByCriteria(BeneficiarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Beneficiario> specification = createSpecification(criteria);
        return beneficiarioMapper.toDto(beneficiarioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BeneficiarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BeneficiarioDTO> findByCriteria(BeneficiarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Beneficiario> specification = createSpecification(criteria);
        return beneficiarioRepository.findAll(specification, page).map(beneficiarioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BeneficiarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Beneficiario> specification = createSpecification(criteria);
        return beneficiarioRepository.count(specification);
    }

    /**
     * Function to convert {@link BeneficiarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Beneficiario> createSpecification(BeneficiarioCriteria criteria) {
        Specification<Beneficiario> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Beneficiario_.id));
            }
            if (criteria.getNomeBeneficiario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomeBeneficiario(), Beneficiario_.nomeBeneficiario));
            }
            if (criteria.getCpfCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpfCnpj(), Beneficiario_.cpfCnpj));
            }
        }
        return specification;
    }
}
