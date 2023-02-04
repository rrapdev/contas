package br.com.contas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoriaPagamentoMapperTest {

    private CategoriaPagamentoMapper categoriaPagamentoMapper;

    @BeforeEach
    public void setUp() {
        categoriaPagamentoMapper = new CategoriaPagamentoMapperImpl();
    }
}
