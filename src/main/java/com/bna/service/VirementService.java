package com.bna.service;

import com.bna.domain.Virement;
import com.bna.repository.VirementRepository;
import com.bna.service.dto.VirementDTO;
import com.bna.service.mapper.VirementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bna.domain.Virement}.
 */
@Service
@Transactional
public class VirementService {

    private static final Logger LOG = LoggerFactory.getLogger(VirementService.class);

    private final VirementRepository virementRepository;

    private final VirementMapper virementMapper;

    public VirementService(VirementRepository virementRepository, VirementMapper virementMapper) {
        this.virementRepository = virementRepository;
        this.virementMapper = virementMapper;
    }

    /**
     * Save a virement.
     *
     * @param virementDTO the entity to save.
     * @return the persisted entity.
     */
    public VirementDTO save(VirementDTO virementDTO) {
        LOG.debug("Request to save Virement : {}", virementDTO);
        Virement virement = virementMapper.toEntity(virementDTO);
        virement = virementRepository.save(virement);
        return virementMapper.toDto(virement);
    }

    /**
     * Update a virement.
     *
     * @param virementDTO the entity to save.
     * @return the persisted entity.
     */
    public VirementDTO update(VirementDTO virementDTO) {
        LOG.debug("Request to update Virement : {}", virementDTO);
        Virement virement = virementMapper.toEntity(virementDTO);
        virement = virementRepository.save(virement);
        return virementMapper.toDto(virement);
    }
    /**
     * Virement pour les compte d'un utilisateur
    */
    @Transactional(readOnly = true)
    public List<VirementDTO> findAllByCurrentUser() {
    LOG.debug("Request to get all virements for current user");
    return virementRepository.findAllByCurrentUser()
        .stream()
        .map(virementMapper::toDto)
        .toList();
    }

    /**
     * Partially update a virement.
     *
     * @param virementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VirementDTO> partialUpdate(VirementDTO virementDTO) {
        LOG.debug("Request to partially update Virement : {}", virementDTO);

        return virementRepository
            .findById(virementDTO.getId())
            .map(existingVirement -> {
                virementMapper.partialUpdate(existingVirement, virementDTO);

                return existingVirement;
            })
            .map(virementRepository::save)
            .map(virementMapper::toDto);
    }

    /**
     * Get all the virements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VirementDTO> findAll() {
        LOG.debug("Request to get all Virements");
        return virementRepository.findAll().stream().map(virementMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one virement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VirementDTO> findOne(Long id) {
        LOG.debug("Request to get Virement : {}", id);
        return virementRepository.findById(id).map(virementMapper::toDto);
    }

    /**
     * Delete the virement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Virement : {}", id);
        virementRepository.deleteById(id);
    }
}