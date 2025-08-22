package com.bna.web.rest;

import com.bna.repository.VirementRepository;
import com.bna.service.VirementService;
import com.bna.service.dto.VirementDTO;
import com.bna.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bna.domain.Virement}.
 */
@RestController
@RequestMapping("/api/virements")
public class VirementResource {

    private static final Logger LOG = LoggerFactory.getLogger(VirementResource.class);

    private static final String ENTITY_NAME = "virement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VirementService virementService;

    private final VirementRepository virementRepository;

    public VirementResource(VirementService virementService, VirementRepository virementRepository) {
        this.virementService = virementService;
        this.virementRepository = virementRepository;
    }

    /**
     * {@code POST  /virements} : Create a new virement.
     *
     * @param virementDTO the virementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new virementDTO, or with status {@code 400 (Bad Request)} if the virement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VirementDTO> createVirement(@Valid @RequestBody VirementDTO virementDTO) throws URISyntaxException {
        LOG.debug("REST request to save Virement : {}", virementDTO);
        if (virementDTO.getId() != null) {
            throw new BadRequestAlertException("A new virement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        virementDTO = virementService.save(virementDTO);
        return ResponseEntity.created(new URI("/api/virements/" + virementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, virementDTO.getId().toString()))
            .body(virementDTO);
    }

    /**
     * {@code PUT  /virements/:id} : Updates an existing virement.
     *
     * @param id the id of the virementDTO to save.
     * @param virementDTO the virementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virementDTO,
     * or with status {@code 400 (Bad Request)} if the virementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the virementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VirementDTO> updateVirement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VirementDTO virementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Virement : {}, {}", id, virementDTO);
        if (virementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        virementDTO = virementService.update(virementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, virementDTO.getId().toString()))
            .body(virementDTO);
    }

    /**
     * {@code PATCH  /virements/:id} : Partial updates given fields of an existing virement, field will ignore if it is null
     *
     * @param id the id of the virementDTO to save.
     * @param virementDTO the virementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated virementDTO,
     * or with status {@code 400 (Bad Request)} if the virementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the virementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the virementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VirementDTO> partialUpdateVirement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VirementDTO virementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Virement partially : {}, {}", id, virementDTO);
        if (virementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, virementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!virementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VirementDTO> result = virementService.partialUpdate(virementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, virementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /virements} : get all the virements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of virements in body.
     */
    @GetMapping("")
    public List<VirementDTO> getAllVirements() {
        LOG.debug("REST request to get all Virements");
        return virementService.findAll();
    }

    /**
     * {@code GET  /virements/:id} : get the "id" virement.
     *
     * @param id the id of the virementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the virementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VirementDTO> getVirement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Virement : {}", id);
        Optional<VirementDTO> virementDTO = virementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(virementDTO);
    }

    /**
     * {@code DELETE  /virements/:id} : delete the "id" virement.
     *
     * @param id the id of the virementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVirement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Virement : {}", id);
        virementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    /**
     * {@code GET  /operations/current-user} : get all operations for the current logged-in user.
     */
    @GetMapping("/current-user")
    public List<VirementDTO> getAllOperationsForCurrentUser() {
        LOG.debug("REST request to get all virements for current user");
        return virementService.findAllByCurrentUser();
    }
}