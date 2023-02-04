package br.com.contas.service.mapper;

import br.com.contas.domain.CategoriaPagamento;
import br.com.contas.service.dto.CategoriaPagamentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoriaPagamento} and its DTO {@link CategoriaPagamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaPagamentoMapper extends EntityMapper<CategoriaPagamentoDTO, CategoriaPagamento> {}
