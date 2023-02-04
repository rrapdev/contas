package br.com.contas.service.mapper;

import br.com.contas.domain.Pagador;
import br.com.contas.service.dto.PagadorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pagador} and its DTO {@link PagadorDTO}.
 */
@Mapper(componentModel = "spring")
public interface PagadorMapper extends EntityMapper<PagadorDTO, Pagador> {}
