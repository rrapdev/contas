package br.com.contas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContaPagamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaPagamento.class);
        ContaPagamento contaPagamento1 = new ContaPagamento();
        contaPagamento1.setId(1L);
        ContaPagamento contaPagamento2 = new ContaPagamento();
        contaPagamento2.setId(contaPagamento1.getId());
        assertThat(contaPagamento1).isEqualTo(contaPagamento2);
        contaPagamento2.setId(2L);
        assertThat(contaPagamento1).isNotEqualTo(contaPagamento2);
        contaPagamento1.setId(null);
        assertThat(contaPagamento1).isNotEqualTo(contaPagamento2);
    }
}
