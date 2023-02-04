package br.com.contas.service.mapper;

import br.com.contas.domain.Caixa;
import br.com.contas.domain.CategoriaRecebimento;
import br.com.contas.domain.ContaRecebimento;
import br.com.contas.domain.Pagador;
import br.com.contas.service.dto.CaixaDTO;
import br.com.contas.service.dto.CategoriaRecebimentoDTO;
import br.com.contas.service.dto.ContaRecebimentoDTO;
import br.com.contas.service.dto.PagadorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContaRecebimento} and its DTO {@link ContaRecebimentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContaRecebimentoMapper extends EntityMapper<ContaRecebimentoDTO, ContaRecebimento> {
    @Mapping(target = "pagador", source = "pagador", qualifiedByName = "pagadorNomePagador")
    @Mapping(target = "categoriaRecebimento", source = "categoriaRecebimento", qualifiedByName = "categoriaRecebimentoNomeCategoria")
    @Mapping(target = "caixa", source = "caixa", qualifiedByName = "caixaNomeCaixa")
    ContaRecebimentoDTO toDto(ContaRecebimento s);

    @Named("pagadorNomePagador")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomePagador", source = "nomePagador")
    PagadorDTO toDtoPagadorNomePagador(Pagador pagador);

    @Named("categoriaRecebimentoNomeCategoria")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeCategoria", source = "nomeCategoria")
    CategoriaRecebimentoDTO toDtoCategoriaRecebimentoNomeCategoria(CategoriaRecebimento categoriaRecebimento);

    @Named("caixaNomeCaixa")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomeCaixa", source = "nomeCaixa")
    CaixaDTO toDtoCaixaNomeCaixa(Caixa caixa);
}
