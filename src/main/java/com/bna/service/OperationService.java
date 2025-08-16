package com.bna.service;

import com.bna.domain.Operation;
import com.bna.repository.OperationRepository;
import com.bna.service.dto.OperationDTO;
import com.bna.service.mapper.OperationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bna.domain.Operation}.
 */
@Service
@Transactional
public class OperationService {

    private static final Logger LOG = LoggerFactory.getLogger(OperationService.class);

    private final OperationRepository operationRepository;

    private final OperationMapper operationMapper;

    public OperationService(OperationRepository operationRepository, OperationMapper operationMapper) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
    }

    /**
     * Save a operation.
     *
     * @param operationDTO the entity to save.
     * @return the persisted entity.
     */
    public OperationDTO save(OperationDTO operationDTO) {
        LOG.debug("Request to save Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    /**
     * Update a operation.
     *
     * @param operationDTO the entity to save.
     * @return the persisted entity.
     */
    public OperationDTO update(OperationDTO operationDTO) {
        LOG.debug("Request to update Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    /**
     * Partially update a operation.
     *
     * @param operationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OperationDTO> partialUpdate(OperationDTO operationDTO) {
        LOG.debug("Request to partially update Operation : {}", operationDTO);

        return operationRepository
            .findById(operationDTO.getId())
            .map(existingOperation -> {
                operationMapper.partialUpdate(existingOperation, operationDTO);

                return existingOperation;
            })
            .map(operationRepository::save)
            .map(operationMapper::toDto);
    }

    /**
     * Get all the operations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OperationDTO> findAll() {
        LOG.debug("Request to get all Operations");
        return operationRepository.findAll().stream().map(operationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one operation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OperationDTO> findOne(Long id) {
        LOG.debug("Request to get Operation : {}", id);
        return operationRepository.findById(id).map(operationMapper::toDto);
    }


    /**
     * Operations pour les compte d'un utilisateur
     */
    @Transactional(readOnly = true)
    public List<OperationDTO> findAllByCurrentUser() {
    LOG.debug("Request to get all Operations for current user");
    return operationRepository.findAllByCurrentUser()
        .stream()
        .map(operationMapper::toDto)
        .toList();
    }

    /**
     * Delete the operation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Operation : {}", id);
        operationRepository.deleteById(id);
    }
}
