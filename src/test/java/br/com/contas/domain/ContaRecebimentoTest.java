package br.com.contas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaRecebimentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaRecebimento.class);
        ContaRecebimento contaRecebimento1 = new ContaRecebimento();
        contaRecebimento1.setId(1L);
        ContaRecebimento contaRecebimento2 = new ContaRecebimento();
        contaRecebimento2.setId(contaRecebimento1.getId());
        assertThat(contaRecebimento1).isEqualTo(contaRecebimento2);
        contaRecebimento2.setId(2L);
        assertThat(contaRecebimento1).isNotEqualTo(contaRecebimento2);
        contaRecebimento1.setId(null);
        assertThat(contaRecebimento1).isNotEqualTo(contaRecebimento2);
    }
}
