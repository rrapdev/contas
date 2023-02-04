package br.com.contas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.contas.IntegrationTest;
import br.com.contas.domain.Caixa;
import br.com.contas.repository.CaixaRepository;
import br.com.contas.service.criteria.CaixaCriteria;
import br.com.contas.service.dto.CaixaDTO;
import br.com.contas.service.mapper.CaixaMapper;
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
 * Integration tests for the {@link CaixaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaixaResourceIT {

    private static final String DEFAULT_NOME_CAIXA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_CAIXA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/caixas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CaixaRepository caixaRepository;

    @Autowired
    private CaixaMapper caixaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaixaMockMvc;

    private Caixa caixa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caixa createEntity(EntityManager em) {
        Caixa caixa = new Caixa().nomeCaixa(DEFAULT_NOME_CAIXA);
        return caixa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caixa createUpdatedEntity(EntityManager em) {
        Caixa caixa = new Caixa().nomeCaixa(UPDATED_NOME_CAIXA);
        return caixa;
    }

    @BeforeEach
    public void initTest() {
        caixa = createEntity(em);
    }

    @Test
    @Transactional
    void createCaixa() throws Exception {
        int databaseSizeBeforeCreate = caixaRepository.findAll().size();
        // Create the Caixa
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);
        restCaixaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isCreated());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeCreate + 1);
        Caixa testCaixa = caixaList.get(caixaList.size() - 1);
        assertThat(testCaixa.getNomeCaixa()).isEqualTo(DEFAULT_NOME_CAIXA);
    }

    @Test
    @Transactional
    void createCaixaWithExistingId() throws Exception {
        // Create the Caixa with an existing ID
        caixa.setId(1L);
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        int databaseSizeBeforeCreate = caixaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaixaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCaixas() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        // Get all the caixaList
        restCaixaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caixa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeCaixa").value(hasItem(DEFAULT_NOME_CAIXA)));
    }

    @Test
    @Transactional
    void getCaixa() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        // Get the caixa
        restCaixaMockMvc
            .perform(get(ENTITY_API_URL_ID, caixa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caixa.getId().intValue()))
            .andExpect(jsonPath("$.nomeCaixa").value(DEFAULT_NOME_CAIXA));
    }

    @Test
    @Transactional
    void getCaixasByIdFiltering() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        Long id = caixa.getId();

        defaultCaixaShouldBeFound("id.equals=" + id);
        defaultCaixaShouldNotBeFound("id.notEquals=" + id);

        defaultCaixaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCaixaShouldNotBeFound("id.greaterThan=" + id);

        defaultCaixaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCaixaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCaixasByNomeCaixaIsEqualToSomething() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        // Get all the caixaList where nomeCaixa equals to DEFAULT_NOME_CAIXA
        defaultCaixaShouldBeFound("nomeCaixa.equals=" + DEFAULT_NOME_CAIXA);

        // Get all the caixaList where nomeCaixa equals to UPDATED_NOME_CAIXA
        defaultCaixaShouldNotBeFound("nomeCaixa.equals=" + UPDATED_NOME_CAIXA);
    }

    @Test
    @Transactional
    void getAllCaixasByNomeCaixaIsInShouldWork() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        // Get all the caixaList where nomeCaixa in DEFAULT_NOME_CAIXA or UPDATED_NOME_CAIXA
        defaultCaixaShouldBeFound("nomeCaixa.in=" + DEFAULT_NOME_CAIXA + "," + UPDATED_NOME_CAIXA);

        // Get all the caixaList where nomeCaixa equals to UPDATED_NOME_CAIXA
        defaultCaixaShouldNotBeFound("nomeCaixa.in=" + UPDATED_NOME_CAIXA);
    }

    @Test
    @Transactional
    void getAllCaixasByNomeCaixaIsNullOrNotNull() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        // Get all the caixaList where nomeCaixa is not null
        defaultCaixaShouldBeFound("nomeCaixa.specified=true");

        // Get all the caixaList where nomeCaixa is null
        defaultCaixaShouldNotBeFound("nomeCaixa.specified=false");
    }

    @Test
    @Transactional
    void getAllCaixasByNomeCaixaContainsSomething() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        // Get all the caixaList where nomeCaixa contains DEFAULT_NOME_CAIXA
        defaultCaixaShouldBeFound("nomeCaixa.contains=" + DEFAULT_NOME_CAIXA);

        // Get all the caixaList where nomeCaixa contains UPDATED_NOME_CAIXA
        defaultCaixaShouldNotBeFound("nomeCaixa.contains=" + UPDATED_NOME_CAIXA);
    }

    @Test
    @Transactional
    void getAllCaixasByNomeCaixaNotContainsSomething() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        // Get all the caixaList where nomeCaixa does not contain DEFAULT_NOME_CAIXA
        defaultCaixaShouldNotBeFound("nomeCaixa.doesNotContain=" + DEFAULT_NOME_CAIXA);

        // Get all the caixaList where nomeCaixa does not contain UPDATED_NOME_CAIXA
        defaultCaixaShouldBeFound("nomeCaixa.doesNotContain=" + UPDATED_NOME_CAIXA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCaixaShouldBeFound(String filter) throws Exception {
        restCaixaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caixa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeCaixa").value(hasItem(DEFAULT_NOME_CAIXA)));

        // Check, that the count call also returns 1
        restCaixaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCaixaShouldNotBeFound(String filter) throws Exception {
        restCaixaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCaixaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCaixa() throws Exception {
        // Get the caixa
        restCaixaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCaixa() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();

        // Update the caixa
        Caixa updatedCaixa = caixaRepository.findById(caixa.getId()).get();
        // Disconnect from session so that the updates on updatedCaixa are not directly saved in db
        em.detach(updatedCaixa);
        updatedCaixa.nomeCaixa(UPDATED_NOME_CAIXA);
        CaixaDTO caixaDTO = caixaMapper.toDto(updatedCaixa);

        restCaixaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caixaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caixaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
        Caixa testCaixa = caixaList.get(caixaList.size() - 1);
        assertThat(testCaixa.getNomeCaixa()).isEqualTo(UPDATED_NOME_CAIXA);
    }

    @Test
    @Transactional
    void putNonExistingCaixa() throws Exception {
        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();
        caixa.setId(count.incrementAndGet());

        // Create the Caixa
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaixaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caixaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caixaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaixa() throws Exception {
        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();
        caixa.setId(count.incrementAndGet());

        // Create the Caixa
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaixaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caixaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaixa() throws Exception {
        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();
        caixa.setId(count.incrementAndGet());

        // Create the Caixa
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaixaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaixaWithPatch() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();

        // Update the caixa using partial update
        Caixa partialUpdatedCaixa = new Caixa();
        partialUpdatedCaixa.setId(caixa.getId());

        restCaixaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaixa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaixa))
            )
            .andExpect(status().isOk());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
        Caixa testCaixa = caixaList.get(caixaList.size() - 1);
        assertThat(testCaixa.getNomeCaixa()).isEqualTo(DEFAULT_NOME_CAIXA);
    }

    @Test
    @Transactional
    void fullUpdateCaixaWithPatch() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();

        // Update the caixa using partial update
        Caixa partialUpdatedCaixa = new Caixa();
        partialUpdatedCaixa.setId(caixa.getId());

        partialUpdatedCaixa.nomeCaixa(UPDATED_NOME_CAIXA);

        restCaixaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaixa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaixa))
            )
            .andExpect(status().isOk());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
        Caixa testCaixa = caixaList.get(caixaList.size() - 1);
        assertThat(testCaixa.getNomeCaixa()).isEqualTo(UPDATED_NOME_CAIXA);
    }

    @Test
    @Transactional
    void patchNonExistingCaixa() throws Exception {
        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();
        caixa.setId(count.incrementAndGet());

        // Create the Caixa
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaixaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caixaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caixaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaixa() throws Exception {
        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();
        caixa.setId(count.incrementAndGet());

        // Create the Caixa
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaixaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caixaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaixa() throws Exception {
        int databaseSizeBeforeUpdate = caixaRepository.findAll().size();
        caixa.setId(count.incrementAndGet());

        // Create the Caixa
        CaixaDTO caixaDTO = caixaMapper.toDto(caixa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaixaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(caixaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caixa in the database
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaixa() throws Exception {
        // Initialize the database
        caixaRepository.saveAndFlush(caixa);

        int databaseSizeBeforeDelete = caixaRepository.findAll().size();

        // Delete the caixa
        restCaixaMockMvc
            .perform(delete(ENTITY_API_URL_ID, caixa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caixa> caixaList = caixaRepository.findAll();
        assertThat(caixaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
