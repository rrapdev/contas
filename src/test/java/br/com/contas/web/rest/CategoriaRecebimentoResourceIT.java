package br.com.contas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.contas.IntegrationTest;
import br.com.contas.domain.CategoriaRecebimento;
import br.com.contas.repository.CategoriaRecebimentoRepository;
import br.com.contas.service.criteria.CategoriaRecebimentoCriteria;
import br.com.contas.service.dto.CategoriaRecebimentoDTO;
import br.com.contas.service.mapper.CategoriaRecebimentoMapper;
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
 * Integration tests for the {@link CategoriaRecebimentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriaRecebimentoResourceIT {

    private static final String DEFAULT_NOME_CATEGORIA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_CATEGORIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categoria-recebimentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriaRecebimentoRepository categoriaRecebimentoRepository;

    @Autowired
    private CategoriaRecebimentoMapper categoriaRecebimentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriaRecebimentoMockMvc;

    private CategoriaRecebimento categoriaRecebimento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaRecebimento createEntity(EntityManager em) {
        CategoriaRecebimento categoriaRecebimento = new CategoriaRecebimento().nomeCategoria(DEFAULT_NOME_CATEGORIA);
        return categoriaRecebimento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaRecebimento createUpdatedEntity(EntityManager em) {
        CategoriaRecebimento categoriaRecebimento = new CategoriaRecebimento().nomeCategoria(UPDATED_NOME_CATEGORIA);
        return categoriaRecebimento;
    }

    @BeforeEach
    public void initTest() {
        categoriaRecebimento = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoriaRecebimento() throws Exception {
        int databaseSizeBeforeCreate = categoriaRecebimentoRepository.findAll().size();
        // Create the CategoriaRecebimento
        CategoriaRecebimentoDTO categoriaRecebimentoDTO = categoriaRecebimentoMapper.toDto(categoriaRecebimento);
        restCategoriaRecebimentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaRecebimentoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeCreate + 1);
        CategoriaRecebimento testCategoriaRecebimento = categoriaRecebimentoList.get(categoriaRecebimentoList.size() - 1);
        assertThat(testCategoriaRecebimento.getNomeCategoria()).isEqualTo(DEFAULT_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void createCategoriaRecebimentoWithExistingId() throws Exception {
        // Create the CategoriaRecebimento with an existing ID
        categoriaRecebimento.setId(1L);
        CategoriaRecebimentoDTO categoriaRecebimentoDTO = categoriaRecebimentoMapper.toDto(categoriaRecebimento);

        int databaseSizeBeforeCreate = categoriaRecebimentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaRecebimentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategoriaRecebimentos() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        // Get all the categoriaRecebimentoList
        restCategoriaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaRecebimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeCategoria").value(hasItem(DEFAULT_NOME_CATEGORIA)));
    }

    @Test
    @Transactional
    void getCategoriaRecebimento() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        // Get the categoriaRecebimento
        restCategoriaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL_ID, categoriaRecebimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoriaRecebimento.getId().intValue()))
            .andExpect(jsonPath("$.nomeCategoria").value(DEFAULT_NOME_CATEGORIA));
    }

    @Test
    @Transactional
    void getCategoriaRecebimentosByIdFiltering() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        Long id = categoriaRecebimento.getId();

        defaultCategoriaRecebimentoShouldBeFound("id.equals=" + id);
        defaultCategoriaRecebimentoShouldNotBeFound("id.notEquals=" + id);

        defaultCategoriaRecebimentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoriaRecebimentoShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoriaRecebimentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoriaRecebimentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriaRecebimentosByNomeCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        // Get all the categoriaRecebimentoList where nomeCategoria equals to DEFAULT_NOME_CATEGORIA
        defaultCategoriaRecebimentoShouldBeFound("nomeCategoria.equals=" + DEFAULT_NOME_CATEGORIA);

        // Get all the categoriaRecebimentoList where nomeCategoria equals to UPDATED_NOME_CATEGORIA
        defaultCategoriaRecebimentoShouldNotBeFound("nomeCategoria.equals=" + UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategoriaRecebimentosByNomeCategoriaIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        // Get all the categoriaRecebimentoList where nomeCategoria in DEFAULT_NOME_CATEGORIA or UPDATED_NOME_CATEGORIA
        defaultCategoriaRecebimentoShouldBeFound("nomeCategoria.in=" + DEFAULT_NOME_CATEGORIA + "," + UPDATED_NOME_CATEGORIA);

        // Get all the categoriaRecebimentoList where nomeCategoria equals to UPDATED_NOME_CATEGORIA
        defaultCategoriaRecebimentoShouldNotBeFound("nomeCategoria.in=" + UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategoriaRecebimentosByNomeCategoriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        // Get all the categoriaRecebimentoList where nomeCategoria is not null
        defaultCategoriaRecebimentoShouldBeFound("nomeCategoria.specified=true");

        // Get all the categoriaRecebimentoList where nomeCategoria is null
        defaultCategoriaRecebimentoShouldNotBeFound("nomeCategoria.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriaRecebimentosByNomeCategoriaContainsSomething() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        // Get all the categoriaRecebimentoList where nomeCategoria contains DEFAULT_NOME_CATEGORIA
        defaultCategoriaRecebimentoShouldBeFound("nomeCategoria.contains=" + DEFAULT_NOME_CATEGORIA);

        // Get all the categoriaRecebimentoList where nomeCategoria contains UPDATED_NOME_CATEGORIA
        defaultCategoriaRecebimentoShouldNotBeFound("nomeCategoria.contains=" + UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllCategoriaRecebimentosByNomeCategoriaNotContainsSomething() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        // Get all the categoriaRecebimentoList where nomeCategoria does not contain DEFAULT_NOME_CATEGORIA
        defaultCategoriaRecebimentoShouldNotBeFound("nomeCategoria.doesNotContain=" + DEFAULT_NOME_CATEGORIA);

        // Get all the categoriaRecebimentoList where nomeCategoria does not contain UPDATED_NOME_CATEGORIA
        defaultCategoriaRecebimentoShouldBeFound("nomeCategoria.doesNotContain=" + UPDATED_NOME_CATEGORIA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoriaRecebimentoShouldBeFound(String filter) throws Exception {
        restCategoriaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaRecebimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeCategoria").value(hasItem(DEFAULT_NOME_CATEGORIA)));

        // Check, that the count call also returns 1
        restCategoriaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoriaRecebimentoShouldNotBeFound(String filter) throws Exception {
        restCategoriaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoriaRecebimentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoriaRecebimento() throws Exception {
        // Get the categoriaRecebimento
        restCategoriaRecebimentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoriaRecebimento() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        int databaseSizeBeforeUpdate = categoriaRecebimentoRepository.findAll().size();

        // Update the categoriaRecebimento
        CategoriaRecebimento updatedCategoriaRecebimento = categoriaRecebimentoRepository.findById(categoriaRecebimento.getId()).get();
        // Disconnect from session so that the updates on updatedCategoriaRecebimento are not directly saved in db
        em.detach(updatedCategoriaRecebimento);
        updatedCategoriaRecebimento.nomeCategoria(UPDATED_NOME_CATEGORIA);
        CategoriaRecebimentoDTO categoriaRecebimentoDTO = categoriaRecebimentoMapper.toDto(updatedCategoriaRecebimento);

        restCategoriaRecebimentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaRecebimentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaRecebimentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaRecebimento testCategoriaRecebimento = categoriaRecebimentoList.get(categoriaRecebimentoList.size() - 1);
        assertThat(testCategoriaRecebimento.getNomeCategoria()).isEqualTo(UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void putNonExistingCategoriaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRecebimentoRepository.findAll().size();
        categoriaRecebimento.setId(count.incrementAndGet());

        // Create the CategoriaRecebimento
        CategoriaRecebimentoDTO categoriaRecebimentoDTO = categoriaRecebimentoMapper.toDto(categoriaRecebimento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaRecebimentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaRecebimentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoriaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRecebimentoRepository.findAll().size();
        categoriaRecebimento.setId(count.incrementAndGet());

        // Create the CategoriaRecebimento
        CategoriaRecebimentoDTO categoriaRecebimentoDTO = categoriaRecebimentoMapper.toDto(categoriaRecebimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaRecebimentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoriaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRecebimentoRepository.findAll().size();
        categoriaRecebimento.setId(count.incrementAndGet());

        // Create the CategoriaRecebimento
        CategoriaRecebimentoDTO categoriaRecebimentoDTO = categoriaRecebimentoMapper.toDto(categoriaRecebimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaRecebimentoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaRecebimentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriaRecebimentoWithPatch() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        int databaseSizeBeforeUpdate = categoriaRecebimentoRepository.findAll().size();

        // Update the categoriaRecebimento using partial update
        CategoriaRecebimento partialUpdatedCategoriaRecebimento = new CategoriaRecebimento();
        partialUpdatedCategoriaRecebimento.setId(categoriaRecebimento.getId());

        partialUpdatedCategoriaRecebimento.nomeCategoria(UPDATED_NOME_CATEGORIA);

        restCategoriaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaRecebimento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaRecebimento))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaRecebimento testCategoriaRecebimento = categoriaRecebimentoList.get(categoriaRecebimentoList.size() - 1);
        assertThat(testCategoriaRecebimento.getNomeCategoria()).isEqualTo(UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void fullUpdateCategoriaRecebimentoWithPatch() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        int databaseSizeBeforeUpdate = categoriaRecebimentoRepository.findAll().size();

        // Update the categoriaRecebimento using partial update
        CategoriaRecebimento partialUpdatedCategoriaRecebimento = new CategoriaRecebimento();
        partialUpdatedCategoriaRecebimento.setId(categoriaRecebimento.getId());

        partialUpdatedCategoriaRecebimento.nomeCategoria(UPDATED_NOME_CATEGORIA);

        restCategoriaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaRecebimento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaRecebimento))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaRecebimento testCategoriaRecebimento = categoriaRecebimentoList.get(categoriaRecebimentoList.size() - 1);
        assertThat(testCategoriaRecebimento.getNomeCategoria()).isEqualTo(UPDATED_NOME_CATEGORIA);
    }

    @Test
    @Transactional
    void patchNonExistingCategoriaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRecebimentoRepository.findAll().size();
        categoriaRecebimento.setId(count.incrementAndGet());

        // Create the CategoriaRecebimento
        CategoriaRecebimentoDTO categoriaRecebimentoDTO = categoriaRecebimentoMapper.toDto(categoriaRecebimento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoriaRecebimentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoriaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRecebimentoRepository.findAll().size();
        categoriaRecebimento.setId(count.incrementAndGet());

        // Create the CategoriaRecebimento
        CategoriaRecebimentoDTO categoriaRecebimentoDTO = categoriaRecebimentoMapper.toDto(categoriaRecebimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaRecebimentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoriaRecebimento() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRecebimentoRepository.findAll().size();
        categoriaRecebimento.setId(count.incrementAndGet());

        // Create the CategoriaRecebimento
        CategoriaRecebimentoDTO categoriaRecebimentoDTO = categoriaRecebimentoMapper.toDto(categoriaRecebimento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaRecebimentoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaRecebimentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaRecebimento in the database
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoriaRecebimento() throws Exception {
        // Initialize the database
        categoriaRecebimentoRepository.saveAndFlush(categoriaRecebimento);

        int databaseSizeBeforeDelete = categoriaRecebimentoRepository.findAll().size();

        // Delete the categoriaRecebimento
        restCategoriaRecebimentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoriaRecebimento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoriaRecebimento> categoriaRecebimentoList = categoriaRecebimentoRepository.findAll();
        assertThat(categoriaRecebimentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
