package com.bna.web.rest;

import static com.bna.domain.CompteAsserts.*;
import static com.bna.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bna.IntegrationTest;
import com.bna.domain.Compte;
import com.bna.repository.CompteRepository;
import com.bna.repository.UserRepository;
import com.bna.service.dto.CompteDTO;
import com.bna.service.mapper.CompteMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompteResourceIT {

    private static final String DEFAULT_NUMCOMPTE = "AAAAAAAAAA";
    private static final String UPDATED_NUMCOMPTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGENCE = 1;
    private static final Integer UPDATED_AGENCE = 2;

    private static final String ENTITY_API_URL = "/api/comptes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompteMapper compteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteMockMvc;

    private Compte compte;

    private Compte insertedCompte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compte createEntity() {
        return new Compte().numcompte(DEFAULT_NUMCOMPTE).agence(DEFAULT_AGENCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compte createUpdatedEntity() {
        return new Compte().numcompte(UPDATED_NUMCOMPTE).agence(UPDATED_AGENCE);
    }

    @BeforeEach
    void initTest() {
        compte = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompte != null) {
            compteRepository.delete(insertedCompte);
            insertedCompte = null;
        }
    }

    @Test
    @Transactional
    void createCompte() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);
        var returnedCompteDTO = om.readValue(
            restCompteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompteDTO.class
        );

        // Validate the Compte in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompte = compteMapper.toEntity(returnedCompteDTO);
        assertCompteUpdatableFieldsEquals(returnedCompte, getPersistedCompte(returnedCompte));

        insertedCompte = returnedCompte;
    }

    @Test
    @Transactional
    void createCompteWithExistingId() throws Exception {
        // Create the Compte with an existing ID
        compte.setId(1L);
        CompteDTO compteDTO = compteMapper.toDto(compte);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumcompteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compte.setNumcompte(null);

        // Create the Compte, which fails.
        CompteDTO compteDTO = compteMapper.toDto(compte);

        restCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgenceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compte.setAgence(null);

        // Create the Compte, which fails.
        CompteDTO compteDTO = compteMapper.toDto(compte);

        restCompteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComptes() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        // Get all the compteList
        restCompteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compte.getId().intValue())))
            .andExpect(jsonPath("$.[*].numcompte").value(hasItem(DEFAULT_NUMCOMPTE)))
            .andExpect(jsonPath("$.[*].agence").value(hasItem(DEFAULT_AGENCE)));
    }

    @Test
    @Transactional
    void getCompte() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        // Get the compte
        restCompteMockMvc
            .perform(get(ENTITY_API_URL_ID, compte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compte.getId().intValue()))
            .andExpect(jsonPath("$.numcompte").value(DEFAULT_NUMCOMPTE))
            .andExpect(jsonPath("$.agence").value(DEFAULT_AGENCE));
    }

    @Test
    @Transactional
    void getNonExistingCompte() throws Exception {
        // Get the compte
        restCompteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompte() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compte
        Compte updatedCompte = compteRepository.findById(compte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompte are not directly saved in db
        em.detach(updatedCompte);
        updatedCompte.numcompte(UPDATED_NUMCOMPTE).agence(UPDATED_AGENCE);
        CompteDTO compteDTO = compteMapper.toDto(updatedCompte);

        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompteToMatchAllProperties(updatedCompte);
    }

    @Test
    @Transactional
    void putNonExistingCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteWithPatch() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compte using partial update
        Compte partialUpdatedCompte = new Compte();
        partialUpdatedCompte.setId(compte.getId());

        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompte))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompte, compte), getPersistedCompte(compte));
    }

    @Test
    @Transactional
    void fullUpdateCompteWithPatch() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compte using partial update
        Compte partialUpdatedCompte = new Compte();
        partialUpdatedCompte.setId(compte.getId());

        partialUpdatedCompte.numcompte(UPDATED_NUMCOMPTE).agence(UPDATED_AGENCE);

        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompte))
            )
            .andExpect(status().isOk());

        // Validate the Compte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompteUpdatableFieldsEquals(partialUpdatedCompte, getPersistedCompte(partialUpdatedCompte));
    }

    @Test
    @Transactional
    void patchNonExistingCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compte.setId(longCount.incrementAndGet());

        // Create the Compte
        CompteDTO compteDTO = compteMapper.toDto(compte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompte() throws Exception {
        // Initialize the database
        insertedCompte = compteRepository.saveAndFlush(compte);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compte
        restCompteMockMvc
            .perform(delete(ENTITY_API_URL_ID, compte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compteRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Compte getPersistedCompte(Compte compte) {
        return compteRepository.findById(compte.getId()).orElseThrow();
    }

    protected void assertPersistedCompteToMatchAllProperties(Compte expectedCompte) {
        assertCompteAllPropertiesEquals(expectedCompte, getPersistedCompte(expectedCompte));
    }

    protected void assertPersistedCompteToMatchUpdatableProperties(Compte expectedCompte) {
        assertCompteAllUpdatablePropertiesEquals(expectedCompte, getPersistedCompte(expectedCompte));
    }
}
