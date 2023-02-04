package br.com.contas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.contas.IntegrationTest;
import br.com.contas.domain.CategoriaPagamento;
import br.com.contas.repository.CategoriaPagamentoRepository;
import br.com.contas.service.criteria.CategoriaPagamentoCriteria;
import br.com.contas.service.dto.CategoriaPagamentoDTO;
import br.com.contas.service.mapper.CategoriaPagamentoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CategoriaPagamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriaPagamentoResourceIT {

    private static final String DEFAULT_NOME_CATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_CATEGORIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categoria-pagamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriaPagamentoRepository categoriaPagamentoRepository;

    @Autowired
    private CategoriaPagamentoMapper categoriaPagamentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriaPagamentoMockMvc;

    private CategoriaPagamento categoriaPagamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaPagamento createEntity(EntityManager em) {
        CategoriaPagamento categoriaPagamento = new CategoriaPagamento().nomeCategoria(DEFAULT_NOME_CATEGORIA);
        return categoriaPagamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaPagamento createUpdatedEntity(EntityManager em) {
        CategoriaPagamento categoriaPagamento = new CategoriaPagamento().nomeCategoria(UPDATED_NOME_CATEGORIA);
        return categoriaPagamento;
    }

    @BeforeEach
    public void initTest() {
        categoriaPagamento = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoriaPagamento() throws Exception {
        int databaseSizeBeforeCreate = categoriaPagamentoRepository.findAll().size();
        // Create the CategoriaPagamento
        CategoriaPagamentoDTO categoriaPagamentoDTO = categoriaPagamentoMapper.toDto(categoriaPagamento);
        restCategoriaPagamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaPagamentoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeCreate + 1);
        CategoriaPagamento testCategoriaPagamento = categoriaPagamentoList.get(categoriaPagamentoList.size() - 1);
        assertThat(testCategoriaPagamento.getNomeCategoria()).isEqualTo(DEFAULT_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void createCategoriaPagamentoWithExistingId() throws Exception {
        // Create the CategoriaPagamento with an existing ID
        categoriaPagamento.setId(1L);
        CategoriaPagamentoDTO categoriaPagamentoDTO = categoriaPagamentoMapper.toDto(categoriaPagamento);

        int databaseSizeBeforeCreate = categoriaPagamentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaPagamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategoriaPagamentos() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        // Get all the categoriaPagamentoList
        restCategoriaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeCategoria").value(hasItem(DEFAULT_NOME_CATEGORIA)));
    }

    @Test
    @Transactional
    void getCategoriaPagamento() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        // Get the categoriaPagamento
        restCategoriaPagamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, categoriaPagamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoriaPagamento.getId().intValue()))
            .andExpect(jsonPath("$.nomeCategoria").value(DEFAULT_NOME_CATEGORIA));
    }

    @Test
    @Transactional
    void getCategoriaPagamentosByIdFiltering() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        Long id = categoriaPagamento.getId();

        defaultCategoriaPagamentoShouldBeFound("id.equals=" + id);
        defaultCategoriaPagamentoShouldNotBeFound("id.notEquals=" + id);

        defaultCategoriaPagamentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoriaPagamentoShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoriaPagamentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoriaPagamentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriaPagamentosByNomeCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        // Get all the categoriaPagamentoList where nomeCategoria equals to DEFAULT_NOME_CATEGORIA
        defaultCategoriaPagamentoShouldBeFound("nomeCategoria.equals=" + DEFAULT_NOME_CATEGORIA);

        // Get all the categoriaPagamentoList where nomeCategoria equals to UPDATED_NOME_CATEGORIA
        defaultCategoriaPagamentoShouldNotBeFound("nomeCategoria.equals=" + UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategoriaPagamentosByNomeCategoriaIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        // Get all the categoriaPagamentoList where nomeCategoria in DEFAULT_NOME_CATEGORIA or UPDATED_NOME_CATEGORIA
        defaultCategoriaPagamentoShouldBeFound("nomeCategoria.in=" + DEFAULT_NOME_CATEGORIA + "," + UPDATED_NOME_CATEGORIA);

        // Get all the categoriaPagamentoList where nomeCategoria equals to UPDATED_NOME_CATEGORIA
        defaultCategoriaPagamentoShouldNotBeFound("nomeCategoria.in=" + UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategoriaPagamentosByNomeCategoriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        // Get all the categoriaPagamentoList where nomeCategoria is not null
        defaultCategoriaPagamentoShouldBeFound("nomeCategoria.specified=true");

        // Get all the categoriaPagamentoList where nomeCategoria is null
        defaultCategoriaPagamentoShouldNotBeFound("nomeCategoria.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriaPagamentosByNomeCategoriaContainsSomething() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        // Get all the categoriaPagamentoList where nomeCategoria contains DEFAULT_NOME_CATEGORIA
        defaultCategoriaPagamentoShouldBeFound("nomeCategoria.contains=" + DEFAULT_NOME_CATEGORIA);

        // Get all the categoriaPagamentoList where nomeCategoria contains UPDATED_NOME_CATEGORIA
        defaultCategoriaPagamentoShouldNotBeFound("nomeCategoria.contains=" + UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategoriaPagamentosByNomeCategoriaNotContainsSomething() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        // Get all the categoriaPagamentoList where nomeCategoria does not contain DEFAULT_NOME_CATEGORIA
        defaultCategoriaPagamentoShouldNotBeFound("nomeCategoria.doesNotContain=" + DEFAULT_NOME_CATEGORIA);

        // Get all the categoriaPagamentoList where nomeCategoria does not contain UPDATED_NOME_CATEGORIA
        defaultCategoriaPagamentoShouldBeFound("nomeCategoria.doesNotContain=" + UPDATED_NOME_CATEGORIA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoriaPagamentoShouldBeFound(String filter) throws Exception {
        restCategoriaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeCategoria").value(hasItem(DEFAULT_NOME_CATEGORIA)));

        // Check, that the count call also returns 1
        restCategoriaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoriaPagamentoShouldNotBeFound(String filter) throws Exception {
        restCategoriaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoriaPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoriaPagamento() throws Exception {
        // Get the categoriaPagamento
        restCategoriaPagamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoriaPagamento() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        int databaseSizeBeforeUpdate = categoriaPagamentoRepository.findAll().size();

        // Update the categoriaPagamento
        CategoriaPagamento updatedCategoriaPagamento = categoriaPagamentoRepository.findById(categoriaPagamento.getId()).get();
        // Disconnect from session so that the updates on updatedCategoriaPagamento are not directly saved in db
        em.detach(updatedCategoriaPagamento);
        updatedCategoriaPagamento.nomeCategoria(UPDATED_NOME_CATEGORIA);
        CategoriaPagamentoDTO categoriaPagamentoDTO = categoriaPagamentoMapper.toDto(updatedCategoriaPagamento);

        restCategoriaPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaPagamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaPagamentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaPagamento testCategoriaPagamento = categoriaPagamentoList.get(categoriaPagamentoList.size() - 1);
        assertThat(testCategoriaPagamento.getNomeCategoria()).isEqualTo(UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void putNonExistingCategoriaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaPagamentoRepository.findAll().size();
        categoriaPagamento.setId(count.incrementAndGet());

        // Create the CategoriaPagamento
        CategoriaPagamentoDTO categoriaPagamentoDTO = categoriaPagamentoMapper.toDto(categoriaPagamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaPagamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoriaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaPagamentoRepository.findAll().size();
        categoriaPagamento.setId(count.incrementAndGet());

        // Create the CategoriaPagamento
        CategoriaPagamentoDTO categoriaPagamentoDTO = categoriaPagamentoMapper.toDto(categoriaPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoriaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaPagamentoRepository.findAll().size();
        categoriaPagamento.setId(count.incrementAndGet());

        // Create the CategoriaPagamento
        CategoriaPagamentoDTO categoriaPagamentoDTO = categoriaPagamentoMapper.toDto(categoriaPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaPagamentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriaPagamentoWithPatch() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        int databaseSizeBeforeUpdate = categoriaPagamentoRepository.findAll().size();

        // Update the categoriaPagamento using partial update
        CategoriaPagamento partialUpdatedCategoriaPagamento = new CategoriaPagamento();
        partialUpdatedCategoriaPagamento.setId(categoriaPagamento.getId());

        restCategoriaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaPagamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaPagamento))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaPagamento testCategoriaPagamento = categoriaPagamentoList.get(categoriaPagamentoList.size() - 1);
        assertThat(testCategoriaPagamento.getNomeCategoria()).isEqualTo(DEFAULT_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void fullUpdateCategoriaPagamentoWithPatch() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        int databaseSizeBeforeUpdate = categoriaPagamentoRepository.findAll().size();

        // Update the categoriaPagamento using partial update
        CategoriaPagamento partialUpdatedCategoriaPagamento = new CategoriaPagamento();
        partialUpdatedCategoriaPagamento.setId(categoriaPagamento.getId());

        partialUpdatedCategoriaPagamento.nomeCategoria(UPDATED_NOME_CATEGORIA);

        restCategoriaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaPagamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaPagamento))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaPagamento testCategoriaPagamento = categoriaPagamentoList.get(categoriaPagamentoList.size() - 1);
        assertThat(testCategoriaPagamento.getNomeCategoria()).isEqualTo(UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void patchNonExistingCategoriaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaPagamentoRepository.findAll().size();
        categoriaPagamento.setId(count.incrementAndGet());

        // Create the CategoriaPagamento
        CategoriaPagamentoDTO categoriaPagamentoDTO = categoriaPagamentoMapper.toDto(categoriaPagamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoriaPagamentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoriaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaPagamentoRepository.findAll().size();
        categoriaPagamento.setId(count.incrementAndGet());

        // Create the CategoriaPagamento
        CategoriaPagamentoDTO categoriaPagamentoDTO = categoriaPagamentoMapper.toDto(categoriaPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoriaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaPagamentoRepository.findAll().size();
        categoriaPagamento.setId(count.incrementAndGet());

        // Create the CategoriaPagamento
        CategoriaPagamentoDTO categoriaPagamentoDTO = categoriaPagamentoMapper.toDto(categoriaPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaPagamentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaPagamento in the database
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoriaPagamento() throws Exception {
        // Initialize the database
        categoriaPagamentoRepository.saveAndFlush(categoriaPagamento);

        int databaseSizeBeforeDelete = categoriaPagamentoRepository.findAll().size();

        // Delete the categoriaPagamento
        restCategoriaPagamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoriaPagamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoriaPagamento> categoriaPagamentoList = categoriaPagamentoRepository.findAll();
        assertThat(categoriaPagamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
