package br.com.contas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaRecebimentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaRecebimentoDTO.class);
        ContaRecebimentoDTO contaRecebimentoDTO1 = new ContaRecebimentoDTO();
        contaRecebimentoDTO1.setId(1L);
        ContaRecebimentoDTO contaRecebimentoDTO2 = new ContaRecebimentoDTO();
        assertThat(contaRecebimentoDTO1).isNotEqualTo(contaRecebimentoDTO2);
        contaRecebimentoDTO2.setId(contaRecebimentoDTO1.getId());
        assertThat(contaRecebimentoDTO1).isEqualTo(contaRecebimentoDTO2);
        contaRecebimentoDTO2.setId(2L);
        assertThat(contaRecebimentoDTO1).isNotEqualTo(contaRecebimentoDTO2);
        contaRecebimentoDTO1.setId(null);
        assertThat(contaRecebimentoDTO1).isNotEqualTo(contaRecebimentoDTO2);
    }
}
