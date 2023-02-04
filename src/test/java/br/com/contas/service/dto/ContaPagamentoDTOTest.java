package br.com.contas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaPagamentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaPagamentoDTO.class);
        ContaPagamentoDTO contaPagamentoDTO1 = new ContaPagamentoDTO();
        contaPagamentoDTO1.setId(1L);
        ContaPagamentoDTO contaPagamentoDTO2 = new ContaPagamentoDTO();
        assertThat(contaPagamentoDTO1).isNotEqualTo(contaPagamentoDTO2);
        contaPagamentoDTO2.setId(contaPagamentoDTO1.getId());
        assertThat(contaPagamentoDTO1).isEqualTo(contaPagamentoDTO2);
        contaPagamentoDTO2.setId(2L);
        assertThat(contaPagamentoDTO1).isNotEqualTo(contaPagamentoDTO2);
        contaPagamentoDTO1.setId(null);
        assertThat(contaPagamentoDTO1).isNotEqualTo(contaPagamentoDTO2);
    }
}
