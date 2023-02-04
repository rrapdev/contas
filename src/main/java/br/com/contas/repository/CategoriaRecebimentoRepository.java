package br.com.contas.repository;

import br.com.contas.domain.CategoriaRecebimento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoriaRecebimento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaRecebimentoRepository
    extends JpaRepository<CategoriaRecebimento, Long>, JpaSpecificationExecutor<CategoriaRecebimento> {}
