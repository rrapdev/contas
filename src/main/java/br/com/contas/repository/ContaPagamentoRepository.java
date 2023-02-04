package br.com.contas.repository;

import br.com.contas.domain.ContaPagamento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContaPagamento entity.
 */
@Repository
public interface ContaPagamentoRepository extends JpaRepository<ContaPagamento, Long>, JpaSpecificationExecutor<ContaPagamento> {
    default Optional<ContaPagamento> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ContaPagamento> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ContaPagamento> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct contaPagamento from ContaPagamento contaPagamento left join fetch contaPagamento.beneficiario left join fetch contaPagamento.categoriaPagamento left join fetch contaPagamento.caixa",
        countQuery = "select count(distinct contaPagamento) from ContaPagamento contaPagamento"
    )
    Page<ContaPagamento> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct contaPagamento from ContaPagamento contaPagamento left join fetch contaPagamento.beneficiario left join fetch contaPagamento.categoriaPagamento left join fetch contaPagamento.caixa"
    )
    List<ContaPagamento> findAllWithToOneRelationships();

    @Query(
        "select contaPagamento from ContaPagamento contaPagamento left join fetch contaPagamento.beneficiario left join fetch contaPagamento.categoriaPagamento left join fetch contaPagamento.caixa where contaPagamento.id =:id"
    )
    Optional<ContaPagamento> findOneWithToOneRelationships(@Param("id") Long id);
}
