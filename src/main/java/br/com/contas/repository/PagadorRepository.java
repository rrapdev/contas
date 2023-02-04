package br.com.contas.repository;

import br.com.contas.domain.Pagador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pagador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagadorRepository extends JpaRepository<Pagador, Long>, JpaSpecificationExecutor<Pagador> {}
