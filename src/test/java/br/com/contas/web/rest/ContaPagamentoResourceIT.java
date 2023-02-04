package br.com.contas.web.rest;

import static br.com.contas.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.contas.IntegrationTest;
import br.com.contas.domain.Beneficiario;
import br.com.contas.domain.Caixa;
import br.com.contas.domain.CategoriaPagamento;
import br.com.contas.domain.ContaPagamento;
import br.com.contas.domain.enumeration.Periodicidade;
import br.com.contas.repository.ContaPagamentoRepository;
import br.com.contas.service.ContaPagamentoService;
import br.com.contas.service.criteria.ContaPagamentoCriteria;
import br.com.contas.service.dto.ContaPagamentoDTO;
import br.com.contas.service.mapper.ContaPagamentoMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContaPagamentoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContaPagamentoResourceIT {

    private static final LocalDate DEFAULT_DATA_VENCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_VENCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_VENCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_DATA_PAGAMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PAGAMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_PAGAMENTO = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_VALOR_PAGO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGO = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_PAGO = new BigDecimal(1 - 1);

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    private static final Periodicidade DEFAULT_PERIODICIDADE = Periodicidade.ESPORADICA;
    private static final Periodicidade UPDATED_PERIODICIDADE = Periodicidade.MENSAL;

    private static final String ENTITY_API_URL = "/api/conta-pagamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContaPagamentoRepository contaPagamentoRepository;

    @Mock
    private ContaPagamentoRepository contaPagamentoRepositoryMock;

    @Autowired
    private ContaPagamentoMapper contaPagamentoMapper;

    @Mock
    private ContaPagamentoService contaPagamentoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContaPagamentoMockMvc;

    private ContaPagamento contaPagamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaPagamento createEntity(EntityManager em) {
        ContaPagamento contaPagamento = new ContaPagamento()
            .dataVencimento(DEFAULT_DATA_VENCIMENTO)
            .descricao(DEFAULT_DESCRICAO)
            .valor(DEFAULT_VALOR)
            .dataPagamento(DEFAULT_DATA_PAGAMENTO)
            .valorPago(DEFAULT_VALOR_PAGO)
            .observacoes(DEFAULT_OBSERVACOES)
            .periodicidade(DEFAULT_PERIODICIDADE);
        return contaPagamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaPagamento createUpdatedEntity(EntityManager em) {
        ContaPagamento contaPagamento = new ContaPagamento()
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dataPagamento(UPDATED_DATA_PAGAMENTO)
            .valorPago(UPDATED_VALOR_PAGO)
            .observacoes(UPDATED_OBSERVACOES)
            .periodicidade(UPDATED_PERIODICIDADE);
        return contaPagamento;
    }

    @BeforeEach
    public void initTest() {
        contaPagamento = createEntity(em);
    }

    @Test
    @Transactional
    void createContaPagamento() throws Exception {
        int databaseSizeBeforeCreate = contaPagamentoRepository.findAll().size();
        // Create the ContaPagamento
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(contaPagamento);
        restContaPagamentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeCreate + 1);
        ContaPagamento testContaPagamento = contaPagamentoList.get(contaPagamentoList.size() - 1);
        assertThat(testContaPagamento.getDataVencimento()).isEqualTo(DEFAULT_DATA_VENCIMENTO);
        assertThat(testContaPagamento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testContaPagamento.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
        assertThat(testContaPagamento.getDataPagamento()).isEqualTo(DEFAULT_DATA_PAGAMENTO);
        assertThat(testContaPagamento.getValorPago()).isEqualByComparingTo(DEFAULT_VALOR_PAGO);
        assertThat(testContaPagamento.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);
        assertThat(testContaPagamento.getPeriodicidade()).isEqualTo(DEFAULT_PERIODICIDADE);
    }

    @Test
    @Transactional
    void createContaPagamentoWithExistingId() throws Exception {
        // Create the ContaPagamento with an existing ID
        contaPagamento.setId(1L);
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(contaPagamento);

        int databaseSizeBeforeCreate = contaPagamentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaPagamentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataVencimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaPagamentoRepository.findAll().size();
        // set the field null
        contaPagamento.setDataVencimento(null);

        // Create the ContaPagamento, which fails.
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(contaPagamento);

        restContaPagamentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContaPagamentos() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList
        restContaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataVencimento").value(hasItem(DEFAULT_DATA_VENCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].dataPagamento").value(hasItem(DEFAULT_DATA_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].valorPago").value(hasItem(sameNumber(DEFAULT_VALOR_PAGO))))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)))
            .andExpect(jsonPath("$.[*].periodicidade").value(hasItem(DEFAULT_PERIODICIDADE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContaPagamentosWithEagerRelationshipsIsEnabled() throws Exception {
        when(contaPagamentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContaPagamentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contaPagamentoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContaPagamentosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contaPagamentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContaPagamentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contaPagamentoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContaPagamento() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get the contaPagamento
        restContaPagamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, contaPagamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contaPagamento.getId().intValue()))
            .andExpect(jsonPath("$.dataVencimento").value(DEFAULT_DATA_VENCIMENTO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.dataPagamento").value(DEFAULT_DATA_PAGAMENTO.toString()))
            .andExpect(jsonPath("$.valorPago").value(sameNumber(DEFAULT_VALOR_PAGO)))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES))
            .andExpect(jsonPath("$.periodicidade").value(DEFAULT_PERIODICIDADE.toString()));
    }

    @Test
    @Transactional
    void getContaPagamentosByIdFiltering() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        Long id = contaPagamento.getId();

        defaultContaPagamentoShouldBeFound("id.equals=" + id);
        defaultContaPagamentoShouldNotBeFound("id.notEquals=" + id);

        defaultContaPagamentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContaPagamentoShouldNotBeFound("id.greaterThan=" + id);

        defaultContaPagamentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContaPagamentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataVencimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataVencimento equals to DEFAULT_DATA_VENCIMENTO
        defaultContaPagamentoShouldBeFound("dataVencimento.equals=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaPagamentoList where dataVencimento equals to UPDATED_DATA_VENCIMENTO
        defaultContaPagamentoShouldNotBeFound("dataVencimento.equals=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataVencimentoIsInShouldWork() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataVencimento in DEFAULT_DATA_VENCIMENTO or UPDATED_DATA_VENCIMENTO
        defaultContaPagamentoShouldBeFound("dataVencimento.in=" + DEFAULT_DATA_VENCIMENTO + "," + UPDATED_DATA_VENCIMENTO);

        // Get all the contaPagamentoList where dataVencimento equals to UPDATED_DATA_VENCIMENTO
        defaultContaPagamentoShouldNotBeFound("dataVencimento.in=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataVencimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataVencimento is not null
        defaultContaPagamentoShouldBeFound("dataVencimento.specified=true");

        // Get all the contaPagamentoList where dataVencimento is null
        defaultContaPagamentoShouldNotBeFound("dataVencimento.specified=false");
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataVencimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataVencimento is greater than or equal to DEFAULT_DATA_VENCIMENTO
        defaultContaPagamentoShouldBeFound("dataVencimento.greaterThanOrEqual=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaPagamentoList where dataVencimento is greater than or equal to UPDATED_DATA_VENCIMENTO
        defaultContaPagamentoShouldNotBeFound("dataVencimento.greaterThanOrEqual=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataVencimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataVencimento is less than or equal to DEFAULT_DATA_VENCIMENTO
        defaultContaPagamentoShouldBeFound("dataVencimento.lessThanOrEqual=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaPagamentoList where dataVencimento is less than or equal to SMALLER_DATA_VENCIMENTO
        defaultContaPagamentoShouldNotBeFound("dataVencimento.lessThanOrEqual=" + SMALLER_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataVencimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataVencimento is less than DEFAULT_DATA_VENCIMENTO
        defaultContaPagamentoShouldNotBeFound("dataVencimento.lessThan=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaPagamentoList where dataVencimento is less than UPDATED_DATA_VENCIMENTO
        defaultContaPagamentoShouldBeFound("dataVencimento.lessThan=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataVencimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataVencimento is greater than DEFAULT_DATA_VENCIMENTO
        defaultContaPagamentoShouldNotBeFound("dataVencimento.greaterThan=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaPagamentoList where dataVencimento is greater than SMALLER_DATA_VENCIMENTO
        defaultContaPagamentoShouldBeFound("dataVencimento.greaterThan=" + SMALLER_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where descricao equals to DEFAULT_DESCRICAO
        defaultContaPagamentoShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the contaPagamentoList where descricao equals to UPDATED_DESCRICAO
        defaultContaPagamentoShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultContaPagamentoShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the contaPagamentoList where descricao equals to UPDATED_DESCRICAO
        defaultContaPagamentoShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where descricao is not null
        defaultContaPagamentoShouldBeFound("descricao.specified=true");

        // Get all the contaPagamentoList where descricao is null
        defaultContaPagamentoShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where descricao contains DEFAULT_DESCRICAO
        defaultContaPagamentoShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the contaPagamentoList where descricao contains UPDATED_DESCRICAO
        defaultContaPagamentoShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where descricao does not contain DEFAULT_DESCRICAO
        defaultContaPagamentoShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the contaPagamentoList where descricao does not contain UPDATED_DESCRICAO
        defaultContaPagamentoShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valor equals to DEFAULT_VALOR
        defaultContaPagamentoShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the contaPagamentoList where valor equals to UPDATED_VALOR
        defaultContaPagamentoShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultContaPagamentoShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the contaPagamentoList where valor equals to UPDATED_VALOR
        defaultContaPagamentoShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valor is not null
        defaultContaPagamentoShouldBeFound("valor.specified=true");

        // Get all the contaPagamentoList where valor is null
        defaultContaPagamentoShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valor is greater than or equal to DEFAULT_VALOR
        defaultContaPagamentoShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the contaPagamentoList where valor is greater than or equal to UPDATED_VALOR
        defaultContaPagamentoShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valor is less than or equal to DEFAULT_VALOR
        defaultContaPagamentoShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the contaPagamentoList where valor is less than or equal to SMALLER_VALOR
        defaultContaPagamentoShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valor is less than DEFAULT_VALOR
        defaultContaPagamentoShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the contaPagamentoList where valor is less than UPDATED_VALOR
        defaultContaPagamentoShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valor is greater than DEFAULT_VALOR
        defaultContaPagamentoShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the contaPagamentoList where valor is greater than SMALLER_VALOR
        defaultContaPagamentoShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataPagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataPagamento equals to DEFAULT_DATA_PAGAMENTO
        defaultContaPagamentoShouldBeFound("dataPagamento.equals=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the contaPagamentoList where dataPagamento equals to UPDATED_DATA_PAGAMENTO
        defaultContaPagamentoShouldNotBeFound("dataPagamento.equals=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataPagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataPagamento in DEFAULT_DATA_PAGAMENTO or UPDATED_DATA_PAGAMENTO
        defaultContaPagamentoShouldBeFound("dataPagamento.in=" + DEFAULT_DATA_PAGAMENTO + "," + UPDATED_DATA_PAGAMENTO);

        // Get all the contaPagamentoList where dataPagamento equals to UPDATED_DATA_PAGAMENTO
        defaultContaPagamentoShouldNotBeFound("dataPagamento.in=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataPagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataPagamento is not null
        defaultContaPagamentoShouldBeFound("dataPagamento.specified=true");

        // Get all the contaPagamentoList where dataPagamento is null
        defaultContaPagamentoShouldNotBeFound("dataPagamento.specified=false");
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataPagamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataPagamento is greater than or equal to DEFAULT_DATA_PAGAMENTO
        defaultContaPagamentoShouldBeFound("dataPagamento.greaterThanOrEqual=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the contaPagamentoList where dataPagamento is greater than or equal to UPDATED_DATA_PAGAMENTO
        defaultContaPagamentoShouldNotBeFound("dataPagamento.greaterThanOrEqual=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataPagamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataPagamento is less than or equal to DEFAULT_DATA_PAGAMENTO
        defaultContaPagamentoShouldBeFound("dataPagamento.lessThanOrEqual=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the contaPagamentoList where dataPagamento is less than or equal to SMALLER_DATA_PAGAMENTO
        defaultContaPagamentoShouldNotBeFound("dataPagamento.lessThanOrEqual=" + SMALLER_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataPagamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataPagamento is less than DEFAULT_DATA_PAGAMENTO
        defaultContaPagamentoShouldNotBeFound("dataPagamento.lessThan=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the contaPagamentoList where dataPagamento is less than UPDATED_DATA_PAGAMENTO
        defaultContaPagamentoShouldBeFound("dataPagamento.lessThan=" + UPDATED_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByDataPagamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where dataPagamento is greater than DEFAULT_DATA_PAGAMENTO
        defaultContaPagamentoShouldNotBeFound("dataPagamento.greaterThan=" + DEFAULT_DATA_PAGAMENTO);

        // Get all the contaPagamentoList where dataPagamento is greater than SMALLER_DATA_PAGAMENTO
        defaultContaPagamentoShouldBeFound("dataPagamento.greaterThan=" + SMALLER_DATA_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valorPago equals to DEFAULT_VALOR_PAGO
        defaultContaPagamentoShouldBeFound("valorPago.equals=" + DEFAULT_VALOR_PAGO);

        // Get all the contaPagamentoList where valorPago equals to UPDATED_VALOR_PAGO
        defaultContaPagamentoShouldNotBeFound("valorPago.equals=" + UPDATED_VALOR_PAGO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorPagoIsInShouldWork() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valorPago in DEFAULT_VALOR_PAGO or UPDATED_VALOR_PAGO
        defaultContaPagamentoShouldBeFound("valorPago.in=" + DEFAULT_VALOR_PAGO + "," + UPDATED_VALOR_PAGO);

        // Get all the contaPagamentoList where valorPago equals to UPDATED_VALOR_PAGO
        defaultContaPagamentoShouldNotBeFound("valorPago.in=" + UPDATED_VALOR_PAGO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valorPago is not null
        defaultContaPagamentoShouldBeFound("valorPago.specified=true");

        // Get all the contaPagamentoList where valorPago is null
        defaultContaPagamentoShouldNotBeFound("valorPago.specified=false");
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorPagoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valorPago is greater than or equal to DEFAULT_VALOR_PAGO
        defaultContaPagamentoShouldBeFound("valorPago.greaterThanOrEqual=" + DEFAULT_VALOR_PAGO);

        // Get all the contaPagamentoList where valorPago is greater than or equal to UPDATED_VALOR_PAGO
        defaultContaPagamentoShouldNotBeFound("valorPago.greaterThanOrEqual=" + UPDATED_VALOR_PAGO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorPagoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valorPago is less than or equal to DEFAULT_VALOR_PAGO
        defaultContaPagamentoShouldBeFound("valorPago.lessThanOrEqual=" + DEFAULT_VALOR_PAGO);

        // Get all the contaPagamentoList where valorPago is less than or equal to SMALLER_VALOR_PAGO
        defaultContaPagamentoShouldNotBeFound("valorPago.lessThanOrEqual=" + SMALLER_VALOR_PAGO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorPagoIsLessThanSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valorPago is less than DEFAULT_VALOR_PAGO
        defaultContaPagamentoShouldNotBeFound("valorPago.lessThan=" + DEFAULT_VALOR_PAGO);

        // Get all the contaPagamentoList where valorPago is less than UPDATED_VALOR_PAGO
        defaultContaPagamentoShouldBeFound("valorPago.lessThan=" + UPDATED_VALOR_PAGO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByValorPagoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where valorPago is greater than DEFAULT_VALOR_PAGO
        defaultContaPagamentoShouldNotBeFound("valorPago.greaterThan=" + DEFAULT_VALOR_PAGO);

        // Get all the contaPagamentoList where valorPago is greater than SMALLER_VALOR_PAGO
        defaultContaPagamentoShouldBeFound("valorPago.greaterThan=" + SMALLER_VALOR_PAGO);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByObservacoesIsEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where observacoes equals to DEFAULT_OBSERVACOES
        defaultContaPagamentoShouldBeFound("observacoes.equals=" + DEFAULT_OBSERVACOES);

        // Get all the contaPagamentoList where observacoes equals to UPDATED_OBSERVACOES
        defaultContaPagamentoShouldNotBeFound("observacoes.equals=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByObservacoesIsInShouldWork() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where observacoes in DEFAULT_OBSERVACOES or UPDATED_OBSERVACOES
        defaultContaPagamentoShouldBeFound("observacoes.in=" + DEFAULT_OBSERVACOES + "," + UPDATED_OBSERVACOES);

        // Get all the contaPagamentoList where observacoes equals to UPDATED_OBSERVACOES
        defaultContaPagamentoShouldNotBeFound("observacoes.in=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByObservacoesIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where observacoes is not null
        defaultContaPagamentoShouldBeFound("observacoes.specified=true");

        // Get all the contaPagamentoList where observacoes is null
        defaultContaPagamentoShouldNotBeFound("observacoes.specified=false");
    }

    @Test
    @Transactional
    void getAllContaPagamentosByObservacoesContainsSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where observacoes contains DEFAULT_OBSERVACOES
        defaultContaPagamentoShouldBeFound("observacoes.contains=" + DEFAULT_OBSERVACOES);

        // Get all the contaPagamentoList where observacoes contains UPDATED_OBSERVACOES
        defaultContaPagamentoShouldNotBeFound("observacoes.contains=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByObservacoesNotContainsSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where observacoes does not contain DEFAULT_OBSERVACOES
        defaultContaPagamentoShouldNotBeFound("observacoes.doesNotContain=" + DEFAULT_OBSERVACOES);

        // Get all the contaPagamentoList where observacoes does not contain UPDATED_OBSERVACOES
        defaultContaPagamentoShouldBeFound("observacoes.doesNotContain=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByPeriodicidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where periodicidade equals to DEFAULT_PERIODICIDADE
        defaultContaPagamentoShouldBeFound("periodicidade.equals=" + DEFAULT_PERIODICIDADE);

        // Get all the contaPagamentoList where periodicidade equals to UPDATED_PERIODICIDADE
        defaultContaPagamentoShouldNotBeFound("periodicidade.equals=" + UPDATED_PERIODICIDADE);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByPeriodicidadeIsInShouldWork() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where periodicidade in DEFAULT_PERIODICIDADE or UPDATED_PERIODICIDADE
        defaultContaPagamentoShouldBeFound("periodicidade.in=" + DEFAULT_PERIODICIDADE + "," + UPDATED_PERIODICIDADE);

        // Get all the contaPagamentoList where periodicidade equals to UPDATED_PERIODICIDADE
        defaultContaPagamentoShouldNotBeFound("periodicidade.in=" + UPDATED_PERIODICIDADE);
    }

    @Test
    @Transactional
    void getAllContaPagamentosByPeriodicidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        // Get all the contaPagamentoList where periodicidade is not null
        defaultContaPagamentoShouldBeFound("periodicidade.specified=true");

        // Get all the contaPagamentoList where periodicidade is null
        defaultContaPagamentoShouldNotBeFound("periodicidade.specified=false");
    }

    @Test
    @Transactional
    void getAllContaPagamentosByBeneficiarioIsEqualToSomething() throws Exception {
        Beneficiario beneficiario;
        if (TestUtil.findAll(em, Beneficiario.class).isEmpty()) {
            contaPagamentoRepository.saveAndFlush(contaPagamento);
            beneficiario = BeneficiarioResourceIT.createEntity(em);
        } else {
            beneficiario = TestUtil.findAll(em, Beneficiario.class).get(0);
        }
        em.persist(beneficiario);
        em.flush();
        contaPagamento.setBeneficiario(beneficiario);
        contaPagamentoRepository.saveAndFlush(contaPagamento);
        Long beneficiarioId = beneficiario.getId();

        // Get all the contaPagamentoList where beneficiario equals to beneficiarioId
        defaultContaPagamentoShouldBeFound("beneficiarioId.equals=" + beneficiarioId);

        // Get all the contaPagamentoList where beneficiario equals to (beneficiarioId + 1)
        defaultContaPagamentoShouldNotBeFound("beneficiarioId.equals=" + (beneficiarioId + 1));
    }

    @Test
    @Transactional
    void getAllContaPagamentosByCategoriaPagamentoIsEqualToSomething() throws Exception {
        CategoriaPagamento categoriaPagamento;
        if (TestUtil.findAll(em, CategoriaPagamento.class).isEmpty()) {
            contaPagamentoRepository.saveAndFlush(contaPagamento);
            categoriaPagamento = CategoriaPagamentoResourceIT.createEntity(em);
        } else {
            categoriaPagamento = TestUtil.findAll(em, CategoriaPagamento.class).get(0);
        }
        em.persist(categoriaPagamento);
        em.flush();
        contaPagamento.setCategoriaPagamento(categoriaPagamento);
        contaPagamentoRepository.saveAndFlush(contaPagamento);
        Long categoriaPagamentoId = categoriaPagamento.getId();

        // Get all the contaPagamentoList where categoriaPagamento equals to categoriaPagamentoId
        defaultContaPagamentoShouldBeFound("categoriaPagamentoId.equals=" + categoriaPagamentoId);

        // Get all the contaPagamentoList where categoriaPagamento equals to (categoriaPagamentoId + 1)
        defaultContaPagamentoShouldNotBeFound("categoriaPagamentoId.equals=" + (categoriaPagamentoId + 1));
    }

    @Test
    @Transactional
    void getAllContaPagamentosByCaixaIsEqualToSomething() throws Exception {
        Caixa caixa;
        if (TestUtil.findAll(em, Caixa.class).isEmpty()) {
            contaPagamentoRepository.saveAndFlush(contaPagamento);
            caixa = CaixaResourceIT.createEntity(em);
        } else {
            caixa = TestUtil.findAll(em, Caixa.class).get(0);
        }
        em.persist(caixa);
        em.flush();
        contaPagamento.setCaixa(caixa);
        contaPagamentoRepository.saveAndFlush(contaPagamento);
        Long caixaId = caixa.getId();

        // Get all the contaPagamentoList where caixa equals to caixaId
        defaultContaPagamentoShouldBeFound("caixaId.equals=" + caixaId);

        // Get all the contaPagamentoList where caixa equals to (caixaId + 1)
        defaultContaPagamentoShouldNotBeFound("caixaId.equals=" + (caixaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContaPagamentoShouldBeFound(String filter) throws Exception {
        restContaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataVencimento").value(hasItem(DEFAULT_DATA_VENCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].dataPagamento").value(hasItem(DEFAULT_DATA_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].valorPago").value(hasItem(sameNumber(DEFAULT_VALOR_PAGO))))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)))
            .andExpect(jsonPath("$.[*].periodicidade").value(hasItem(DEFAULT_PERIODICIDADE.toString())));

        // Check, that the count call also returns 1
        restContaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContaPagamentoShouldNotBeFound(String filter) throws Exception {
        restContaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContaPagamento() throws Exception {
        // Get the contaPagamento
        restContaPagamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContaPagamento() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        int databaseSizeBeforeUpdate = contaPagamentoRepository.findAll().size();

        // Update the contaPagamento
        ContaPagamento updatedContaPagamento = contaPagamentoRepository.findById(contaPagamento.getId()).get();
        // Disconnect from session so that the updates on updatedContaPagamento are not directly saved in db
        em.detach(updatedContaPagamento);
        updatedContaPagamento
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dataPagamento(UPDATED_DATA_PAGAMENTO)
            .valorPago(UPDATED_VALOR_PAGO)
            .observacoes(UPDATED_OBSERVACOES)
            .periodicidade(UPDATED_PERIODICIDADE);
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(updatedContaPagamento);

        restContaPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaPagamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeUpdate);
        ContaPagamento testContaPagamento = contaPagamentoList.get(contaPagamentoList.size() - 1);
        assertThat(testContaPagamento.getDataVencimento()).isEqualTo(UPDATED_DATA_VENCIMENTO);
        assertThat(testContaPagamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testContaPagamento.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testContaPagamento.getDataPagamento()).isEqualTo(UPDATED_DATA_PAGAMENTO);
        assertThat(testContaPagamento.getValorPago()).isEqualByComparingTo(UPDATED_VALOR_PAGO);
        assertThat(testContaPagamento.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testContaPagamento.getPeriodicidade()).isEqualTo(UPDATED_PERIODICIDADE);
    }

    @Test
    @Transactional
    void putNonExistingContaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = contaPagamentoRepository.findAll().size();
        contaPagamento.setId(count.incrementAndGet());

        // Create the ContaPagamento
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(contaPagamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaPagamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = contaPagamentoRepository.findAll().size();
        contaPagamento.setId(count.incrementAndGet());

        // Create the ContaPagamento
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(contaPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = contaPagamentoRepository.findAll().size();
        contaPagamento.setId(count.incrementAndGet());

        // Create the ContaPagamento
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(contaPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContaPagamentoWithPatch() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        int databaseSizeBeforeUpdate = contaPagamentoRepository.findAll().size();

        // Update the contaPagamento using partial update
        ContaPagamento partialUpdatedContaPagamento = new ContaPagamento();
        partialUpdatedContaPagamento.setId(contaPagamento.getId());

        partialUpdatedContaPagamento
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dataPagamento(UPDATED_DATA_PAGAMENTO);

        restContaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaPagamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaPagamento))
            )
            .andExpect(status().isOk());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeUpdate);
        ContaPagamento testContaPagamento = contaPagamentoList.get(contaPagamentoList.size() - 1);
        assertThat(testContaPagamento.getDataVencimento()).isEqualTo(UPDATED_DATA_VENCIMENTO);
        assertThat(testContaPagamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testContaPagamento.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testContaPagamento.getDataPagamento()).isEqualTo(UPDATED_DATA_PAGAMENTO);
        assertThat(testContaPagamento.getValorPago()).isEqualByComparingTo(DEFAULT_VALOR_PAGO);
        assertThat(testContaPagamento.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);
        assertThat(testContaPagamento.getPeriodicidade()).isEqualTo(DEFAULT_PERIODICIDADE);
    }

    @Test
    @Transactional
    void fullUpdateContaPagamentoWithPatch() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        int databaseSizeBeforeUpdate = contaPagamentoRepository.findAll().size();

        // Update the contaPagamento using partial update
        ContaPagamento partialUpdatedContaPagamento = new ContaPagamento();
        partialUpdatedContaPagamento.setId(contaPagamento.getId());

        partialUpdatedContaPagamento
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dataPagamento(UPDATED_DATA_PAGAMENTO)
            .valorPago(UPDATED_VALOR_PAGO)
            .observacoes(UPDATED_OBSERVACOES)
            .periodicidade(UPDATED_PERIODICIDADE);

        restContaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaPagamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaPagamento))
            )
            .andExpect(status().isOk());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeUpdate);
        ContaPagamento testContaPagamento = contaPagamentoList.get(contaPagamentoList.size() - 1);
        assertThat(testContaPagamento.getDataVencimento()).isEqualTo(UPDATED_DATA_VENCIMENTO);
        assertThat(testContaPagamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testContaPagamento.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testContaPagamento.getDataPagamento()).isEqualTo(UPDATED_DATA_PAGAMENTO);
        assertThat(testContaPagamento.getValorPago()).isEqualByComparingTo(UPDATED_VALOR_PAGO);
        assertThat(testContaPagamento.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testContaPagamento.getPeriodicidade()).isEqualTo(UPDATED_PERIODICIDADE);
    }

    @Test
    @Transactional
    void patchNonExistingContaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = contaPagamentoRepository.findAll().size();
        contaPagamento.setId(count.incrementAndGet());

        // Create the ContaPagamento
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(contaPagamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contaPagamentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = contaPagamentoRepository.findAll().size();
        contaPagamento.setId(count.incrementAndGet());

        // Create the ContaPagamento
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(contaPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = contaPagamentoRepository.findAll().size();
        contaPagamento.setId(count.incrementAndGet());

        // Create the ContaPagamento
        ContaPagamentoDTO contaPagamentoDTO = contaPagamentoMapper.toDto(contaPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaPagamentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaPagamento in the database
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContaPagamento() throws Exception {
        // Initialize the database
        contaPagamentoRepository.saveAndFlush(contaPagamento);

        int databaseSizeBeforeDelete = contaPagamentoRepository.findAll().size();

        // Delete the contaPagamento
        restContaPagamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, contaPagamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContaPagamento> contaPagamentoList = contaPagamentoRepository.findAll();
        assertThat(contaPagamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
