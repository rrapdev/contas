package br.com.contas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.contas.IntegrationTest;
import br.com.contas.domain.Pagador;
import br.com.contas.repository.PagadorRepository;
import br.com.contas.service.criteria.PagadorCriteria;
import br.com.contas.service.dto.PagadorDTO;
import br.com.contas.service.mapper.PagadorMapper;
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
 * Integration tests for the {@link PagadorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PagadorResourceIT {

    private static final String DEFAULT_NOME_PAGADOR = "AAAAAAAAAA";
    private static final String UPDATED_NOME_PAGADOR = "BBBBBBBBBB";

    private static final String DEFAULT_CPF_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CPF_CNPJ = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pagadors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PagadorRepository pagadorRepository;

    @Autowired
    private PagadorMapper pagadorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPagadorMockMvc;

    private Pagador pagador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pagador createEntity(EntityManager em) {
        Pagador pagador = new Pagador().nomePagador(DEFAULT_NOME_PAGADOR).cpfCnpj(DEFAULT_CPF_CNPJ);
        return pagador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pagador createUpdatedEntity(EntityManager em) {
        Pagador pagador = new Pagador().nomePagador(UPDATED_NOME_PAGADOR).cpfCnpj(UPDATED_CPF_CNPJ);
        return pagador;
    }

    @BeforeEach
    public void initTest() {
        pagador = createEntity(em);
    }

    @Test
    @Transactional
    void createPagador() throws Exception {
        int databaseSizeBeforeCreate = pagadorRepository.findAll().size();
        // Create the Pagador
        PagadorDTO pagadorDTO = pagadorMapper.toDto(pagador);
        restPagadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagadorDTO)))
            .andExpect(status().isCreated());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeCreate + 1);
        Pagador testPagador = pagadorList.get(pagadorList.size() - 1);
        assertThat(testPagador.getNomePagador()).isEqualTo(DEFAULT_NOME_PAGADOR);
        assertThat(testPagador.getCpfCnpj()).isEqualTo(DEFAULT_CPF_CNPJ);
    }

    @Test
    @Transactional
    void createPagadorWithExistingId() throws Exception {
        // Create the Pagador with an existing ID
        pagador.setId(1L);
        PagadorDTO pagadorDTO = pagadorMapper.toDto(pagador);

        int databaseSizeBeforeCreate = pagadorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagadorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPagadors() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList
        restPagadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pagador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomePagador").value(hasItem(DEFAULT_NOME_PAGADOR)))
            .andExpect(jsonPath("$.[*].cpfCnpj").value(hasItem(DEFAULT_CPF_CNPJ)));
    }

    @Test
    @Transactional
    void getPagador() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get the pagador
        restPagadorMockMvc
            .perform(get(ENTITY_API_URL_ID, pagador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pagador.getId().intValue()))
            .andExpect(jsonPath("$.nomePagador").value(DEFAULT_NOME_PAGADOR))
            .andExpect(jsonPath("$.cpfCnpj").value(DEFAULT_CPF_CNPJ));
    }

    @Test
    @Transactional
    void getPagadorsByIdFiltering() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        Long id = pagador.getId();

        defaultPagadorShouldBeFound("id.equals=" + id);
        defaultPagadorShouldNotBeFound("id.notEquals=" + id);

        defaultPagadorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPagadorShouldNotBeFound("id.greaterThan=" + id);

        defaultPagadorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPagadorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPagadorsByNomePagadorIsEqualToSomething() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where nomePagador equals to DEFAULT_NOME_PAGADOR
        defaultPagadorShouldBeFound("nomePagador.equals=" + DEFAULT_NOME_PAGADOR);

        // Get all the pagadorList where nomePagador equals to UPDATED_NOME_PAGADOR
        defaultPagadorShouldNotBeFound("nomePagador.equals=" + UPDATED_NOME_PAGADOR);
    }

    @Test
    @Transactional
    void getAllPagadorsByNomePagadorIsInShouldWork() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where nomePagador in DEFAULT_NOME_PAGADOR or UPDATED_NOME_PAGADOR
        defaultPagadorShouldBeFound("nomePagador.in=" + DEFAULT_NOME_PAGADOR + "," + UPDATED_NOME_PAGADOR);

        // Get all the pagadorList where nomePagador equals to UPDATED_NOME_PAGADOR
        defaultPagadorShouldNotBeFound("nomePagador.in=" + UPDATED_NOME_PAGADOR);
    }

    @Test
    @Transactional
    void getAllPagadorsByNomePagadorIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where nomePagador is not null
        defaultPagadorShouldBeFound("nomePagador.specified=true");

        // Get all the pagadorList where nomePagador is null
        defaultPagadorShouldNotBeFound("nomePagador.specified=false");
    }

    @Test
    @Transactional
    void getAllPagadorsByNomePagadorContainsSomething() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where nomePagador contains DEFAULT_NOME_PAGADOR
        defaultPagadorShouldBeFound("nomePagador.contains=" + DEFAULT_NOME_PAGADOR);

        // Get all the pagadorList where nomePagador contains UPDATED_NOME_PAGADOR
        defaultPagadorShouldNotBeFound("nomePagador.contains=" + UPDATED_NOME_PAGADOR);
    }

    @Test
    @Transactional
    void getAllPagadorsByNomePagadorNotContainsSomething() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where nomePagador does not contain DEFAULT_NOME_PAGADOR
        defaultPagadorShouldNotBeFound("nomePagador.doesNotContain=" + DEFAULT_NOME_PAGADOR);

        // Get all the pagadorList where nomePagador does not contain UPDATED_NOME_PAGADOR
        defaultPagadorShouldBeFound("nomePagador.doesNotContain=" + UPDATED_NOME_PAGADOR);
    }

    @Test
    @Transactional
    void getAllPagadorsByCpfCnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where cpfCnpj equals to DEFAULT_CPF_CNPJ
        defaultPagadorShouldBeFound("cpfCnpj.equals=" + DEFAULT_CPF_CNPJ);

        // Get all the pagadorList where cpfCnpj equals to UPDATED_CPF_CNPJ
        defaultPagadorShouldNotBeFound("cpfCnpj.equals=" + UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void getAllPagadorsByCpfCnpjIsInShouldWork() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where cpfCnpj in DEFAULT_CPF_CNPJ or UPDATED_CPF_CNPJ
        defaultPagadorShouldBeFound("cpfCnpj.in=" + DEFAULT_CPF_CNPJ + "," + UPDATED_CPF_CNPJ);

        // Get all the pagadorList where cpfCnpj equals to UPDATED_CPF_CNPJ
        defaultPagadorShouldNotBeFound("cpfCnpj.in=" + UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void getAllPagadorsByCpfCnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where cpfCnpj is not null
        defaultPagadorShouldBeFound("cpfCnpj.specified=true");

        // Get all the pagadorList where cpfCnpj is null
        defaultPagadorShouldNotBeFound("cpfCnpj.specified=false");
    }

    @Test
    @Transactional
    void getAllPagadorsByCpfCnpjContainsSomething() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where cpfCnpj contains DEFAULT_CPF_CNPJ
        defaultPagadorShouldBeFound("cpfCnpj.contains=" + DEFAULT_CPF_CNPJ);

        // Get all the pagadorList where cpfCnpj contains UPDATED_CPF_CNPJ
        defaultPagadorShouldNotBeFound("cpfCnpj.contains=" + UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void getAllPagadorsByCpfCnpjNotContainsSomething() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        // Get all the pagadorList where cpfCnpj does not contain DEFAULT_CPF_CNPJ
        defaultPagadorShouldNotBeFound("cpfCnpj.doesNotContain=" + DEFAULT_CPF_CNPJ);

        // Get all the pagadorList where cpfCnpj does not contain UPDATED_CPF_CNPJ
        defaultPagadorShouldBeFound("cpfCnpj.doesNotContain=" + UPDATED_CPF_CNPJ);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPagadorShouldBeFound(String filter) throws Exception {
        restPagadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pagador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomePagador").value(hasItem(DEFAULT_NOME_PAGADOR)))
            .andExpect(jsonPath("$.[*].cpfCnpj").value(hasItem(DEFAULT_CPF_CNPJ)));

        // Check, that the count call also returns 1
        restPagadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPagadorShouldNotBeFound(String filter) throws Exception {
        restPagadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPagadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPagador() throws Exception {
        // Get the pagador
        restPagadorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPagador() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        int databaseSizeBeforeUpdate = pagadorRepository.findAll().size();

        // Update the pagador
        Pagador updatedPagador = pagadorRepository.findById(pagador.getId()).get();
        // Disconnect from session so that the updates on updatedPagador are not directly saved in db
        em.detach(updatedPagador);
        updatedPagador.nomePagador(UPDATED_NOME_PAGADOR).cpfCnpj(UPDATED_CPF_CNPJ);
        PagadorDTO pagadorDTO = pagadorMapper.toDto(updatedPagador);

        restPagadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pagadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagadorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeUpdate);
        Pagador testPagador = pagadorList.get(pagadorList.size() - 1);
        assertThat(testPagador.getNomePagador()).isEqualTo(UPDATED_NOME_PAGADOR);
        assertThat(testPagador.getCpfCnpj()).isEqualTo(UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void putNonExistingPagador() throws Exception {
        int databaseSizeBeforeUpdate = pagadorRepository.findAll().size();
        pagador.setId(count.incrementAndGet());

        // Create the Pagador
        PagadorDTO pagadorDTO = pagadorMapper.toDto(pagador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pagadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPagador() throws Exception {
        int databaseSizeBeforeUpdate = pagadorRepository.findAll().size();
        pagador.setId(count.incrementAndGet());

        // Create the Pagador
        PagadorDTO pagadorDTO = pagadorMapper.toDto(pagador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPagador() throws Exception {
        int databaseSizeBeforeUpdate = pagadorRepository.findAll().size();
        pagador.setId(count.incrementAndGet());

        // Create the Pagador
        PagadorDTO pagadorDTO = pagadorMapper.toDto(pagador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagadorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagadorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePagadorWithPatch() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        int databaseSizeBeforeUpdate = pagadorRepository.findAll().size();

        // Update the pagador using partial update
        Pagador partialUpdatedPagador = new Pagador();
        partialUpdatedPagador.setId(pagador.getId());

        partialUpdatedPagador.nomePagador(UPDATED_NOME_PAGADOR).cpfCnpj(UPDATED_CPF_CNPJ);

        restPagadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPagador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPagador))
            )
            .andExpect(status().isOk());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeUpdate);
        Pagador testPagador = pagadorList.get(pagadorList.size() - 1);
        assertThat(testPagador.getNomePagador()).isEqualTo(UPDATED_NOME_PAGADOR);
        assertThat(testPagador.getCpfCnpj()).isEqualTo(UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void fullUpdatePagadorWithPatch() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        int databaseSizeBeforeUpdate = pagadorRepository.findAll().size();

        // Update the pagador using partial update
        Pagador partialUpdatedPagador = new Pagador();
        partialUpdatedPagador.setId(pagador.getId());

        partialUpdatedPagador.nomePagador(UPDATED_NOME_PAGADOR).cpfCnpj(UPDATED_CPF_CNPJ);

        restPagadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPagador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPagador))
            )
            .andExpect(status().isOk());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeUpdate);
        Pagador testPagador = pagadorList.get(pagadorList.size() - 1);
        assertThat(testPagador.getNomePagador()).isEqualTo(UPDATED_NOME_PAGADOR);
        assertThat(testPagador.getCpfCnpj()).isEqualTo(UPDATED_CPF_CNPJ);
    }

    @Test
    @Transactional
    void patchNonExistingPagador() throws Exception {
        int databaseSizeBeforeUpdate = pagadorRepository.findAll().size();
        pagador.setId(count.incrementAndGet());

        // Create the Pagador
        PagadorDTO pagadorDTO = pagadorMapper.toDto(pagador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pagadorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pagadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPagador() throws Exception {
        int databaseSizeBeforeUpdate = pagadorRepository.findAll().size();
        pagador.setId(count.incrementAndGet());

        // Create the Pagador
        PagadorDTO pagadorDTO = pagadorMapper.toDto(pagador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pagadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPagador() throws Exception {
        int databaseSizeBeforeUpdate = pagadorRepository.findAll().size();
        pagador.setId(count.incrementAndGet());

        // Create the Pagador
        PagadorDTO pagadorDTO = pagadorMapper.toDto(pagador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagadorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pagadorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pagador in the database
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePagador() throws Exception {
        // Initialize the database
        pagadorRepository.saveAndFlush(pagador);

        int databaseSizeBeforeDelete = pagadorRepository.findAll().size();

        // Delete the pagador
        restPagadorMockMvc
            .perform(delete(ENTITY_API_URL_ID, pagador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pagador> pagadorList = pagadorRepository.findAll();
        assertThat(pagadorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
