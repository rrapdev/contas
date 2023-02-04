package br.com.contas.service;

import br.com.contas.service.dto.ContaPagamentoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.contas.domain.ContaPagamento}.
 */
public interface ContaPagamentoService {
    /**
     * Save a contaPagamento.
     *
     * @param contaPagamentoDTO the entity to save.
     * @return the persisted entity.
     */
    ContaPagamentoDTO save(ContaPagamentoDTO contaPagamentoDTO);

    /**
     * Updates a contaPagamento.
     *
     * @param contaPagamentoDTO the entity to update.
     * @return the persisted entity.
     */
    ContaPagamentoDTO update(ContaPagamentoDTO contaPagamentoDTO);

    /**
     * Partially updates a contaPagamento.
     *
     * @param contaPagamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContaPagamentoDTO> partialUpdate(ContaPagamentoDTO contaPagamentoDTO);

    /**
     * Get all the contaPagamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContaPagamentoDTO> findAll(Pageable pageable);

    /**
     * Get all the contaPagamentos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContaPagamentoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" contaPagamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContaPagamentoDTO> findOne(Long id);

    /**
     * Delete the "id" contaPagamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
