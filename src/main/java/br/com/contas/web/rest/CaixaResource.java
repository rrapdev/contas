package br.com.contas.web.rest;

import br.com.contas.repository.CaixaRepository;
import br.com.contas.service.CaixaQueryService;
import br.com.contas.service.CaixaService;
import br.com.contas.service.criteria.CaixaCriteria;
import br.com.contas.service.dto.CaixaDTO;
import br.com.contas.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.contas.domain.Caixa}.
 */
@RestController
@RequestMapping("/api")
public class CaixaResource {

    private final Logger log = LoggerFactory.getLogger(CaixaResource.class);

    private static final String ENTITY_NAME = "caixa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaixaService caixaService;

    private final CaixaRepository caixaRepository;

    private final CaixaQueryService caixaQueryService;

    public CaixaResource(CaixaService caixaService, CaixaRepository caixaRepository, CaixaQueryService caixaQueryService) {
        this.caixaService = caixaService;
        this.caixaRepository = caixaRepository;
        this.caixaQueryService = caixaQueryService;
    }

    /**
     * {@code POST  /caixas} : Create a new caixa.
     *
     * @param caixaDTO the caixaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caixaDTO, or with status {@code 400 (Bad Request)} if the caixa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caixas")
    public ResponseEntity<CaixaDTO> createCaixa(@RequestBody CaixaDTO caixaDTO) throws URISyntaxException {
        log.debug("REST request to save Caixa : {}", caixaDTO);
        if (caixaDTO.getId() != null) {
            throw new BadRequestAlertException("A new caixa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CaixaDTO result = caixaService.save(caixaDTO);
        return ResponseEntity
            .created(new URI("/api/caixas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /caixas/:id} : Updates an existing caixa.
     *
     * @param id the id of the caixaDTO to save.
     * @param caixaDTO the caixaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caixaDTO,
     * or with status {@code 400 (Bad Request)} if the caixaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caixaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caixas/{id}")
    public ResponseEntity<CaixaDTO> updateCaixa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CaixaDTO caixaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Caixa : {}, {}", id, caixaDTO);
        if (caixaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caixaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caixaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CaixaDTO result = caixaService.update(caixaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caixaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /caixas/:id} : Partial updates given fields of an existing caixa, field will ignore if it is null
     *
     * @param id the id of the caixaDTO to save.
     * @param caixaDTO the caixaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caixaDTO,
     * or with status {@code 400 (Bad Request)} if the caixaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the caixaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the caixaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/caixas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CaixaDTO> partialUpdateCaixa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CaixaDTO caixaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Caixa partially : {}, {}", id, caixaDTO);
        if (caixaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caixaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caixaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CaixaDTO> result = caixaService.partialUpdate(caixaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caixaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /caixas} : get all the caixas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of caixas in body.
     */
    @GetMapping("/caixas")
    public ResponseEntity<List<CaixaDTO>> getAllCaixas(
        CaixaCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Caixas by criteria: {}", criteria);
        Page<CaixaDTO> page = caixaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /caixas/count} : count all the caixas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/caixas/count")
    public ResponseEntity<Long> countCaixas(CaixaCriteria criteria) {
        log.debug("REST request to count Caixas by criteria: {}", criteria);
        return ResponseEntity.ok().body(caixaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /caixas/:id} : get the "id" caixa.
     *
     * @param id the id of the caixaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caixaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caixas/{id}")
    public ResponseEntity<CaixaDTO> getCaixa(@PathVariable Long id) {
        log.debug("REST request to get Caixa : {}", id);
        Optional<CaixaDTO> caixaDTO = caixaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caixaDTO);
    }

    /**
     * {@code DELETE  /caixas/:id} : delete the "id" caixa.
     *
     * @param id the id of the caixaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caixas/{id}")
    public ResponseEntity<Void> deleteCaixa(@PathVariable Long id) {
        log.debug("REST request to delete Caixa : {}", id);
        caixaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
