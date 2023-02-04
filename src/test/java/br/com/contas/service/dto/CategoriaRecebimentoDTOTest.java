package br.com.contas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaRecebimentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaRecebimentoDTO.class);
        CategoriaRecebimentoDTO categoriaRecebimentoDTO1 = new CategoriaRecebimentoDTO();
        categoriaRecebimentoDTO1.setId(1L);
        CategoriaRecebimentoDTO categoriaRecebimentoDTO2 = new CategoriaRecebimentoDTO();
        assertThat(categoriaRecebimentoDTO1).isNotEqualTo(categoriaRecebimentoDTO2);
        categoriaRecebimentoDTO2.setId(categoriaRecebimentoDTO1.getId());
        assertThat(categoriaRecebimentoDTO1).isEqualTo(categoriaRecebimentoDTO2);
        categoriaRecebimentoDTO2.setId(2L);
        assertThat(categoriaRecebimentoDTO1).isNotEqualTo(categoriaRecebimentoDTO2);
        categoriaRecebimentoDTO1.setId(null);
        assertThat(categoriaRecebimentoDTO1).isNotEqualTo(categoriaRecebimentoDTO2);
    }
}
