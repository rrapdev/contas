package br.com.contas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.contas.IntegrationTest;
import br.com.contas.domain.Beneficiario;
import br.com.contas.repository.BeneficiarioRepository;
import br.com.contas.service.criteria.BeneficiarioCriteria;
import br.com.contas.service.dto.BeneficiarioDTO;
import br.com.contas.service.mapper.BeneficiarioMapper;
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
 * Integration tests for the {@link BeneficiarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BeneficiarioResourceIT {

    private static final String DEFAULT_NOME_BENEFICIARIO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_BENEFICIARIO = "BBBBBBBBBB";

    private static final String DEFAULT_CPF_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CPF_CNPJ = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/beneficiarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BeneficiarioRepository beneficiarioRepository;

    @Autowired
    private BeneficiarioMapper beneficiarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeneficiarioMockMvc;

    private Beneficiario beneficiario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiario createEntity(EntityManager em) {
        Beneficiario beneficiario = new Beneficiario().nomeBeneficiario(DEFAULT_NOME_BENEFICIARIO).cpfCnpj(DEFAULT_CPF_CNPJ);
        return beneficiario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiario createUpdatedEntity(EntityManager em) {
        Beneficiario beneficiario = new Beneficiario().nomeBeneficiario(UPDATED_NOME_BENEFICIARIO).cpfCnpj(UPDATED_CPF_CNPJ);
        return beneficiario;
    }

    @BeforeEach
    public void initTest() {
        beneficiario = createEntity(em);
    }

    @Test
    @Transactional
    void createBeneficiario() throws Exception {
        int databaseSizeBeforeCreate = beneficiarioRepository.findAll().size();
        // Create the Beneficiario
        BeneficiarioDTO beneficiarioDTO = beneficiarioMapper.toDto(beneficiario);
        restBeneficiarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiarioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeCreate + 1);
        Beneficiario testBeneficiario = beneficiarioList.get(beneficiarioList.size() - 1);
        assertThat(testBeneficiario.getNomeBeneficiario()).isEqualTo(DEFAULT_NOME_BENEFICIARIO);
        assertThat(testBeneficiario.getCpfCnpj()).isEqualTo(DEFAULT_CPF_CNPJ);
    }

    @Test
    @Transactional
    void createBeneficiarioWithExistingId() throws Exception {
        // Create the Beneficiario with an existing ID
        beneficiario.setId(1L);
        BeneficiarioDTO beneficiarioDTO = beneficiarioMapper.toDto(beneficiario);

        int databaseSizeBeforeCreate = beneficiarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeneficiarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBeneficiarios() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList
        restBeneficiarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beneficiario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeBeneficiario").value(hasItem(DEFAULT_NOME_BENEFICIARIO)))
            .andExpect(jsonPath("$.[*].cpfCnpj").value(hasItem(DEFAULT_CPF_CNPJ)));
    }

    @Test
    @Transactional
    void getBeneficiario() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get the beneficiario
        restBeneficiarioMockMvc
            .perform(get(ENTITY_API_URL_ID, beneficiario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beneficiario.getId().intValue()))
            .andExpect(jsonPath("$.nomeBeneficiario").value(DEFAULT_NOME_BENEFICIARIO))
            .andExpect(jsonPath("$.cpfCnpj").value(DEFAULT_CPF_CNPJ));
    }

    @Test
    @Transactional
    void getBeneficiariosByIdFiltering() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        Long id = beneficiario.getId();

        defaultBeneficiarioShouldBeFound("id.equals=" + id);
        defaultBeneficiarioShouldNotBeFound("id.notEquals=" + id);

        defaultBeneficiarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBeneficiarioShouldNotBeFound("id.greaterThan=" + id);

        defaultBeneficiarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBeneficiarioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBeneficiariosByNomeBeneficiarioIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where nomeBeneficiario equals to DEFAULT_NOME_BENEFICIARIO
        defaultBeneficiarioShouldBeFound("nomeBeneficiario.equals=" + DEFAULT_NOME_BENEFICIARIO);

        // Get all the beneficiarioList where nomeBeneficiario equals to UPDATED_NOME_BENEFICIARIO
        defaultBeneficiarioShouldNotBeFound("nomeBeneficiario.equals=" + UPDATED_NOME_BENEFICIARIO);
    }

    @Test
    @Transactional
    void getAllBeneficiariosByNomeBeneficiarioIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where nomeBeneficiario in DEFAULT_NOME_BENEFICIARIO or UPDATED_NOME_BENEFICIARIO
        defaultBeneficiarioShouldBeFound("nomeBeneficiario.in=" + DEFAULT_NOME_BENEFICIARIO + "," + UPDATED_NOME_BENEFICIARIO);

        // Get all the beneficiarioList where nomeBeneficiario equals to UPDATED_NOME_BENEFICIARIO
        defaultBeneficiarioShouldNotBeFound("nomeBeneficiario.in=" + UPDATED_NOME_BENEFICIARIO);
    }

    @Test
    @Transactional
    void getAllBeneficiariosByNomeBeneficiarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where nomeBeneficiario is not null
        defaultBeneficiarioShouldBeFound("nomeBeneficiario.specified=true");

        // Get all the beneficiarioList where nomeBeneficiario is null
        defaultBeneficiarioShouldNotBeFound("nomeBeneficiario.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiariosByNomeBeneficiarioContainsSomething() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where nomeBeneficiario contains DEFAULT_NOME_BENEFICIARIO
        defaultBeneficiarioShouldBeFound("nomeBeneficiario.contains=" + DEFAULT_NOME_BENEFICIARIO);

        // Get all the beneficiarioList where nomeBeneficiario contains UPDATED_NOME_BENEFICIARIO
        defaultBeneficiarioShouldNotBeFound("nomeBeneficiario.contains=" + UPDATED_NOME_BENEFICIARIO);
    }

    @Test
    @Transactional
    void getAllBeneficiariosByNomeBeneficiarioNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where nomeBeneficiario does not contain DEFAULT_NOME_BENEFICIARIO
        defaultBeneficiarioShouldNotBeFound("nomeBeneficiario.doesNotContain=" + DEFAULT_NOME_BENEFICIARIO);

        // Get all the beneficiarioList where nomeBeneficiario does not contain UPDATED_NOME_BENEFICIARIO
        defaultBeneficiarioShouldBeFound("nomeBeneficiario.doesNotContain=" + UPDATED_NOME_BENEFICIARIO);
    }

    @Test
    @Transactional
    void getAllBeneficiariosByCpfCnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where cpfCnpj equals to DEFAULT_CPF_CNPJ
        defaultBeneficiarioShouldBeFound("cpfCnpj.equals=" + DEFAULT_CPF_CNPJ);

        // Get all the beneficiarioList where cpfCnpj equals to UPDATED_CPF_CNPJ
        defaultBeneficiarioShouldNotBeFound("cpfCnpj.equals=" + UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void getAllBeneficiariosByCpfCnpjIsInShouldWork() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where cpfCnpj in DEFAULT_CPF_CNPJ or UPDATED_CPF_CNPJ
        defaultBeneficiarioShouldBeFound("cpfCnpj.in=" + DEFAULT_CPF_CNPJ + "," + UPDATED_CPF_CNPJ);

        // Get all the beneficiarioList where cpfCnpj equals to UPDATED_CPF_CNPJ
        defaultBeneficiarioShouldNotBeFound("cpfCnpj.in=" + UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void getAllBeneficiariosByCpfCnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where cpfCnpj is not null
        defaultBeneficiarioShouldBeFound("cpfCnpj.specified=true");

        // Get all the beneficiarioList where cpfCnpj is null
        defaultBeneficiarioShouldNotBeFound("cpfCnpj.specified=false");
    }

    @Test
    @Transactional
    void getAllBeneficiariosByCpfCnpjContainsSomething() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where cpfCnpj contains DEFAULT_CPF_CNPJ
        defaultBeneficiarioShouldBeFound("cpfCnpj.contains=" + DEFAULT_CPF_CNPJ);

        // Get all the beneficiarioList where cpfCnpj contains UPDATED_CPF_CNPJ
        defaultBeneficiarioShouldNotBeFound("cpfCnpj.contains=" + UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void getAllBeneficiariosByCpfCnpjNotContainsSomething() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        // Get all the beneficiarioList where cpfCnpj does not contain DEFAULT_CPF_CNPJ
        defaultBeneficiarioShouldNotBeFound("cpfCnpj.doesNotContain=" + DEFAULT_CPF_CNPJ);

        // Get all the beneficiarioList where cpfCnpj does not contain UPDATED_CPF_CNPJ
        defaultBeneficiarioShouldBeFound("cpfCnpj.doesNotContain=" + UPDATED_CPF_CNPJ);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBeneficiarioShouldBeFound(String filter) throws Exception {
        restBeneficiarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beneficiario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeBeneficiario").value(hasItem(DEFAULT_NOME_BENEFICIARIO)))
            .andExpect(jsonPath("$.[*].cpfCnpj").value(hasItem(DEFAULT_CPF_CNPJ)));

        // Check, that the count call also returns 1
        restBeneficiarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBeneficiarioShouldNotBeFound(String filter) throws Exception {
        restBeneficiarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBeneficiarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBeneficiario() throws Exception {
        // Get the beneficiario
        restBeneficiarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBeneficiario() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        int databaseSizeBeforeUpdate = beneficiarioRepository.findAll().size();

        // Update the beneficiario
        Beneficiario updatedBeneficiario = beneficiarioRepository.findById(beneficiario.getId()).get();
        // Disconnect from session so that the updates on updatedBeneficiario are not directly saved in db
        em.detach(updatedBeneficiario);
        updatedBeneficiario.nomeBeneficiario(UPDATED_NOME_BENEFICIARIO).cpfCnpj(UPDATED_CPF_CNPJ);
        BeneficiarioDTO beneficiarioDTO = beneficiarioMapper.toDto(updatedBeneficiario);

        restBeneficiarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beneficiarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeUpdate);
        Beneficiario testBeneficiario = beneficiarioList.get(beneficiarioList.size() - 1);
        assertThat(testBeneficiario.getNomeBeneficiario()).isEqualTo(UPDATED_NOME_BENEFICIARIO);
        assertThat(testBeneficiario.getCpfCnpj()).isEqualTo(UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void putNonExistingBeneficiario() throws Exception {
        int databaseSizeBeforeUpdate = beneficiarioRepository.findAll().size();
        beneficiario.setId(count.incrementAndGet());

        // Create the Beneficiario
        BeneficiarioDTO beneficiarioDTO = beneficiarioMapper.toDto(beneficiario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beneficiarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBeneficiario() throws Exception {
        int databaseSizeBeforeUpdate = beneficiarioRepository.findAll().size();
        beneficiario.setId(count.incrementAndGet());

        // Create the Beneficiario
        BeneficiarioDTO beneficiarioDTO = beneficiarioMapper.toDto(beneficiario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBeneficiario() throws Exception {
        int databaseSizeBeforeUpdate = beneficiarioRepository.findAll().size();
        beneficiario.setId(count.incrementAndGet());

        // Create the Beneficiario
        BeneficiarioDTO beneficiarioDTO = beneficiarioMapper.toDto(beneficiario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiarioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBeneficiarioWithPatch() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        int databaseSizeBeforeUpdate = beneficiarioRepository.findAll().size();

        // Update the beneficiario using partial update
        Beneficiario partialUpdatedBeneficiario = new Beneficiario();
        partialUpdatedBeneficiario.setId(beneficiario.getId());

        partialUpdatedBeneficiario.nomeBeneficiario(UPDATED_NOME_BENEFICIARIO);

        restBeneficiarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiario))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeUpdate);
        Beneficiario testBeneficiario = beneficiarioList.get(beneficiarioList.size() - 1);
        assertThat(testBeneficiario.getNomeBeneficiario()).isEqualTo(UPDATED_NOME_BENEFICIARIO);
        assertThat(testBeneficiario.getCpfCnpj()).isEqualTo(DEFAULT_CPF_CNPJ);
    }

    @Test
    @Transactional
    void fullUpdateBeneficiarioWithPatch() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        int databaseSizeBeforeUpdate = beneficiarioRepository.findAll().size();

        // Update the beneficiario using partial update
        Beneficiario partialUpdatedBeneficiario = new Beneficiario();
        partialUpdatedBeneficiario.setId(beneficiario.getId());

        partialUpdatedBeneficiario.nomeBeneficiario(UPDATED_NOME_BENEFICIARIO).cpfCnpj(UPDATED_CPF_CNPJ);

        restBeneficiarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiario))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeUpdate);
        Beneficiario testBeneficiario = beneficiarioList.get(beneficiarioList.size() - 1);
        assertThat(testBeneficiario.getNomeBeneficiario()).isEqualTo(UPDATED_NOME_BENEFICIARIO);
        assertThat(testBeneficiario.getCpfCnpj()).isEqualTo(UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void patchNonExistingBeneficiario() throws Exception {
        int databaseSizeBeforeUpdate = beneficiarioRepository.findAll().size();
        beneficiario.setId(count.incrementAndGet());

        // Create the Beneficiario
        BeneficiarioDTO beneficiarioDTO = beneficiarioMapper.toDto(beneficiario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, beneficiarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBeneficiario() throws Exception {
        int databaseSizeBeforeUpdate = beneficiarioRepository.findAll().size();
        beneficiario.setId(count.incrementAndGet());

        // Create the Beneficiario
        BeneficiarioDTO beneficiarioDTO = beneficiarioMapper.toDto(beneficiario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBeneficiario() throws Exception {
        int databaseSizeBeforeUpdate = beneficiarioRepository.findAll().size();
        beneficiario.setId(count.incrementAndGet());

        // Create the Beneficiario
        BeneficiarioDTO beneficiarioDTO = beneficiarioMapper.toDto(beneficiario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiarioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiario in the database
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBeneficiario() throws Exception {
        // Initialize the database
        beneficiarioRepository.saveAndFlush(beneficiario);

        int databaseSizeBeforeDelete = beneficiarioRepository.findAll().size();

        // Delete the beneficiario
        restBeneficiarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, beneficiario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Beneficiario> beneficiarioList = beneficiarioRepository.findAll();
        assertThat(beneficiarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
