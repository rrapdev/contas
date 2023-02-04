package br.com.contas.service.mapper;

import br.com.contas.domain.Caixa;
import br.com.contas.service.dto.CaixaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Caixa} and its DTO {@link CaixaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CaixaMapper extends EntityMapper<CaixaDTO, Caixa> {}
