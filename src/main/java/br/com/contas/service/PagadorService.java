package br.com.contas.service;

import br.com.contas.service.dto.PagadorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.contas.domain.Pagador}.
 */
public interface PagadorService {
    /**
     * Save a pagador.
     *
     * @param pagadorDTO the entity to save.
     * @return the persisted entity.
     */
    PagadorDTO save(PagadorDTO pagadorDTO);

    /**
     * Updates a pagador.
     *
     * @param pagadorDTO the entity to update.
     * @return the persisted entity.
     */
    PagadorDTO update(PagadorDTO pagadorDTO);

    /**
     * Partially updates a pagador.
     *
     * @param pagadorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PagadorDTO> partialUpdate(PagadorDTO pagadorDTO);

    /**
     * Get all the pagadors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PagadorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pagador.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PagadorDTO> findOne(Long id);

    /**
     * Delete the "id" pagador.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
