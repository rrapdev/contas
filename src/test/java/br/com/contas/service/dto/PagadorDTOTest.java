package br.com.contas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.contas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PagadorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PagadorDTO.class);
        PagadorDTO pagadorDTO1 = new PagadorDTO();
        pagadorDTO1.setId(1L);
        PagadorDTO pagadorDTO2 = new PagadorDTO();
        assertThat(pagadorDTO1).isNotEqualTo(pagadorDTO2);
        pagadorDTO2.setId(pagadorDTO1.getId());
        assertThat(pagadorDTO1).isEqualTo(pagadorDTO2);
        pagadorDTO2.setId(2L);
        assertThat(pagadorDTO1).isNotEqualTo(pagadorDTO2);
        pagadorDTO1.setId(null);
        assertThat(pagadorDTO1).isNotEqualTo(pagadorDTO2);
    }
}
