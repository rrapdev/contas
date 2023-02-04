package br.com.contas.web.rest;

import static br.com.contas.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.contas.IntegrationTest;
import br.com.contas.domain.Caixa;
import br.com.contas.domain.CategoriaRecebimento;
import br.com.contas.domain.ContaRecebimento;
import br.com.contas.domain.Pagador;
import br.com.contas.domain.enumeration.Periodicidade;
import br.com.contas.repository.ContaRecebimentoRepository;
import br.com.contas.service.ContaRecebimentoService;
import br.com.contas.service.criteria.ContaRecebimentoCriteria;
import br.com.contas.service.dto.ContaRecebimentoDTO;
import br.com.contas.service.mapper.ContaRecebimentoMapper;
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
 * Integration tests for the {@link ContaRecebimentoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContaRecebimentoResourceIT {

    private static final LocalDate DEFAULT_DATA_VENCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_VENCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_VENCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_DATA_RECEBIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_RECEBIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_RECEBIMENTO = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_VALOR_RECEBIDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_RECEBIDO = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_RECEBIDO = new BigDecimal(1 - 1);

    private static final String DEFAULT_OBSERVACOES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACOES = "BBBBBBBBBB";

    private static final Periodicidade DEFAULT_PERIODICIDADE = Periodicidade.ESPORADICA;
    private static final Periodicidade UPDATED_PERIODICIDADE = Periodicidade.MENSAL;

    private static final String ENTITY_API_URL = "/api/conta-recebimentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContaRecebimentoRepository contaRecebimentoRepository;

    @Mock
    private ContaRecebimentoRepository contaRecebimentoRepositoryMock;

    @Autowired
    private ContaRecebimentoMapper contaRecebimentoMapper;

    @Mock
    private ContaRecebimentoService contaRecebimentoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContaRecebimentoMockMvc;

    private ContaRecebimento contaRecebimento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaRecebimento createEntity(EntityManager em) {
        ContaRecebimento contaRecebimento = new ContaRecebimento()
            .dataVencimento(DEFAULT_DATA_VENCIMENTO)
            .descricao(DEFAULT_DESCRICAO)
            .valor(DEFAULT_VALOR)
            .dataRecebimento(DEFAULT_DATA_RECEBIMENTO)
            .valorRecebido(DEFAULT_VALOR_RECEBIDO)
            .observacoes(DEFAULT_OBSERVACOES)
            .periodicidade(DEFAULT_PERIODICIDADE);
        return contaRecebimento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaRecebimento createUpdatedEntity(EntityManager em) {
        ContaRecebimento contaRecebimento = new ContaRecebimento()
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .observacoes(UPDATED_OBSERVACOES)
            .periodicidade(UPDATED_PERIODICIDADE);
        return contaRecebimento;
    }

    @BeforeEach
    public void initTest() {
        contaRecebimento = createEntity(em);
    }

    @Test
    @Transactional
    void createContaRecebimento() throws Exception {
        int databaseSizeBeforeCreate = contaRecebimentoRepository.findAll().size();
        // Create the ContaRecebimento
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(contaRecebimento);
        restContaRecebimentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeCreate + 1);
        ContaRecebimento testContaRecebimento = contaRecebimentoList.get(contaRecebimentoList.size() - 1);
        assertThat(testContaRecebimento.getDataVencimento()).isEqualTo(DEFAULT_DATA_VENCIMENTO);
        assertThat(testContaRecebimento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testContaRecebimento.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
        assertThat(testContaRecebimento.getDataRecebimento()).isEqualTo(DEFAULT_DATA_RECEBIMENTO);
        assertThat(testContaRecebimento.getValorRecebido()).isEqualByComparingTo(DEFAULT_VALOR_RECEBIDO);
        assertThat(testContaRecebimento.getObservacoes()).isEqualTo(DEFAULT_OBSERVACOES);
        assertThat(testContaRecebimento.getPeriodicidade()).isEqualTo(DEFAULT_PERIODICIDADE);
    }

    @Test
    @Transactional
    void createContaRecebimentoWithExistingId() throws Exception {
        // Create the ContaRecebimento with an existing ID
        contaRecebimento.setId(1L);
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(contaRecebimento);

        int databaseSizeBeforeCreate = contaRecebimentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaRecebimentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataVencimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaRecebimentoRepository.findAll().size();
        // set the field null
        contaRecebimento.setDataVencimento(null);

        // Create the ContaRecebimento, which fails.
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(contaRecebimento);

        restContaRecebimentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContaRecebimentos() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList
        restContaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaRecebimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataVencimento").value(hasItem(DEFAULT_DATA_VENCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].dataRecebimento").value(hasItem(DEFAULT_DATA_RECEBIMENTO.toString())))
            .andExpect(jsonPath("$.[*].valorRecebido").value(hasItem(sameNumber(DEFAULT_VALOR_RECEBIDO))))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)))
            .andExpect(jsonPath("$.[*].periodicidade").value(hasItem(DEFAULT_PERIODICIDADE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContaRecebimentosWithEagerRelationshipsIsEnabled() throws Exception {
        when(contaRecebimentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContaRecebimentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contaRecebimentoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContaRecebimentosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contaRecebimentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContaRecebimentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contaRecebimentoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContaRecebimento() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get the contaRecebimento
        restContaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL_ID, contaRecebimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contaRecebimento.getId().intValue()))
            .andExpect(jsonPath("$.dataVencimento").value(DEFAULT_DATA_VENCIMENTO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.dataRecebimento").value(DEFAULT_DATA_RECEBIMENTO.toString()))
            .andExpect(jsonPath("$.valorRecebido").value(sameNumber(DEFAULT_VALOR_RECEBIDO)))
            .andExpect(jsonPath("$.observacoes").value(DEFAULT_OBSERVACOES))
            .andExpect(jsonPath("$.periodicidade").value(DEFAULT_PERIODICIDADE.toString()));
    }

    @Test
    @Transactional
    void getContaRecebimentosByIdFiltering() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        Long id = contaRecebimento.getId();

        defaultContaRecebimentoShouldBeFound("id.equals=" + id);
        defaultContaRecebimentoShouldNotBeFound("id.notEquals=" + id);

        defaultContaRecebimentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContaRecebimentoShouldNotBeFound("id.greaterThan=" + id);

        defaultContaRecebimentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContaRecebimentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataVencimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataVencimento equals to DEFAULT_DATA_VENCIMENTO
        defaultContaRecebimentoShouldBeFound("dataVencimento.equals=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaRecebimentoList where dataVencimento equals to UPDATED_DATA_VENCIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataVencimento.equals=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataVencimentoIsInShouldWork() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataVencimento in DEFAULT_DATA_VENCIMENTO or UPDATED_DATA_VENCIMENTO
        defaultContaRecebimentoShouldBeFound("dataVencimento.in=" + DEFAULT_DATA_VENCIMENTO + "," + UPDATED_DATA_VENCIMENTO);

        // Get all the contaRecebimentoList where dataVencimento equals to UPDATED_DATA_VENCIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataVencimento.in=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataVencimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataVencimento is not null
        defaultContaRecebimentoShouldBeFound("dataVencimento.specified=true");

        // Get all the contaRecebimentoList where dataVencimento is null
        defaultContaRecebimentoShouldNotBeFound("dataVencimento.specified=false");
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataVencimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataVencimento is greater than or equal to DEFAULT_DATA_VENCIMENTO
        defaultContaRecebimentoShouldBeFound("dataVencimento.greaterThanOrEqual=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaRecebimentoList where dataVencimento is greater than or equal to UPDATED_DATA_VENCIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataVencimento.greaterThanOrEqual=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataVencimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataVencimento is less than or equal to DEFAULT_DATA_VENCIMENTO
        defaultContaRecebimentoShouldBeFound("dataVencimento.lessThanOrEqual=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaRecebimentoList where dataVencimento is less than or equal to SMALLER_DATA_VENCIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataVencimento.lessThanOrEqual=" + SMALLER_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataVencimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataVencimento is less than DEFAULT_DATA_VENCIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataVencimento.lessThan=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaRecebimentoList where dataVencimento is less than UPDATED_DATA_VENCIMENTO
        defaultContaRecebimentoShouldBeFound("dataVencimento.lessThan=" + UPDATED_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataVencimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataVencimento is greater than DEFAULT_DATA_VENCIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataVencimento.greaterThan=" + DEFAULT_DATA_VENCIMENTO);

        // Get all the contaRecebimentoList where dataVencimento is greater than SMALLER_DATA_VENCIMENTO
        defaultContaRecebimentoShouldBeFound("dataVencimento.greaterThan=" + SMALLER_DATA_VENCIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where descricao equals to DEFAULT_DESCRICAO
        defaultContaRecebimentoShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the contaRecebimentoList where descricao equals to UPDATED_DESCRICAO
        defaultContaRecebimentoShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultContaRecebimentoShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the contaRecebimentoList where descricao equals to UPDATED_DESCRICAO
        defaultContaRecebimentoShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where descricao is not null
        defaultContaRecebimentoShouldBeFound("descricao.specified=true");

        // Get all the contaRecebimentoList where descricao is null
        defaultContaRecebimentoShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where descricao contains DEFAULT_DESCRICAO
        defaultContaRecebimentoShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the contaRecebimentoList where descricao contains UPDATED_DESCRICAO
        defaultContaRecebimentoShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where descricao does not contain DEFAULT_DESCRICAO
        defaultContaRecebimentoShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the contaRecebimentoList where descricao does not contain UPDATED_DESCRICAO
        defaultContaRecebimentoShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valor equals to DEFAULT_VALOR
        defaultContaRecebimentoShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the contaRecebimentoList where valor equals to UPDATED_VALOR
        defaultContaRecebimentoShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultContaRecebimentoShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the contaRecebimentoList where valor equals to UPDATED_VALOR
        defaultContaRecebimentoShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valor is not null
        defaultContaRecebimentoShouldBeFound("valor.specified=true");

        // Get all the contaRecebimentoList where valor is null
        defaultContaRecebimentoShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valor is greater than or equal to DEFAULT_VALOR
        defaultContaRecebimentoShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the contaRecebimentoList where valor is greater than or equal to UPDATED_VALOR
        defaultContaRecebimentoShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valor is less than or equal to DEFAULT_VALOR
        defaultContaRecebimentoShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the contaRecebimentoList where valor is less than or equal to SMALLER_VALOR
        defaultContaRecebimentoShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valor is less than DEFAULT_VALOR
        defaultContaRecebimentoShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the contaRecebimentoList where valor is less than UPDATED_VALOR
        defaultContaRecebimentoShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valor is greater than DEFAULT_VALOR
        defaultContaRecebimentoShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the contaRecebimentoList where valor is greater than SMALLER_VALOR
        defaultContaRecebimentoShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataRecebimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataRecebimento equals to DEFAULT_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldBeFound("dataRecebimento.equals=" + DEFAULT_DATA_RECEBIMENTO);

        // Get all the contaRecebimentoList where dataRecebimento equals to UPDATED_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataRecebimento.equals=" + UPDATED_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataRecebimentoIsInShouldWork() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataRecebimento in DEFAULT_DATA_RECEBIMENTO or UPDATED_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldBeFound("dataRecebimento.in=" + DEFAULT_DATA_RECEBIMENTO + "," + UPDATED_DATA_RECEBIMENTO);

        // Get all the contaRecebimentoList where dataRecebimento equals to UPDATED_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataRecebimento.in=" + UPDATED_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataRecebimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataRecebimento is not null
        defaultContaRecebimentoShouldBeFound("dataRecebimento.specified=true");

        // Get all the contaRecebimentoList where dataRecebimento is null
        defaultContaRecebimentoShouldNotBeFound("dataRecebimento.specified=false");
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataRecebimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataRecebimento is greater than or equal to DEFAULT_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldBeFound("dataRecebimento.greaterThanOrEqual=" + DEFAULT_DATA_RECEBIMENTO);

        // Get all the contaRecebimentoList where dataRecebimento is greater than or equal to UPDATED_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataRecebimento.greaterThanOrEqual=" + UPDATED_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataRecebimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataRecebimento is less than or equal to DEFAULT_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldBeFound("dataRecebimento.lessThanOrEqual=" + DEFAULT_DATA_RECEBIMENTO);

        // Get all the contaRecebimentoList where dataRecebimento is less than or equal to SMALLER_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataRecebimento.lessThanOrEqual=" + SMALLER_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataRecebimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataRecebimento is less than DEFAULT_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataRecebimento.lessThan=" + DEFAULT_DATA_RECEBIMENTO);

        // Get all the contaRecebimentoList where dataRecebimento is less than UPDATED_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldBeFound("dataRecebimento.lessThan=" + UPDATED_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByDataRecebimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where dataRecebimento is greater than DEFAULT_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldNotBeFound("dataRecebimento.greaterThan=" + DEFAULT_DATA_RECEBIMENTO);

        // Get all the contaRecebimentoList where dataRecebimento is greater than SMALLER_DATA_RECEBIMENTO
        defaultContaRecebimentoShouldBeFound("dataRecebimento.greaterThan=" + SMALLER_DATA_RECEBIMENTO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorRecebidoIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valorRecebido equals to DEFAULT_VALOR_RECEBIDO
        defaultContaRecebimentoShouldBeFound("valorRecebido.equals=" + DEFAULT_VALOR_RECEBIDO);

        // Get all the contaRecebimentoList where valorRecebido equals to UPDATED_VALOR_RECEBIDO
        defaultContaRecebimentoShouldNotBeFound("valorRecebido.equals=" + UPDATED_VALOR_RECEBIDO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorRecebidoIsInShouldWork() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valorRecebido in DEFAULT_VALOR_RECEBIDO or UPDATED_VALOR_RECEBIDO
        defaultContaRecebimentoShouldBeFound("valorRecebido.in=" + DEFAULT_VALOR_RECEBIDO + "," + UPDATED_VALOR_RECEBIDO);

        // Get all the contaRecebimentoList where valorRecebido equals to UPDATED_VALOR_RECEBIDO
        defaultContaRecebimentoShouldNotBeFound("valorRecebido.in=" + UPDATED_VALOR_RECEBIDO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorRecebidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valorRecebido is not null
        defaultContaRecebimentoShouldBeFound("valorRecebido.specified=true");

        // Get all the contaRecebimentoList where valorRecebido is null
        defaultContaRecebimentoShouldNotBeFound("valorRecebido.specified=false");
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorRecebidoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valorRecebido is greater than or equal to DEFAULT_VALOR_RECEBIDO
        defaultContaRecebimentoShouldBeFound("valorRecebido.greaterThanOrEqual=" + DEFAULT_VALOR_RECEBIDO);

        // Get all the contaRecebimentoList where valorRecebido is greater than or equal to UPDATED_VALOR_RECEBIDO
        defaultContaRecebimentoShouldNotBeFound("valorRecebido.greaterThanOrEqual=" + UPDATED_VALOR_RECEBIDO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorRecebidoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valorRecebido is less than or equal to DEFAULT_VALOR_RECEBIDO
        defaultContaRecebimentoShouldBeFound("valorRecebido.lessThanOrEqual=" + DEFAULT_VALOR_RECEBIDO);

        // Get all the contaRecebimentoList where valorRecebido is less than or equal to SMALLER_VALOR_RECEBIDO
        defaultContaRecebimentoShouldNotBeFound("valorRecebido.lessThanOrEqual=" + SMALLER_VALOR_RECEBIDO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorRecebidoIsLessThanSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valorRecebido is less than DEFAULT_VALOR_RECEBIDO
        defaultContaRecebimentoShouldNotBeFound("valorRecebido.lessThan=" + DEFAULT_VALOR_RECEBIDO);

        // Get all the contaRecebimentoList where valorRecebido is less than UPDATED_VALOR_RECEBIDO
        defaultContaRecebimentoShouldBeFound("valorRecebido.lessThan=" + UPDATED_VALOR_RECEBIDO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByValorRecebidoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where valorRecebido is greater than DEFAULT_VALOR_RECEBIDO
        defaultContaRecebimentoShouldNotBeFound("valorRecebido.greaterThan=" + DEFAULT_VALOR_RECEBIDO);

        // Get all the contaRecebimentoList where valorRecebido is greater than SMALLER_VALOR_RECEBIDO
        defaultContaRecebimentoShouldBeFound("valorRecebido.greaterThan=" + SMALLER_VALOR_RECEBIDO);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByObservacoesIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where observacoes equals to DEFAULT_OBSERVACOES
        defaultContaRecebimentoShouldBeFound("observacoes.equals=" + DEFAULT_OBSERVACOES);

        // Get all the contaRecebimentoList where observacoes equals to UPDATED_OBSERVACOES
        defaultContaRecebimentoShouldNotBeFound("observacoes.equals=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByObservacoesIsInShouldWork() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where observacoes in DEFAULT_OBSERVACOES or UPDATED_OBSERVACOES
        defaultContaRecebimentoShouldBeFound("observacoes.in=" + DEFAULT_OBSERVACOES + "," + UPDATED_OBSERVACOES);

        // Get all the contaRecebimentoList where observacoes equals to UPDATED_OBSERVACOES
        defaultContaRecebimentoShouldNotBeFound("observacoes.in=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByObservacoesIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where observacoes is not null
        defaultContaRecebimentoShouldBeFound("observacoes.specified=true");

        // Get all the contaRecebimentoList where observacoes is null
        defaultContaRecebimentoShouldNotBeFound("observacoes.specified=false");
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByObservacoesContainsSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where observacoes contains DEFAULT_OBSERVACOES
        defaultContaRecebimentoShouldBeFound("observacoes.contains=" + DEFAULT_OBSERVACOES);

        // Get all the contaRecebimentoList where observacoes contains UPDATED_OBSERVACOES
        defaultContaRecebimentoShouldNotBeFound("observacoes.contains=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByObservacoesNotContainsSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where observacoes does not contain DEFAULT_OBSERVACOES
        defaultContaRecebimentoShouldNotBeFound("observacoes.doesNotContain=" + DEFAULT_OBSERVACOES);

        // Get all the contaRecebimentoList where observacoes does not contain UPDATED_OBSERVACOES
        defaultContaRecebimentoShouldBeFound("observacoes.doesNotContain=" + UPDATED_OBSERVACOES);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByPeriodicidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where periodicidade equals to DEFAULT_PERIODICIDADE
        defaultContaRecebimentoShouldBeFound("periodicidade.equals=" + DEFAULT_PERIODICIDADE);

        // Get all the contaRecebimentoList where periodicidade equals to UPDATED_PERIODICIDADE
        defaultContaRecebimentoShouldNotBeFound("periodicidade.equals=" + UPDATED_PERIODICIDADE);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByPeriodicidadeIsInShouldWork() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where periodicidade in DEFAULT_PERIODICIDADE or UPDATED_PERIODICIDADE
        defaultContaRecebimentoShouldBeFound("periodicidade.in=" + DEFAULT_PERIODICIDADE + "," + UPDATED_PERIODICIDADE);

        // Get all the contaRecebimentoList where periodicidade equals to UPDATED_PERIODICIDADE
        defaultContaRecebimentoShouldNotBeFound("periodicidade.in=" + UPDATED_PERIODICIDADE);
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByPeriodicidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        // Get all the contaRecebimentoList where periodicidade is not null
        defaultContaRecebimentoShouldBeFound("periodicidade.specified=true");

        // Get all the contaRecebimentoList where periodicidade is null
        defaultContaRecebimentoShouldNotBeFound("periodicidade.specified=false");
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByPagadorIsEqualToSomething() throws Exception {
        Pagador pagador;
        if (TestUtil.findAll(em, Pagador.class).isEmpty()) {
            contaRecebimentoRepository.saveAndFlush(contaRecebimento);
            pagador = PagadorResourceIT.createEntity(em);
        } else {
            pagador = TestUtil.findAll(em, Pagador.class).get(0);
        }
        em.persist(pagador);
        em.flush();
        contaRecebimento.setPagador(pagador);
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);
        Long pagadorId = pagador.getId();

        // Get all the contaRecebimentoList where pagador equals to pagadorId
        defaultContaRecebimentoShouldBeFound("pagadorId.equals=" + pagadorId);

        // Get all the contaRecebimentoList where pagador equals to (pagadorId + 1)
        defaultContaRecebimentoShouldNotBeFound("pagadorId.equals=" + (pagadorId + 1));
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByCategoriaRecebimentoIsEqualToSomething() throws Exception {
        CategoriaRecebimento categoriaRecebimento;
        if (TestUtil.findAll(em, CategoriaRecebimento.class).isEmpty()) {
            contaRecebimentoRepository.saveAndFlush(contaRecebimento);
            categoriaRecebimento = CategoriaRecebimentoResourceIT.createEntity(em);
        } else {
            categoriaRecebimento = TestUtil.findAll(em, CategoriaRecebimento.class).get(0);
        }
        em.persist(categoriaRecebimento);
        em.flush();
        contaRecebimento.setCategoriaRecebimento(categoriaRecebimento);
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);
        Long categoriaRecebimentoId = categoriaRecebimento.getId();

        // Get all the contaRecebimentoList where categoriaRecebimento equals to categoriaRecebimentoId
        defaultContaRecebimentoShouldBeFound("categoriaRecebimentoId.equals=" + categoriaRecebimentoId);

        // Get all the contaRecebimentoList where categoriaRecebimento equals to (categoriaRecebimentoId + 1)
        defaultContaRecebimentoShouldNotBeFound("categoriaRecebimentoId.equals=" + (categoriaRecebimentoId + 1));
    }

    @Test
    @Transactional
    void getAllContaRecebimentosByCaixaIsEqualToSomething() throws Exception {
        Caixa caixa;
        if (TestUtil.findAll(em, Caixa.class).isEmpty()) {
            contaRecebimentoRepository.saveAndFlush(contaRecebimento);
            caixa = CaixaResourceIT.createEntity(em);
        } else {
            caixa = TestUtil.findAll(em, Caixa.class).get(0);
        }
        em.persist(caixa);
        em.flush();
        contaRecebimento.setCaixa(caixa);
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);
        Long caixaId = caixa.getId();

        // Get all the contaRecebimentoList where caixa equals to caixaId
        defaultContaRecebimentoShouldBeFound("caixaId.equals=" + caixaId);

        // Get all the contaRecebimentoList where caixa equals to (caixaId + 1)
        defaultContaRecebimentoShouldNotBeFound("caixaId.equals=" + (caixaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContaRecebimentoShouldBeFound(String filter) throws Exception {
        restContaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaRecebimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataVencimento").value(hasItem(DEFAULT_DATA_VENCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].dataRecebimento").value(hasItem(DEFAULT_DATA_RECEBIMENTO.toString())))
            .andExpect(jsonPath("$.[*].valorRecebido").value(hasItem(sameNumber(DEFAULT_VALOR_RECEBIDO))))
            .andExpect(jsonPath("$.[*].observacoes").value(hasItem(DEFAULT_OBSERVACOES)))
            .andExpect(jsonPath("$.[*].periodicidade").value(hasItem(DEFAULT_PERIODICIDADE.toString())));

        // Check, that the count call also returns 1
        restContaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContaRecebimentoShouldNotBeFound(String filter) throws Exception {
        restContaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContaRecebimento() throws Exception {
        // Get the contaRecebimento
        restContaRecebimentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContaRecebimento() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        int databaseSizeBeforeUpdate = contaRecebimentoRepository.findAll().size();

        // Update the contaRecebimento
        ContaRecebimento updatedContaRecebimento = contaRecebimentoRepository.findById(contaRecebimento.getId()).get();
        // Disconnect from session so that the updates on updatedContaRecebimento are not directly saved in db
        em.detach(updatedContaRecebimento);
        updatedContaRecebimento
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .observacoes(UPDATED_OBSERVACOES)
            .periodicidade(UPDATED_PERIODICIDADE);
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(updatedContaRecebimento);

        restContaRecebimentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaRecebimentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
        ContaRecebimento testContaRecebimento = contaRecebimentoList.get(contaRecebimentoList.size() - 1);
        assertThat(testContaRecebimento.getDataVencimento()).isEqualTo(UPDATED_DATA_VENCIMENTO);
        assertThat(testContaRecebimento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testContaRecebimento.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testContaRecebimento.getDataRecebimento()).isEqualTo(UPDATED_DATA_RECEBIMENTO);
        assertThat(testContaRecebimento.getValorRecebido()).isEqualByComparingTo(UPDATED_VALOR_RECEBIDO);
        assertThat(testContaRecebimento.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testContaRecebimento.getPeriodicidade()).isEqualTo(UPDATED_PERIODICIDADE);
    }

    @Test
    @Transactional
    void putNonExistingContaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = contaRecebimentoRepository.findAll().size();
        contaRecebimento.setId(count.incrementAndGet());

        // Create the ContaRecebimento
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(contaRecebimento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaRecebimentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaRecebimentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = contaRecebimentoRepository.findAll().size();
        contaRecebimento.setId(count.incrementAndGet());

        // Create the ContaRecebimento
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(contaRecebimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaRecebimentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = contaRecebimentoRepository.findAll().size();
        contaRecebimento.setId(count.incrementAndGet());

        // Create the ContaRecebimento
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(contaRecebimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaRecebimentoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContaRecebimentoWithPatch() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        int databaseSizeBeforeUpdate = contaRecebimentoRepository.findAll().size();

        // Update the contaRecebimento using partial update
        ContaRecebimento partialUpdatedContaRecebimento = new ContaRecebimento();
        partialUpdatedContaRecebimento.setId(contaRecebimento.getId());

        partialUpdatedContaRecebimento
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .observacoes(UPDATED_OBSERVACOES)
            .periodicidade(UPDATED_PERIODICIDADE);

        restContaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaRecebimento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaRecebimento))
            )
            .andExpect(status().isOk());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
        ContaRecebimento testContaRecebimento = contaRecebimentoList.get(contaRecebimentoList.size() - 1);
        assertThat(testContaRecebimento.getDataVencimento()).isEqualTo(UPDATED_DATA_VENCIMENTO);
        assertThat(testContaRecebimento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testContaRecebimento.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testContaRecebimento.getDataRecebimento()).isEqualTo(UPDATED_DATA_RECEBIMENTO);
        assertThat(testContaRecebimento.getValorRecebido()).isEqualByComparingTo(DEFAULT_VALOR_RECEBIDO);
        assertThat(testContaRecebimento.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testContaRecebimento.getPeriodicidade()).isEqualTo(UPDATED_PERIODICIDADE);
    }

    @Test
    @Transactional
    void fullUpdateContaRecebimentoWithPatch() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        int databaseSizeBeforeUpdate = contaRecebimentoRepository.findAll().size();

        // Update the contaRecebimento using partial update
        ContaRecebimento partialUpdatedContaRecebimento = new ContaRecebimento();
        partialUpdatedContaRecebimento.setId(contaRecebimento.getId());

        partialUpdatedContaRecebimento
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .observacoes(UPDATED_OBSERVACOES)
            .periodicidade(UPDATED_PERIODICIDADE);

        restContaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaRecebimento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContaRecebimento))
            )
            .andExpect(status().isOk());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
        ContaRecebimento testContaRecebimento = contaRecebimentoList.get(contaRecebimentoList.size() - 1);
        assertThat(testContaRecebimento.getDataVencimento()).isEqualTo(UPDATED_DATA_VENCIMENTO);
        assertThat(testContaRecebimento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testContaRecebimento.getValor()).isEqualByComparingTo(UPDATED_VALOR);
        assertThat(testContaRecebimento.getDataRecebimento()).isEqualTo(UPDATED_DATA_RECEBIMENTO);
        assertThat(testContaRecebimento.getValorRecebido()).isEqualByComparingTo(UPDATED_VALOR_RECEBIDO);
        assertThat(testContaRecebimento.getObservacoes()).isEqualTo(UPDATED_OBSERVACOES);
        assertThat(testContaRecebimento.getPeriodicidade()).isEqualTo(UPDATED_PERIODICIDADE);
    }

    @Test
    @Transactional
    void patchNonExistingContaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = contaRecebimentoRepository.findAll().size();
        contaRecebimento.setId(count.incrementAndGet());

        // Create the ContaRecebimento
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(contaRecebimento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contaRecebimentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = contaRecebimentoRepository.findAll().size();
        contaRecebimento.setId(count.incrementAndGet());

        // Create the ContaRecebimento
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(contaRecebimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = contaRecebimentoRepository.findAll().size();
        contaRecebimento.setId(count.incrementAndGet());

        // Create the ContaRecebimento
        ContaRecebimentoDTO contaRecebimentoDTO = contaRecebimentoMapper.toDto(contaRecebimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contaRecebimentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaRecebimento in the database
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContaRecebimento() throws Exception {
        // Initialize the database
        contaRecebimentoRepository.saveAndFlush(contaRecebimento);

        int databaseSizeBeforeDelete = contaRecebimentoRepository.findAll().size();

        // Delete the contaRecebimento
        restContaRecebimentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, contaRecebimento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContaRecebimento> contaRecebimentoList = contaRecebimentoRepository.findAll();
        assertThat(contaRecebimentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
