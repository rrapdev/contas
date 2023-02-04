package br.com.contas.web.rest;

import br.com.contas.repository.CategoriaPagamentoRepository;
import br.com.contas.service.CategoriaPagamentoQueryService;
import br.com.contas.service.CategoriaPagamentoService;
import br.com.contas.service.criteria.CategoriaPagamentoCriteria;
import br.com.contas.service.dto.CategoriaPagamentoDTO;
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
 * REST controller for managing {@link br.com.contas.domain.CategoriaPagamento}.
 */
@RestController
@RequestMapping("/api")
public class CategoriaPagamentoResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaPagamentoResource.class);

    private static final String ENTITY_NAME = "categoriaPagamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriaPagamentoService categoriaPagamentoService;

    private final CategoriaPagamentoRepository categoriaPagamentoRepository;

    private final CategoriaPagamentoQueryService categoriaPagamentoQueryService;

    public CategoriaPagamentoResource(
        CategoriaPagamentoService categoriaPagamentoService,
        CategoriaPagamentoRepository categoriaPagamentoRepository,
        CategoriaPagamentoQueryService categoriaPagamentoQueryService
    ) {
        this.categoriaPagamentoService = categoriaPagamentoService;
        this.categoriaPagamentoRepository = categoriaPagamentoRepository;
        this.categoriaPagamentoQueryService = categoriaPagamentoQueryService;
    }

    /**
     * {@code POST  /categoria-pagamentos} : Create a new categoriaPagamento.
     *
     * @param categoriaPagamentoDTO the categoriaPagamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoriaPagamentoDTO, or with status {@code 400 (Bad Request)} if the categoriaPagamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categoria-pagamentos")
    public ResponseEntity<CategoriaPagamentoDTO> createCategoriaPagamento(@RequestBody CategoriaPagamentoDTO categoriaPagamentoDTO)
        throws URISyntaxException {
        log.debug("REST request to save CategoriaPagamento : {}", categoriaPagamentoDTO);
        if (categoriaPagamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoriaPagamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoriaPagamentoDTO result = categoriaPagamentoService.save(categoriaPagamentoDTO);
        return ResponseEntity
            .created(new URI("/api/categoria-pagamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categoria-pagamentos/:id} : Updates an existing categoriaPagamento.
     *
     * @param id the id of the categoriaPagamentoDTO to save.
     * @param categoriaPagamentoDTO the categoriaPagamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaPagamentoDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaPagamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoriaPagamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categoria-pagamentos/{id}")
    public ResponseEntity<CategoriaPagamentoDTO> updateCategoriaPagamento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaPagamentoDTO categoriaPagamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CategoriaPagamento : {}, {}", id, categoriaPagamentoDTO);
        if (categoriaPagamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaPagamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaPagamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategoriaPagamentoDTO result = categoriaPagamentoService.update(categoriaPagamentoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaPagamentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categoria-pagamentos/:id} : Partial updates given fields of an existing categoriaPagamento, field will ignore if it is null
     *
     * @param id the id of the categoriaPagamentoDTO to save.
     * @param categoriaPagamentoDTO the categoriaPagamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoriaPagamentoDTO,
     * or with status {@code 400 (Bad Request)} if the categoriaPagamentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoriaPagamentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoriaPagamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categoria-pagamentos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoriaPagamentoDTO> partialUpdateCategoriaPagamento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategoriaPagamentoDTO categoriaPagamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategoriaPagamento partially : {}, {}", id, categoriaPagamentoDTO);
        if (categoriaPagamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoriaPagamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriaPagamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoriaPagamentoDTO> result = categoriaPagamentoService.partialUpdate(categoriaPagamentoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoriaPagamentoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /categoria-pagamentos} : get all the categoriaPagamentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoriaPagamentos in body.
     */
    @GetMapping("/categoria-pagamentos")
    public ResponseEntity<List<CategoriaPagamentoDTO>> getAllCategoriaPagamentos(
        CategoriaPagamentoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CategoriaPagamentos by criteria: {}", criteria);
        Page<CategoriaPagamentoDTO> page = categoriaPagamentoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /categoria-pagamentos/count} : count all the categoriaPagamentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/categoria-pagamentos/count")
    public ResponseEntity<Long> countCategoriaPagamentos(CategoriaPagamentoCriteria criteria) {
        log.debug("REST request to count CategoriaPagamentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoriaPagamentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /categoria-pagamentos/:id} : get the "id" categoriaPagamento.
     *
     * @param id the id of the categoriaPagamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoriaPagamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categoria-pagamentos/{id}")
    public ResponseEntity<CategoriaPagamentoDTO> getCategoriaPagamento(@PathVariable Long id) {
        log.debug("REST request to get CategoriaPagamento : {}", id);
        Optional<CategoriaPagamentoDTO> categoriaPagamentoDTO = categoriaPagamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoriaPagamentoDTO);
    }

    /**
     * {@code DELETE  /categoria-pagamentos/:id} : delete the "id" categoriaPagamento.
     *
     * @param id the id of the categoriaPagamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categoria-pagamentos/{id}")
    public ResponseEntity<Void> deleteCategoriaPagamento(@PathVariable Long id) {
        log.debug("REST request to delete CategoriaPagamento : {}", id);
        categoriaPagamentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
