package br.com.contas.repository;

import br.com.contas.domain.CategoriaPagamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoriaPagamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaPagamentoRepository
    extends JpaRepository<CategoriaPagamento, Long>, JpaSpecificationExecutor<CategoriaPagamento> {}
