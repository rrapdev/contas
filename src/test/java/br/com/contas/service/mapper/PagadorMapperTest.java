package br.com.contas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PagadorMapperTest {

    private PagadorMapper pagadorMapper;

    @BeforeEach
    public void setUp() {
        pagadorMapper = new PagadorMapperImpl();
    }
}
