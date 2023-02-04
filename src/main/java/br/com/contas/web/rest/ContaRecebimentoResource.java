package br.com.contas.web.rest;

import br.com.contas.repository.ContaRecebimentoRepository;
import br.com.contas.service.ContaRecebimentoQueryService;
import br.com.contas.service.ContaRecebimentoService;
import br.com.contas.service.criteria.ContaRecebimentoCriteria;
import br.com.contas.service.dto.ContaRecebimentoDTO;
import br.com.contas.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link br.com.contas.domain.ContaRecebimento}.
 */
@RestController
@RequestMapping("/api")
public class ContaRecebimentoResource {

    private final Logger log = LoggerFactory.getLogger(ContaRecebimentoResource.class);

    private static final String ENTITY_NAME = "contaRecebimento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContaRecebimentoService contaRecebimentoService;

    private final ContaRecebimentoRepository contaRecebimentoRepository;

    private final ContaRecebimentoQueryService contaRecebimentoQueryService;

    public ContaRecebimentoResource(
        ContaRecebimentoService contaRecebimentoService,
        ContaRecebimentoRepository contaRecebimentoRepository,
        ContaRecebimentoQueryService contaRecebimentoQueryService
    ) {
        this.contaRecebimentoService = contaRecebimentoService;
        this.contaRecebimentoRepository = contaRecebimentoRepository;
        this.contaRecebimentoQueryService = contaRecebimentoQueryService;
    }

    /**
     * {@code POST  /conta-recebimentos} : Create a new contaRecebimento.
     *
     * @param contaRecebimentoDTO the contaRecebimentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contaRecebimentoDTO, or with status {@code 400 (Bad Request)} if the contaRecebimento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conta-recebimentos")
    public ResponseEntity<ContaRecebimentoDTO> createContaRecebimento(@Valid @RequestBody ContaRecebimentoDTO contaRecebimentoDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContaRecebimento : {}", contaRecebimentoDTO);
        if (contaRecebimentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new contaRecebimento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContaRecebimentoDTO result = contaRecebimentoService.save(contaRecebimentoDTO);
        return ResponseEntity
            .created(new URI("/api/conta-recebimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conta-recebimentos/:id} : Updates an existing contaRecebimento.
     *
     * @param id the id of the contaRecebimentoDTO to save.
     * @param contaRecebimentoDTO the contaRecebimentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaRecebimentoDTO,
     * or with status {@code 400 (Bad Request)} if the contaRecebimentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contaRecebimentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conta-recebimentos/{id}")
    public ResponseEntity<ContaRecebimentoDTO> updateContaRecebimento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContaRecebimentoDTO contaRecebimentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContaRecebimento : {}, {}", id, contaRecebimentoDTO);
        if (contaRecebimentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaRecebimentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaRecebimentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContaRecebimentoDTO result = contaRecebimentoService.update(contaRecebimentoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contaRecebimentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conta-recebimentos/:id} : Partial updates given fields of an existing contaRecebimento, field will ignore if it is null
     *
     * @param id the id of the contaRecebimentoDTO to save.
     * @param contaRecebimentoDTO the contaRecebimentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaRecebimentoDTO,
     * or with status {@code 400 (Bad Request)} if the contaRecebimentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contaRecebimentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contaRecebimentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conta-recebimentos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContaRecebimentoDTO> partialUpdateContaRecebimento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContaRecebimentoDTO contaRecebimentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContaRecebimento partially : {}, {}", id, contaRecebimentoDTO);
        if (contaRecebimentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaRecebimentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaRecebimentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContaRecebimentoDTO> result = contaRecebimentoService.partialUpdate(contaRecebimentoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contaRecebimentoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conta-recebimentos} : get all the contaRecebimentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contaRecebimentos in body.
     */
    @GetMapping("/conta-recebimentos")
    public ResponseEntity<List<ContaRecebimentoDTO>> getAllContaRecebimentos(
        ContaRecebimentoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ContaRecebimentos by criteria: {}", criteria);
        Page<ContaRecebimentoDTO> page = contaRecebimentoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conta-recebimentos/count} : count all the contaRecebimentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/conta-recebimentos/count")
    public ResponseEntity<Long> countContaRecebimentos(ContaRecebimentoCriteria criteria) {
        log.debug("REST request to count ContaRecebimentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(contaRecebimentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /conta-recebimentos/:id} : get the "id" contaRecebimento.
     *
     * @param id the id of the contaRecebimentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contaRecebimentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conta-recebimentos/{id}")
    public ResponseEntity<ContaRecebimentoDTO> getContaRecebimento(@PathVariable Long id) {
        log.debug("REST request to get ContaRecebimento : {}", id);
        Optional<ContaRecebimentoDTO> contaRecebimentoDTO = contaRecebimentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contaRecebimentoDTO);
    }

    /**
     * {@code DELETE  /conta-recebimentos/:id} : delete the "id" contaRecebimento.
     *
     * @param id the id of the contaRecebimentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conta-recebimentos/{id}")
    public ResponseEntity<Void> deleteContaRecebimento(@PathVariable Long id) {
        log.debug("REST request to delete ContaRecebimento : {}", id);
        contaRecebimentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
