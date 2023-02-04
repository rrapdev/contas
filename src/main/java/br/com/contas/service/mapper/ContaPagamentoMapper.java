package br.com.contas.service.mapper;

import br.com.contas.domain.Beneficiario;
import br.com.contas.domain.Caixa;
import br.com.contas.domain.CategoriaPagamento;
import br.com.contas.domain.ContaPagamento;
import br.com.contas.service.dto.BeneficiarioDTO;
import br.com.contas.service.dto.CaixaDTO;
import br.com.contas.service.dto.CategoriaPagamentoDTO;
import br.com.contas.service.dto.ContaPagamentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContaPagamento} and its DTO {@link ContaPagamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContaPagamentoMapper extends EntityMapper<ContaPagamentoDTO, ContaPagamento> {
    @Mapping(target = "beneficiario", source = "beneficiario", qualifiedByName = "beneficiarioNomeBeneficiario")
    @Mapping(target = "categoriaPagamento", source = "categoriaPagamento", qualifiedByName = "categoriaPagamentoNomeCategoria")
    @Mapping(target = "caixa", source = "caixa", qualifiedByName = "caixaNomeCaixa")
    ContaPagamentoDTO toDto(ContaPagamento s);

    @Named("beneficiarioNomeBeneficiario")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeBeneficiario", source = "nomeBeneficiario")
    BeneficiarioDTO toDtoBeneficiarioNomeBeneficiario(Beneficiario beneficiario);

    @Named("categoriaPagamentoNomeCategoria")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeCategoria", source = "nomeCategoria")
    CategoriaPagamentoDTO toDtoCategoriaPagamentoNomeCategoria(CategoriaPagamento categoriaPagamento);

    @Named("caixaNomeCaixa")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeCaixa", source = "nomeCaixa")
    CaixaDTO toDtoCaixaNomeCaixa(Caixa caixa);
}
