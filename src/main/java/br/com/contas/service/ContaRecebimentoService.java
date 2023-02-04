package br.com.contas.service;

import br.com.contas.service.dto.ContaRecebimentoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.contas.domain.ContaRecebimento}.
 */
public interface ContaRecebimentoService {
    /**
     * Save a contaRecebimento.
     *
     * @param contaRecebimentoDTO the entity to save.
     * @return the persisted entity.
     */
    ContaRecebimentoDTO save(ContaRecebimentoDTO contaRecebimentoDTO);

    /**
     * Updates a contaRecebimento.
     *
     * @param contaRecebimentoDTO the entity to update.
     * @return the persisted entity.
     */
    ContaRecebimentoDTO update(ContaRecebimentoDTO contaRecebimentoDTO);

    /**
     * Partially updates a contaRecebimento.
     *
     * @param contaRecebimentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContaRecebimentoDTO> partialUpdate(ContaRecebimentoDTO contaRecebimentoDTO);

    /**
     * Get all the contaRecebimentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContaRecebimentoDTO> findAll(Pageable pageable);

    /**
     * Get all the contaRecebimentos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContaRecebimentoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" contaRecebimento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContaRecebimentoDTO> findOne(Long id);

    /**
     * Delete the "id" contaRecebimento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
