package com.bna.web.rest;

import static com.bna.domain.VirementAsserts.*;
import static com.bna.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bna.IntegrationTest;
import com.bna.domain.Virement;
import com.bna.repository.VirementRepository;
import com.bna.service.dto.VirementDTO;
import com.bna.service.mapper.VirementMapper;
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
 * Integration tests for the {@link VirementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VirementResourceIT {

    private static final String DEFAULT_COMPTE_BENEFICIAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMPTE_BENEFICIAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/virements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VirementRepository virementRepository;

    @Autowired
    private VirementMapper virementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVirementMockMvc;

    private Virement virement;

    private Virement insertedVirement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Virement createEntity() {
        return new Virement().compteBeneficiaire(DEFAULT_COMPTE_BENEFICIAIRE).motif(DEFAULT_MOTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Virement createUpdatedEntity() {
        return new Virement().compteBeneficiaire(UPDATED_COMPTE_BENEFICIAIRE).motif(UPDATED_MOTIF);
    }

    @BeforeEach
    void initTest() {
        virement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVirement != null) {
            virementRepository.delete(insertedVirement);
            insertedVirement = null;
        }
    }

    @Test
    @Transactional
    void createVirement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);
        var returnedVirementDTO = om.readValue(
            restVirementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(virementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VirementDTO.class
        );

        // Validate the Virement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVirement = virementMapper.toEntity(returnedVirementDTO);
        assertVirementUpdatableFieldsEquals(returnedVirement, getPersistedVirement(returnedVirement));

        insertedVirement = returnedVirement;
    }

    @Test
    @Transactional
    void createVirementWithExistingId() throws Exception {
        // Create the Virement with an existing ID
        virement.setId(1L);
        VirementDTO virementDTO = virementMapper.toDto(virement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(virementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompteBeneficiaireIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        virement.setCompteBeneficiaire(null);

        // Create the Virement, which fails.
        VirementDTO virementDTO = virementMapper.toDto(virement);

        restVirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(virementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        virement.setMotif(null);

        // Create the Virement, which fails.
        VirementDTO virementDTO = virementMapper.toDto(virement);

        restVirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(virementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVirements() throws Exception {
        // Initialize the database
        insertedVirement = virementRepository.saveAndFlush(virement);

        // Get all the virementList
        restVirementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(virement.getId().intValue())))
            .andExpect(jsonPath("$.[*].compteBeneficiaire").value(hasItem(DEFAULT_COMPTE_BENEFICIAIRE)))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)));
    }

    @Test
    @Transactional
    void getVirement() throws Exception {
        // Initialize the database
        insertedVirement = virementRepository.saveAndFlush(virement);

        // Get the virement
        restVirementMockMvc
            .perform(get(ENTITY_API_URL_ID, virement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(virement.getId().intValue()))
            .andExpect(jsonPath("$.compteBeneficiaire").value(DEFAULT_COMPTE_BENEFICIAIRE))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF));
    }

    @Test
    @Transactional
    void getNonExistingVirement() throws Exception {
        // Get the virement
        restVirementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVirement() throws Exception {
        // Initialize the database
        insertedVirement = virementRepository.saveAndFlush(virement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the virement
        Virement updatedVirement = virementRepository.findById(virement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVirement are not directly saved in db
        em.detach(updatedVirement);
        updatedVirement.compteBeneficiaire(UPDATED_COMPTE_BENEFICIAIRE).motif(UPDATED_MOTIF);
        VirementDTO virementDTO = virementMapper.toDto(updatedVirement);

        restVirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(virementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Virement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVirementToMatchAllProperties(updatedVirement);
    }

    @Test
    @Transactional
    void putNonExistingVirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        virement.setId(longCount.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, virementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(virementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        virement.setId(longCount.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(virementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        virement.setId(longCount.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(virementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Virement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVirementWithPatch() throws Exception {
        // Initialize the database
        insertedVirement = virementRepository.saveAndFlush(virement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the virement using partial update
        Virement partialUpdatedVirement = new Virement();
        partialUpdatedVirement.setId(virement.getId());

        partialUpdatedVirement.motif(UPDATED_MOTIF);

        restVirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVirement))
            )
            .andExpect(status().isOk());

        // Validate the Virement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVirementUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVirement, virement), getPersistedVirement(virement));
    }

    @Test
    @Transactional
    void fullUpdateVirementWithPatch() throws Exception {
        // Initialize the database
        insertedVirement = virementRepository.saveAndFlush(virement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the virement using partial update
        Virement partialUpdatedVirement = new Virement();
        partialUpdatedVirement.setId(virement.getId());

        partialUpdatedVirement.compteBeneficiaire(UPDATED_COMPTE_BENEFICIAIRE).motif(UPDATED_MOTIF);

        restVirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVirement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVirement))
            )
            .andExpect(status().isOk());

        // Validate the Virement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVirementUpdatableFieldsEquals(partialUpdatedVirement, getPersistedVirement(partialUpdatedVirement));
    }

    @Test
    @Transactional
    void patchNonExistingVirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        virement.setId(longCount.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, virementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(virementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        virement.setId(longCount.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(virementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Virement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        virement.setId(longCount.incrementAndGet());

        // Create the Virement
        VirementDTO virementDTO = virementMapper.toDto(virement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVirementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(virementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Virement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVirement() throws Exception {
        // Initialize the database
        insertedVirement = virementRepository.saveAndFlush(virement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the virement
        restVirementMockMvc
            .perform(delete(ENTITY_API_URL_ID, virement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return virementRepository.count();
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

    protected Virement getPersistedVirement(Virement virement) {
        return virementRepository.findById(virement.getId()).orElseThrow();
    }

    protected void assertPersistedVirementToMatchAllProperties(Virement expectedVirement) {
        assertVirementAllPropertiesEquals(expectedVirement, getPersistedVirement(expectedVirement));
    }

    protected void assertPersistedVirementToMatchUpdatableProperties(Virement expectedVirement) {
        assertVirementAllUpdatablePropertiesEquals(expectedVirement, getPersistedVirement(expectedVirement));
    }
}
