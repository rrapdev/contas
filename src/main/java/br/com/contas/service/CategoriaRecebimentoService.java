package br.com.contas.service;

import br.com.contas.service.dto.CategoriaRecebimentoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.contas.domain.CategoriaRecebimento}.
 */
public interface CategoriaRecebimentoService {
    /**
     * Save a categoriaRecebimento.
     *
     * @param categoriaRecebimentoDTO the entity to save.
     * @return the persisted entity.
     */
    CategoriaRecebimentoDTO save(CategoriaRecebimentoDTO categoriaRecebimentoDTO);

    /**
     * Updates a categoriaRecebimento.
     *
     * @param categoriaRecebimentoDTO the entity to update.
     * @return the persisted entity.
     */
    CategoriaRecebimentoDTO update(CategoriaRecebimentoDTO categoriaRecebimentoDTO);

    /**
     * Partially updates a categoriaRecebimento.
     *
     * @param categoriaRecebimentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoriaRecebimentoDTO> partialUpdate(CategoriaRecebimentoDTO categoriaRecebimentoDTO);

    /**
     * Get all the categoriaRecebimentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CategoriaRecebimentoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" categoriaRecebimento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoriaRecebimentoDTO> findOne(Long id);

    /**
     * Delete the "id" categoriaRecebimento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
