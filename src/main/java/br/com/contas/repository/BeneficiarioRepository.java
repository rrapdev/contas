package br.com.contas.repository;

import br.com.contas.domain.Beneficiario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Beneficiario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeneficiarioRepository extends JpaRepository<Beneficiario, Long>, JpaSpecificationExecutor<Beneficiario> {}
