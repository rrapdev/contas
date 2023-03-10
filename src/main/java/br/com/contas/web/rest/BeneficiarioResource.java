package br.com.contas.web.rest;

import br.com.contas.repository.BeneficiarioRepository;
import br.com.contas.service.BeneficiarioQueryService;
import br.com.contas.service.BeneficiarioService;
import br.com.contas.service.criteria.BeneficiarioCriteria;
import br.com.contas.service.dto.BeneficiarioDTO;
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
 * REST controller for managing {@link br.com.contas.domain.Beneficiario}.
 */
@RestController
@RequestMapping("/api")
public class BeneficiarioResource {

    private final Logger log = LoggerFactory.getLogger(BeneficiarioResource.class);

    private static final String ENTITY_NAME = "beneficiario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeneficiarioService beneficiarioService;

    private final BeneficiarioRepository beneficiarioRepository;

    private final BeneficiarioQueryService beneficiarioQueryService;

    public BeneficiarioResource(
        BeneficiarioService beneficiarioService,
        BeneficiarioRepository beneficiarioRepository,
        BeneficiarioQueryService beneficiarioQueryService
    ) {
        this.beneficiarioService = beneficiarioService;
        this.beneficiarioRepository = beneficiarioRepository;
        this.beneficiarioQueryService = beneficiarioQueryService;
    }

    /**
     * {@code POST  /beneficiarios} : Create a new beneficiario.
     *
     * @param beneficiarioDTO the beneficiarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beneficiarioDTO, or with status {@code 400 (Bad Request)} if the beneficiario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beneficiarios")
    public ResponseEntity<BeneficiarioDTO> createBeneficiario(@RequestBody BeneficiarioDTO beneficiarioDTO) throws URISyntaxException {
        log.debug("REST request to save Beneficiario : {}", beneficiarioDTO);
        if (beneficiarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new beneficiario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeneficiarioDTO result = beneficiarioService.save(beneficiarioDTO);
        return ResponseEntity
            .created(new URI("/api/beneficiarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beneficiarios/:id} : Updates an existing beneficiario.
     *
     * @param id the id of the beneficiarioDTO to save.
     * @param beneficiarioDTO the beneficiarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiarioDTO,
     * or with status {@code 400 (Bad Request)} if the beneficiarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beneficiarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beneficiarios/{id}")
    public ResponseEntity<BeneficiarioDTO> updateBeneficiario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BeneficiarioDTO beneficiarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Beneficiario : {}, {}", id, beneficiarioDTO);
        if (beneficiarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BeneficiarioDTO result = beneficiarioService.update(beneficiarioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiarioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /beneficiarios/:id} : Partial updates given fields of an existing beneficiario, field will ignore if it is null
     *
     * @param id the id of the beneficiarioDTO to save.
     * @param beneficiarioDTO the beneficiarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiarioDTO,
     * or with status {@code 400 (Bad Request)} if the beneficiarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the beneficiarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the beneficiarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beneficiarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BeneficiarioDTO> partialUpdateBeneficiario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BeneficiarioDTO beneficiarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Beneficiario partially : {}, {}", id, beneficiarioDTO);
        if (beneficiarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BeneficiarioDTO> result = beneficiarioService.partialUpdate(beneficiarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /beneficiarios} : get all the beneficiarios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beneficiarios in body.
     */
    @GetMapping("/beneficiarios")
    public ResponseEntity<List<BeneficiarioDTO>> getAllBeneficiarios(
        BeneficiarioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Beneficiarios by criteria: {}", criteria);
        Page<BeneficiarioDTO> page = beneficiarioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beneficiarios/count} : count all the beneficiarios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/beneficiarios/count")
    public ResponseEntity<Long> countBeneficiarios(BeneficiarioCriteria criteria) {
        log.debug("REST request to count Beneficiarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(beneficiarioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /beneficiarios/:id} : get the "id" beneficiario.
     *
     * @param id the id of the beneficiarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beneficiarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beneficiarios/{id}")
    public ResponseEntity<BeneficiarioDTO> getBeneficiario(@PathVariable Long id) {
        log.debug("REST request to get Beneficiario : {}", id);
        Optional<BeneficiarioDTO> beneficiarioDTO = beneficiarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beneficiarioDTO);
    }

    /**
     * {@code DELETE  /beneficiarios/:id} : delete the "id" beneficiario.
     *
     * @param id the id of the beneficiarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beneficiarios/{id}")
    public ResponseEntity<Void> deleteBeneficiario(@PathVariable Long id) {
        log.debug("REST request to delete Beneficiario : {}", id);
        beneficiarioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
