package br.com.contas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PagadorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pagador.class);
        Pagador pagador1 = new Pagador();
        pagador1.setId(1L);
        Pagador pagador2 = new Pagador();
        pagador2.setId(pagador1.getId());
        assertThat(pagador1).isEqualTo(pagador2);
        pagador2.setId(2L);
        assertThat(pagador1).isNotEqualTo(pagador2);
        pagador1.setId(null);
        assertThat(pagador1).isNotEqualTo(pagador2);
    }
}
