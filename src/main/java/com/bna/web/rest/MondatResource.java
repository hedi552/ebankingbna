package com.bna.web.rest;

import com.bna.repository.MondatRepository;
import com.bna.service.MondatService;
import com.bna.service.dto.MondatDTO;
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
 * REST controller for managing {@link com.bna.domain.Mondat}.
 */
@RestController
@RequestMapping("/api/mondats")
public class MondatResource {

    private static final Logger LOG = LoggerFactory.getLogger(MondatResource.class);

    private static final String ENTITY_NAME = "mondat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MondatService mondatService;

    private final MondatRepository mondatRepository;

    public MondatResource(MondatService mondatService, MondatRepository mondatRepository) {
        this.mondatService = mondatService;
        this.mondatRepository = mondatRepository;
    }

    /**
     * {@code POST  /mondats} : Create a new mondat.
     *
     * @param mondatDTO the mondatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mondatDTO, or with status {@code 400 (Bad Request)} if the mondat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MondatDTO> createMondat(@Valid @RequestBody MondatDTO mondatDTO) throws URISyntaxException {
        LOG.debug("REST request to save Mondat : {}", mondatDTO);
        if (mondatDTO.getId() != null) {
            throw new BadRequestAlertException("A new mondat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mondatDTO = mondatService.save(mondatDTO);
        return ResponseEntity.created(new URI("/api/mondats/" + mondatDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mondatDTO.getId().toString()))
            .body(mondatDTO);
    }

    /**
     * {@code PUT  /mondats/:id} : Updates an existing mondat.
     *
     * @param id the id of the mondatDTO to save.
     * @param mondatDTO the mondatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mondatDTO,
     * or with status {@code 400 (Bad Request)} if the mondatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mondatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MondatDTO> updateMondat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MondatDTO mondatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Mondat : {}, {}", id, mondatDTO);
        if (mondatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mondatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mondatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mondatDTO = mondatService.update(mondatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mondatDTO.getId().toString()))
            .body(mondatDTO);
    }

    /**
     * {@code PATCH  /mondats/:id} : Partial updates given fields of an existing mondat, field will ignore if it is null
     *
     * @param id the id of the mondatDTO to save.
     * @param mondatDTO the mondatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mondatDTO,
     * or with status {@code 400 (Bad Request)} if the mondatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mondatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mondatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MondatDTO> partialUpdateMondat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MondatDTO mondatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Mondat partially : {}, {}", id, mondatDTO);
        if (mondatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mondatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mondatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MondatDTO> result = mondatService.partialUpdate(mondatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mondatDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mondats} : get all the mondats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mondats in body.
     */
    @GetMapping("")
    public List<MondatDTO> getAllMondats() {
        LOG.debug("REST request to get all Mondats");
        return mondatService.findAll();
    }

    /**
     * {@code GET  /mondats/:id} : get the "id" mondat.
     *
     * @param id the id of the mondatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mondatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MondatDTO> getMondat(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Mondat : {}", id);
        Optional<MondatDTO> mondatDTO = mondatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mondatDTO);
    }

    /**
     * {@code DELETE  /mondats/:id} : delete the "id" mondat.
     *
     * @param id the id of the mondatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMondat(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Mondat : {}", id);
        mondatService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    @GetMapping("/current-user")
    public List<MondatDTO> getAllMondatForCurrentUser() {
        LOG.debug("REST request to get all virements for current user");
        return mondatService.findAllByCurrentUser();
    }
}
