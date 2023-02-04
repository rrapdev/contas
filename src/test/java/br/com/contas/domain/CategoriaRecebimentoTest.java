package br.com.contas.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaRecebimentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaRecebimento.class);
        CategoriaRecebimento categoriaRecebimento1 = new CategoriaRecebimento();
        categoriaRecebimento1.setId(1L);
        CategoriaRecebimento categoriaRecebimento2 = new CategoriaRecebimento();
        categoriaRecebimento2.setId(categoriaRecebimento1.getId());
        assertThat(categoriaRecebimento1).isEqualTo(categoriaRecebimento2);
        categoriaRecebimento2.setId(2L);
        assertThat(categoriaRecebimento1).isNotEqualTo(categoriaRecebimento2);
        categoriaRecebimento1.setId(null);
        assertThat(categoriaRecebimento1).isNotEqualTo(categoriaRecebimento2);
    }
}
