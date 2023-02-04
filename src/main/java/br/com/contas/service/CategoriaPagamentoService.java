package br.com.contas.service;

import br.com.contas.service.dto.CategoriaPagamentoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.contas.domain.CategoriaPagamento}.
 */
public interface CategoriaPagamentoService {
    /**
     * Save a categoriaPagamento.
     *
     * @param categoriaPagamentoDTO the entity to save.
     * @return the persisted entity.
     */
    CategoriaPagamentoDTO save(CategoriaPagamentoDTO categoriaPagamentoDTO);

    /**
     * Updates a categoriaPagamento.
     *
     * @param categoriaPagamentoDTO the entity to update.
     * @return the persisted entity.
     */
    CategoriaPagamentoDTO update(CategoriaPagamentoDTO categoriaPagamentoDTO);

    /**
     * Partially updates a categoriaPagamento.
     *
     * @param categoriaPagamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoriaPagamentoDTO> partialUpdate(CategoriaPagamentoDTO categoriaPagamentoDTO);

    /**
     * Get all the categoriaPagamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CategoriaPagamentoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" categoriaPagamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoriaPagamentoDTO> findOne(Long id);

    /**
     * Delete the "id" categoriaPagamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
