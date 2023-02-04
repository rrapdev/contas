package br.com.contas.repository;

import br.com.contas.domain.ContaRecebimento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContaRecebimento entity.
 */
@Repository
public interface ContaRecebimentoRepository extends JpaRepository<ContaRecebimento, Long>, JpaSpecificationExecutor<ContaRecebimento> {
    default Optional<ContaRecebimento> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ContaRecebimento> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ContaRecebimento> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct contaRecebimento from ContaRecebimento contaRecebimento left join fetch contaRecebimento.pagador left join fetch contaRecebimento.categoriaRecebimento left join fetch contaRecebimento.caixa",
        countQuery = "select count(distinct contaRecebimento) from ContaRecebimento contaRecebimento"
    )
    Page<ContaRecebimento> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct contaRecebimento from ContaRecebimento contaRecebimento left join fetch contaRecebimento.pagador left join fetch contaRecebimento.categoriaRecebimento left join fetch contaRecebimento.caixa"
    )
    List<ContaRecebimento> findAllWithToOneRelationships();

    @Query(
        "select contaRecebimento from ContaRecebimento contaRecebimento left join fetch contaRecebimento.pagador left join fetch contaRecebimento.categoriaRecebimento left join fetch contaRecebimento.caixa where contaRecebimento.id =:id"
    )
    Optional<ContaRecebimento> findOneWithToOneRelationships(@Param("id") Long id);
}
