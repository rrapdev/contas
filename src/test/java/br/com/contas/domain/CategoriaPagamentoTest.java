package br.com.contas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaPagamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaPagamento.class);
        CategoriaPagamento categoriaPagamento1 = new CategoriaPagamento();
        categoriaPagamento1.setId(1L);
        CategoriaPagamento categoriaPagamento2 = new CategoriaPagamento();
        categoriaPagamento2.setId(categoriaPagamento1.getId());
        assertThat(categoriaPagamento1).isEqualTo(categoriaPagamento2);
        categoriaPagamento2.setId(2L);
        assertThat(categoriaPagamento1).isNotEqualTo(categoriaPagamento2);
        categoriaPagamento1.setId(null);
        assertThat(categoriaPagamento1).isNotEqualTo(categoriaPagamento2);
    }
}
