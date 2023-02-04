package br.com.contas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaPagamentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaPagamentoDTO.class);
        CategoriaPagamentoDTO categoriaPagamentoDTO1 = new CategoriaPagamentoDTO();
        categoriaPagamentoDTO1.setId(1L);
        CategoriaPagamentoDTO categoriaPagamentoDTO2 = new CategoriaPagamentoDTO();
        assertThat(categoriaPagamentoDTO1).isNotEqualTo(categoriaPagamentoDTO2);
        categoriaPagamentoDTO2.setId(categoriaPagamentoDTO1.getId());
        assertThat(categoriaPagamentoDTO1).isEqualTo(categoriaPagamentoDTO2);
        categoriaPagamentoDTO2.setId(2L);
        assertThat(categoriaPagamentoDTO1).isNotEqualTo(categoriaPagamentoDTO2);
        categoriaPagamentoDTO1.setId(null);
        assertThat(categoriaPagamentoDTO1).isNotEqualTo(categoriaPagamentoDTO2);
    }
}
