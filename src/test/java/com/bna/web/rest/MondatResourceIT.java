package com.bna.web.rest;

import static com.bna.domain.MondatAsserts.*;
import static com.bna.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bna.IntegrationTest;
import com.bna.domain.Mondat;
import com.bna.repository.MondatRepository;
import com.bna.service.dto.MondatDTO;
import com.bna.service.mapper.MondatMapper;
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
 * Integration tests for the {@link MondatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MondatResourceIT {

    private static final String DEFAULT_COMPTE_SRC = "AAAAAAAAAA";
    private static final String UPDATED_COMPTE_SRC = "BBBBBBBBBB";

    private static final String DEFAULT_COMPTE_BENEF = "AAAAAAAAAA";
    private static final String UPDATED_COMPTE_BENEF = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mondats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MondatRepository mondatRepository;

    @Autowired
    private MondatMapper mondatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMondatMockMvc;

    private Mondat mondat;

    private Mondat insertedMondat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mondat createEntity() {
        return new Mondat().compteSrc(DEFAULT_COMPTE_SRC).compteBenef(DEFAULT_COMPTE_BENEF).montant(DEFAULT_MONTANT).code(DEFAULT_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mondat createUpdatedEntity() {
        return new Mondat().compteSrc(UPDATED_COMPTE_SRC).compteBenef(UPDATED_COMPTE_BENEF).montant(UPDATED_MONTANT).code(UPDATED_CODE);
    }

    @BeforeEach
    void initTest() {
        mondat = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMondat != null) {
            mondatRepository.delete(insertedMondat);
            insertedMondat = null;
        }
    }

    @Test
    @Transactional
    void createMondat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mondat
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);
        var returnedMondatDTO = om.readValue(
            restMondatMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mondatDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MondatDTO.class
        );

        // Validate the Mondat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMondat = mondatMapper.toEntity(returnedMondatDTO);
        assertMondatUpdatableFieldsEquals(returnedMondat, getPersistedMondat(returnedMondat));

        insertedMondat = returnedMondat;
    }

    @Test
    @Transactional
    void createMondatWithExistingId() throws Exception {
        // Create the Mondat with an existing ID
        mondat.setId(1L);
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMondatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mondatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mondat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompteSrcIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mondat.setCompteSrc(null);

        // Create the Mondat, which fails.
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        restMondatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mondatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompteBenefIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mondat.setCompteBenef(null);

        // Create the Mondat, which fails.
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        restMondatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mondatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mondat.setMontant(null);

        // Create the Mondat, which fails.
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        restMondatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mondatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMondats() throws Exception {
        // Initialize the database
        insertedMondat = mondatRepository.saveAndFlush(mondat);

        // Get all the mondatList
        restMondatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mondat.getId().intValue())))
            .andExpect(jsonPath("$.[*].compteSrc").value(hasItem(DEFAULT_COMPTE_SRC)))
            .andExpect(jsonPath("$.[*].compteBenef").value(hasItem(DEFAULT_COMPTE_BENEF)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getMondat() throws Exception {
        // Initialize the database
        insertedMondat = mondatRepository.saveAndFlush(mondat);

        // Get the mondat
        restMondatMockMvc
            .perform(get(ENTITY_API_URL_ID, mondat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mondat.getId().intValue()))
            .andExpect(jsonPath("$.compteSrc").value(DEFAULT_COMPTE_SRC))
            .andExpect(jsonPath("$.compteBenef").value(DEFAULT_COMPTE_BENEF))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingMondat() throws Exception {
        // Get the mondat
        restMondatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMondat() throws Exception {
        // Initialize the database
        insertedMondat = mondatRepository.saveAndFlush(mondat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mondat
        Mondat updatedMondat = mondatRepository.findById(mondat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMondat are not directly saved in db
        em.detach(updatedMondat);
        updatedMondat.compteSrc(UPDATED_COMPTE_SRC).compteBenef(UPDATED_COMPTE_BENEF).montant(UPDATED_MONTANT).code(UPDATED_CODE);
        MondatDTO mondatDTO = mondatMapper.toDto(updatedMondat);

        restMondatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mondatDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mondatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mondat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMondatToMatchAllProperties(updatedMondat);
    }

    @Test
    @Transactional
    void putNonExistingMondat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mondat.setId(longCount.incrementAndGet());

        // Create the Mondat
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMondatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mondatDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mondatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mondat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMondat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mondat.setId(longCount.incrementAndGet());

        // Create the Mondat
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMondatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mondatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mondat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMondat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mondat.setId(longCount.incrementAndGet());

        // Create the Mondat
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMondatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mondatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mondat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMondatWithPatch() throws Exception {
        // Initialize the database
        insertedMondat = mondatRepository.saveAndFlush(mondat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mondat using partial update
        Mondat partialUpdatedMondat = new Mondat();
        partialUpdatedMondat.setId(mondat.getId());

        partialUpdatedMondat.compteSrc(UPDATED_COMPTE_SRC).compteBenef(UPDATED_COMPTE_BENEF).code(UPDATED_CODE);

        restMondatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMondat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMondat))
            )
            .andExpect(status().isOk());

        // Validate the Mondat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMondatUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMondat, mondat), getPersistedMondat(mondat));
    }

    @Test
    @Transactional
    void fullUpdateMondatWithPatch() throws Exception {
        // Initialize the database
        insertedMondat = mondatRepository.saveAndFlush(mondat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mondat using partial update
        Mondat partialUpdatedMondat = new Mondat();
        partialUpdatedMondat.setId(mondat.getId());

        partialUpdatedMondat.compteSrc(UPDATED_COMPTE_SRC).compteBenef(UPDATED_COMPTE_BENEF).montant(UPDATED_MONTANT).code(UPDATED_CODE);

        restMondatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMondat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMondat))
            )
            .andExpect(status().isOk());

        // Validate the Mondat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMondatUpdatableFieldsEquals(partialUpdatedMondat, getPersistedMondat(partialUpdatedMondat));
    }

    @Test
    @Transactional
    void patchNonExistingMondat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mondat.setId(longCount.incrementAndGet());

        // Create the Mondat
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMondatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mondatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mondatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mondat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMondat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mondat.setId(longCount.incrementAndGet());

        // Create the Mondat
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMondatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mondatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mondat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMondat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mondat.setId(longCount.incrementAndGet());

        // Create the Mondat
        MondatDTO mondatDTO = mondatMapper.toDto(mondat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMondatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mondatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mondat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMondat() throws Exception {
        // Initialize the database
        insertedMondat = mondatRepository.saveAndFlush(mondat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mondat
        restMondatMockMvc
            .perform(delete(ENTITY_API_URL_ID, mondat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mondatRepository.count();
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

    protected Mondat getPersistedMondat(Mondat mondat) {
        return mondatRepository.findById(mondat.getId()).orElseThrow();
    }

    protected void assertPersistedMondatToMatchAllProperties(Mondat expectedMondat) {
        assertMondatAllPropertiesEquals(expectedMondat, getPersistedMondat(expectedMondat));
    }

    protected void assertPersistedMondatToMatchUpdatableProperties(Mondat expectedMondat) {
        assertMondatAllUpdatablePropertiesEquals(expectedMondat, getPersistedMondat(expectedMondat));
    }
}
