package br.com.contas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoriaRecebimentoMapperTest {

    private CategoriaRecebimentoMapper categoriaRecebimentoMapper;

    @BeforeEach
    public void setUp() {
        categoriaRecebimentoMapper = new CategoriaRecebimentoMapperImpl();
    }
}
