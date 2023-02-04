package br.com.contas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaixaMapperTest {

    private CaixaMapper caixaMapper;

    @BeforeEach
    public void setUp() {
        caixaMapper = new CaixaMapperImpl();
    }
}
