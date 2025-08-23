package com.bna.web.rest;

import com.bna.domain.Mondat;
import com.bna.repository.MondatRepository;
import com.bna.service.JwtUtil;
import com.bna.service.MailService;
import com.bna.service.dto.MondatDTO;
import com.bna.service.MondatService;
import com.bna.service.dto.MondatDTO;
import com.bna.web.rest.errors.BadRequestAlertException;
import com.bna.web.service.MondatEnAtt;

// Java collections
import java.util.Map;
import java.util.HashMap;

// JWT classes
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final JwtUtil jwtUtil;
    private final MailService mailService;
    private final MondatEnAtt mondatEnAtt;

    public MondatResource(MondatService mondatService, MondatRepository mondatRepository, JwtUtil jwtUtil,
            MailService mailService, MondatEnAtt mondatEnAtt) {
        this.mondatService = mondatService;
        this.mondatRepository = mondatRepository;
        this.jwtUtil = jwtUtil;
        this.mailService = mailService;
        this.mondatEnAtt = mondatEnAtt;;
    }

    /**
     * {@code POST  /mondats} : Create a new mondat.
     *
     * @param mondatDTO the mondatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mondatDTO, or with status {@code 400 (Bad Request)} if the mondat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
        /**
     * Create a new Mondat and send confirmation email.
     */
    @PostMapping
    public ResponseEntity<String> createMondat(@RequestBody MondatDTO mondatDTO) {
    // Generate a token and store temporarily
    String token = mondatEnAtt.addPendingTransaction(mondatDTO);

    // Build confirmation link
    String confirmLink = "http://localhost:8080/api/mondats/confirm?token=" + token;

    // Send email
    mailService.sendEmail(
        "ebankingbnastage@gmail.com",
        "Confirmer la transaction",
        "Clicker ce lien pour confirmer la transaction: " + confirmLink,
        false,
        true
    );

    return ResponseEntity.ok("email de confirmation a ete envoyer, en attente de confirmation");
    }

    /** Confirm Mondat via JWT link */
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmMondat(@RequestParam("token") String token) {
        if (!mondatEnAtt.exists(token)) {
            return ResponseEntity.badRequest().body("Invalid or expired token ❌");
        }

    // Remove from pending and persist to DB
        MondatDTO mondatDTO = mondatEnAtt.confirmTransaction(token);
        mondatService.save(mondatDTO); // actually saves to DB

        return ResponseEntity.ok("Transaction confirmee ✅");
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
