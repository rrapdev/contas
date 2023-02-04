package br.com.contas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContaRecebimentoMapperTest {

    private ContaRecebimentoMapper contaRecebimentoMapper;

    @BeforeEach
    public void setUp() {
        contaRecebimentoMapper = new ContaRecebimentoMapperImpl();
    }
}
