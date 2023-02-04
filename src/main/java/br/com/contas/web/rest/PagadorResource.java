package br.com.contas.web.rest;

import br.com.contas.repository.PagadorRepository;
import br.com.contas.service.PagadorQueryService;
import br.com.contas.service.PagadorService;
import br.com.contas.service.criteria.PagadorCriteria;
import br.com.contas.service.dto.PagadorDTO;
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
 * REST controller for managing {@link br.com.contas.domain.Pagador}.
 */
@RestController
@RequestMapping("/api")
public class PagadorResource {

    private final Logger log = LoggerFactory.getLogger(PagadorResource.class);

    private static final String ENTITY_NAME = "pagador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PagadorService pagadorService;

    private final PagadorRepository pagadorRepository;

    private final PagadorQueryService pagadorQueryService;

    public PagadorResource(PagadorService pagadorService, PagadorRepository pagadorRepository, PagadorQueryService pagadorQueryService) {
        this.pagadorService = pagadorService;
        this.pagadorRepository = pagadorRepository;
        this.pagadorQueryService = pagadorQueryService;
    }

    /**
     * {@code POST  /pagadors} : Create a new pagador.
     *
     * @param pagadorDTO the pagadorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pagadorDTO, or with status {@code 400 (Bad Request)} if the pagador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pagadors")
    public ResponseEntity<PagadorDTO> createPagador(@RequestBody PagadorDTO pagadorDTO) throws URISyntaxException {
        log.debug("REST request to save Pagador : {}", pagadorDTO);
        if (pagadorDTO.getId() != null) {
            throw new BadRequestAlertException("A new pagador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PagadorDTO result = pagadorService.save(pagadorDTO);
        return ResponseEntity
            .created(new URI("/api/pagadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pagadors/:id} : Updates an existing pagador.
     *
     * @param id the id of the pagadorDTO to save.
     * @param pagadorDTO the pagadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagadorDTO,
     * or with status {@code 400 (Bad Request)} if the pagadorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pagadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pagadors/{id}")
    public ResponseEntity<PagadorDTO> updatePagador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PagadorDTO pagadorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pagador : {}, {}", id, pagadorDTO);
        if (pagadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pagadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PagadorDTO result = pagadorService.update(pagadorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pagadorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pagadors/:id} : Partial updates given fields of an existing pagador, field will ignore if it is null
     *
     * @param id the id of the pagadorDTO to save.
     * @param pagadorDTO the pagadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagadorDTO,
     * or with status {@code 400 (Bad Request)} if the pagadorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pagadorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pagadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pagadors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PagadorDTO> partialUpdatePagador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PagadorDTO pagadorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pagador partially : {}, {}", id, pagadorDTO);
        if (pagadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pagadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PagadorDTO> result = pagadorService.partialUpdate(pagadorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pagadorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pagadors} : get all the pagadors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pagadors in body.
     */
    @GetMapping("/pagadors")
    public ResponseEntity<List<PagadorDTO>> getAllPagadors(
        PagadorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Pagadors by criteria: {}", criteria);
        Page<PagadorDTO> page = pagadorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pagadors/count} : count all the pagadors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pagadors/count")
    public ResponseEntity<Long> countPagadors(PagadorCriteria criteria) {
        log.debug("REST request to count Pagadors by criteria: {}", criteria);
        return ResponseEntity.ok().body(pagadorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pagadors/:id} : get the "id" pagador.
     *
     * @param id the id of the pagadorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pagadorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pagadors/{id}")
    public ResponseEntity<PagadorDTO> getPagador(@PathVariable Long id) {
        log.debug("REST request to get Pagador : {}", id);
        Optional<PagadorDTO> pagadorDTO = pagadorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pagadorDTO);
    }

    /**
     * {@code DELETE  /pagadors/:id} : delete the "id" pagador.
     *
     * @param id the id of the pagadorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pagadors/{id}")
    public ResponseEntity<Void> deletePagador(@PathVariable Long id) {
        log.debug("REST request to delete Pagador : {}", id);
        pagadorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
