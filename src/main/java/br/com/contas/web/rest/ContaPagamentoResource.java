package br.com.contas.web.rest;

import br.com.contas.repository.ContaPagamentoRepository;
import br.com.contas.service.ContaPagamentoQueryService;
import br.com.contas.service.ContaPagamentoService;
import br.com.contas.service.criteria.ContaPagamentoCriteria;
import br.com.contas.service.dto.ContaPagamentoDTO;
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
 * REST controller for managing {@link br.com.contas.domain.ContaPagamento}.
 */
@RestController
@RequestMapping("/api")
public class ContaPagamentoResource {

    private final Logger log = LoggerFactory.getLogger(ContaPagamentoResource.class);

    private static final String ENTITY_NAME = "contaPagamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContaPagamentoService contaPagamentoService;

    private final ContaPagamentoRepository contaPagamentoRepository;

    private final ContaPagamentoQueryService contaPagamentoQueryService;

    public ContaPagamentoResource(
        ContaPagamentoService contaPagamentoService,
        ContaPagamentoRepository contaPagamentoRepository,
        ContaPagamentoQueryService contaPagamentoQueryService
    ) {
        this.contaPagamentoService = contaPagamentoService;
        this.contaPagamentoRepository = contaPagamentoRepository;
        this.contaPagamentoQueryService = contaPagamentoQueryService;
    }

    /**
     * {@code POST  /conta-pagamentos} : Create a new contaPagamento.
     *
     * @param contaPagamentoDTO the contaPagamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contaPagamentoDTO, or with status {@code 400 (Bad Request)} if the contaPagamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conta-pagamentos")
    public ResponseEntity<ContaPagamentoDTO> createContaPagamento(@Valid @RequestBody ContaPagamentoDTO contaPagamentoDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContaPagamento : {}", contaPagamentoDTO);
        if (contaPagamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new contaPagamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContaPagamentoDTO result = contaPagamentoService.save(contaPagamentoDTO);
        return ResponseEntity
            .created(new URI("/api/conta-pagamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conta-pagamentos/:id} : Updates an existing contaPagamento.
     *
     * @param id the id of the contaPagamentoDTO to save.
     * @param contaPagamentoDTO the contaPagamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaPagamentoDTO,
     * or with status {@code 400 (Bad Request)} if the contaPagamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contaPagamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conta-pagamentos/{id}")
    public ResponseEntity<ContaPagamentoDTO> updateContaPagamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContaPagamentoDTO contaPagamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContaPagamento : {}, {}", id, contaPagamentoDTO);
        if (contaPagamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaPagamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaPagamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContaPagamentoDTO result = contaPagamentoService.update(contaPagamentoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contaPagamentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conta-pagamentos/:id} : Partial updates given fields of an existing contaPagamento, field will ignore if it is null
     *
     * @param id the id of the contaPagamentoDTO to save.
     * @param contaPagamentoDTO the contaPagamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaPagamentoDTO,
     * or with status {@code 400 (Bad Request)} if the contaPagamentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contaPagamentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contaPagamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conta-pagamentos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContaPagamentoDTO> partialUpdateContaPagamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContaPagamentoDTO contaPagamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContaPagamento partially : {}, {}", id, contaPagamentoDTO);
        if (contaPagamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaPagamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaPagamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContaPagamentoDTO> result = contaPagamentoService.partialUpdate(contaPagamentoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contaPagamentoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conta-pagamentos} : get all the contaPagamentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contaPagamentos in body.
     */
    @GetMapping("/conta-pagamentos")
    public ResponseEntity<List<ContaPagamentoDTO>> getAllContaPagamentos(
        ContaPagamentoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ContaPagamentos by criteria: {}", criteria);
        Page<ContaPagamentoDTO> page = contaPagamentoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conta-pagamentos/count} : count all the contaPagamentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/conta-pagamentos/count")
    public ResponseEntity<Long> countContaPagamentos(ContaPagamentoCriteria criteria) {
        log.debug("REST request to count ContaPagamentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(contaPagamentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /conta-pagamentos/:id} : get the "id" contaPagamento.
     *
     * @param id the id of the contaPagamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contaPagamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conta-pagamentos/{id}")
    public ResponseEntity<ContaPagamentoDTO> getContaPagamento(@PathVariable Long id) {
        log.debug("REST request to get ContaPagamento : {}", id);
        Optional<ContaPagamentoDTO> contaPagamentoDTO = contaPagamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contaPagamentoDTO);
    }

    /**
     * {@code DELETE  /conta-pagamentos/:id} : delete the "id" contaPagamento.
     *
     * @param id the id of the contaPagamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conta-pagamentos/{id}")
    public ResponseEntity<Void> deleteContaPagamento(@PathVariable Long id) {
        log.debug("REST request to delete ContaPagamento : {}", id);
        contaPagamentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
