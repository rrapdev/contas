package br.com.contas.service.mapper;

import br.com.contas.domain.CategoriaRecebimento;
import br.com.contas.service.dto.CategoriaRecebimentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoriaRecebimento} and its DTO {@link CategoriaRecebimentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaRecebimentoMapper extends EntityMapper<CategoriaRecebimentoDTO, CategoriaRecebimento> {}
