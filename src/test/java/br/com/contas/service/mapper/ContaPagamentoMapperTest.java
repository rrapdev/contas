package br.com.contas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaPagamentoMapperTest {

    private ContaPagamentoMapper contaPagamentoMapper;

    @BeforeEach
    public void setUp() {
        contaPagamentoMapper = new ContaPagamentoMapperImpl();
    }
}
