package br.com.contas.service.mapper;

import br.com.contas.domain.Beneficiario;
import br.com.contas.service.dto.BeneficiarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Beneficiario} and its DTO {@link BeneficiarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface BeneficiarioMapper extends EntityMapper<BeneficiarioDTO, Beneficiario> {}
