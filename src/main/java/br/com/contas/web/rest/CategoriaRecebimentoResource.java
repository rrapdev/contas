package br.com.contas.web.rest;

import br.com.contas.repository.CategoriaRecebimentoRepository;
import br.com.contas.service.CategoriaRecebimentoQueryService;
import br.com.contas.service.CategoriaRecebimentoService;
import br.com.contas.service.criteria.CategoriaRecebimentoCriteria;
import br.com.contas.service.dto.CategoriaRecebimentoDTO;
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
 * REST controller for managing {@link br.com.contas.domain.CategoriaRecebimento}.
 */
@RestController
@RequestMapping("/api")
public class CategoriaRecebimentoResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaRecebimentoResource.class);

    private static final String ENTITY_NAME = "categoriaRecebimento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriaRecebimentoService categoriaRecebimentoService;

    private final CategoriaRecebimentoRepository categoriaRecebimentoRepository;

    private final CategoriaRecebimentoQueryService categoriaRecebimentoQueryService;

    public CategoriaRecebimentoResource(
        CategoriaRecebimentoService categoriaRecebimentoService,
        CategoriaRecebimentoRepository categoriaRecebimentoRepository,
        CategoriaRecebimentoQueryService categoriaRecebimentoQueryService
    ) {
        this.categoriaRecebimentoService = categoriaRecebimentoService;
        this.categoriaRecebimentoRepository = categoriaRecebimentoRepository;
        this.categoriaRecebimentoQueryService = categoriaRecebimentoQueryService;
    }

    /**
     * {@code POST  /categoria-recebimentos} : Create a new categoriaRecebimento.
     *
     * @param categoriaRecebimentoDTO the categoriaRecebimentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoriaRecebimentoDTO, or with status {@code 400 (Bad Request)} if the categoriaRecebimento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categoria-recebimentos")
    public ResponseEntity<CategoriaRecebimentoDTO> createCategoriaRecebimento(@RequestBody CategoriaRecebimentoDTO categoriaRecebimentoDTO)
        throws URISyntaxException {
        log.debug("REST request to save CategoriaRecebimento : {}", categoriaRecebimentoDTO);
        if (categoriaRecebimentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoriaRecebimento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoriaRecebimentoDTO result = categoriaRecebimentoService.save(categoriaRecebimentoDTO);
        return ResponseEntity
            .created(new URI("/api/categoria-recebimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categoria-recebimentos/:id} : Updates an existing categoriaRecebimento.
     *
     * @param id the id of the categoriaRecebimentoDTO to save.
     * @param categoriaRecebimentoDTO the categoriaRecebimentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaRecebimentoDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaRecebimentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoriaRecebimentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categoria-recebimentos/{id}")
    public ResponseEntity<CategoriaRecebimentoDTO> updateCategoriaRecebimento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaRecebimentoDTO categoriaRecebimentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CategoriaRecebimento : {}, {}", id, categoriaRecebimentoDTO);
        if (categoriaRecebimentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaRecebimentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaRecebimentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategoriaRecebimentoDTO result = categoriaRecebimentoService.update(categoriaRecebimentoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaRecebimentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categoria-recebimentos/:id} : Partial updates given fields of an existing categoriaRecebimento, field will ignore if it is null
     *
     * @param id the id of the categoriaRecebimentoDTO to save.
     * @param categoriaRecebimentoDTO the categoriaRecebimentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaRecebimentoDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaRecebimentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoriaRecebimentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoriaRecebimentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categoria-recebimentos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoriaRecebimentoDTO> partialUpdateCategoriaRecebimento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaRecebimentoDTO categoriaRecebimentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategoriaRecebimento partially : {}, {}", id, categoriaRecebimentoDTO);
        if (categoriaRecebimentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaRecebimentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaRecebimentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoriaRecebimentoDTO> result = categoriaRecebimentoService.partialUpdate(categoriaRecebimentoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaRecebimentoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /categoria-recebimentos} : get all the categoriaRecebimentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoriaRecebimentos in body.
     */
    @GetMapping("/categoria-recebimentos")
    public ResponseEntity<List<CategoriaRecebimentoDTO>> getAllCategoriaRecebimentos(
        CategoriaRecebimentoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CategoriaRecebimentos by criteria: {}", criteria);
        Page<CategoriaRecebimentoDTO> page = categoriaRecebimentoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /categoria-recebimentos/count} : count all the categoriaRecebimentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/categoria-recebimentos/count")
    public ResponseEntity<Long> countCategoriaRecebimentos(CategoriaRecebimentoCriteria criteria) {
        log.debug("REST request to count CategoriaRecebimentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoriaRecebimentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /categoria-recebimentos/:id} : get the "id" categoriaRecebimento.
     *
     * @param id the id of the categoriaRecebimentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoriaRecebimentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categoria-recebimentos/{id}")
    public ResponseEntity<CategoriaRecebimentoDTO> getCategoriaRecebimento(@PathVariable Long id) {
        log.debug("REST request to get CategoriaRecebimento : {}", id);
        Optional<CategoriaRecebimentoDTO> categoriaRecebimentoDTO = categoriaRecebimentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoriaRecebimentoDTO);
    }

    /**
     * {@code DELETE  /categoria-recebimentos/:id} : delete the "id" categoriaRecebimento.
     *
     * @param id the id of the categoriaRecebimentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categoria-recebimentos/{id}")
    public ResponseEntity<Void> deleteCategoriaRecebimento(@PathVariable Long id) {
        log.debug("REST request to delete CategoriaRecebimento : {}", id);
        categoriaRecebimentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
