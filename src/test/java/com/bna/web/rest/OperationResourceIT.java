package com.bna.web.rest;

import static com.bna.domain.OperationAsserts.*;
import static com.bna.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bna.IntegrationTest;
import com.bna.domain.Operation;
import com.bna.repository.OperationRepository;
import com.bna.service.dto.OperationDTO;
import com.bna.service.mapper.OperationMapper;
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
 * Integration tests for the {@link OperationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperationResourceIT {

    private static final Long DEFAULT_MONTANT = 1L;
    private static final Long UPDATED_MONTANT = 2L;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperationMockMvc;

    private Operation operation;

    private Operation insertedOperation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operation createEntity() {
        return new Operation().montant(DEFAULT_MONTANT).libelle(DEFAULT_LIBELLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operation createUpdatedEntity() {
        return new Operation().montant(UPDATED_MONTANT).libelle(UPDATED_LIBELLE);
    }

    @BeforeEach
    void initTest() {
        operation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOperation != null) {
            operationRepository.delete(insertedOperation);
            insertedOperation = null;
        }
    }

    @Test
    @Transactional
    void createOperation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);
        var returnedOperationDTO = om.readValue(
            restOperationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OperationDTO.class
        );

        // Validate the Operation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOperation = operationMapper.toEntity(returnedOperationDTO);
        assertOperationUpdatableFieldsEquals(returnedOperation, getPersistedOperation(returnedOperation));

        insertedOperation = returnedOperation;
    }

    @Test
    @Transactional
    void createOperationWithExistingId() throws Exception {
        // Create the Operation with an existing ID
        operation.setId(1L);
        OperationDTO operationDTO = operationMapper.toDto(operation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        operation.setMontant(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        operation.setLibelle(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperations() throws Exception {
        // Initialize the database
        insertedOperation = operationRepository.saveAndFlush(operation);

        // Get all the operationList
        restOperationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operation.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getOperation() throws Exception {
        // Initialize the database
        insertedOperation = operationRepository.saveAndFlush(operation);

        // Get the operation
        restOperationMockMvc
            .perform(get(ENTITY_API_URL_ID, operation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operation.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingOperation() throws Exception {
        // Get the operation
        restOperationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperation() throws Exception {
        // Initialize the database
        insertedOperation = operationRepository.saveAndFlush(operation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the operation
        Operation updatedOperation = operationRepository.findById(operation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOperation are not directly saved in db
        em.detach(updatedOperation);
        updatedOperation.montant(UPDATED_MONTANT).libelle(UPDATED_LIBELLE);
        OperationDTO operationDTO = operationMapper.toDto(updatedOperation);

        restOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(operationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOperationToMatchAllProperties(updatedOperation);
    }

    @Test
    @Transactional
    void putNonExistingOperation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operation.setId(longCount.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operation.setId(longCount.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operation.setId(longCount.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperationWithPatch() throws Exception {
        // Initialize the database
        insertedOperation = operationRepository.saveAndFlush(operation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the operation using partial update
        Operation partialUpdatedOperation = new Operation();
        partialUpdatedOperation.setId(operation.getId());

        partialUpdatedOperation.montant(UPDATED_MONTANT).libelle(UPDATED_LIBELLE);

        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOperation))
            )
            .andExpect(status().isOk());

        // Validate the Operation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOperationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOperation, operation),
            getPersistedOperation(operation)
        );
    }

    @Test
    @Transactional
    void fullUpdateOperationWithPatch() throws Exception {
        // Initialize the database
        insertedOperation = operationRepository.saveAndFlush(operation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the operation using partial update
        Operation partialUpdatedOperation = new Operation();
        partialUpdatedOperation.setId(operation.getId());

        partialUpdatedOperation.montant(UPDATED_MONTANT).libelle(UPDATED_LIBELLE);

        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOperation))
            )
            .andExpect(status().isOk());

        // Validate the Operation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOperationUpdatableFieldsEquals(partialUpdatedOperation, getPersistedOperation(partialUpdatedOperation));
    }

    @Test
    @Transactional
    void patchNonExistingOperation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operation.setId(longCount.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operation.setId(longCount.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operation.setId(longCount.incrementAndGet());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(operationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperation() throws Exception {
        // Initialize the database
        insertedOperation = operationRepository.saveAndFlush(operation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the operation
        restOperationMockMvc
            .perform(delete(ENTITY_API_URL_ID, operation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return operationRepository.count();
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

    protected Operation getPersistedOperation(Operation operation) {
        return operationRepository.findById(operation.getId()).orElseThrow();
    }

    protected void assertPersistedOperationToMatchAllProperties(Operation expectedOperation) {
        assertOperationAllPropertiesEquals(expectedOperation, getPersistedOperation(expectedOperation));
    }

    protected void assertPersistedOperationToMatchUpdatableProperties(Operation expectedOperation) {
        assertOperationAllUpdatablePropertiesEquals(expectedOperation, getPersistedOperation(expectedOperation));
    }
}
