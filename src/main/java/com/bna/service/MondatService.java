package com.bna.service;

import com.bna.domain.Mondat;
import com.bna.repository.MondatRepository;
import com.bna.service.dto.MondatDTO;
import com.bna.service.mapper.MondatMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bna.domain.Mondat}.
 */
@Service
@Transactional
public class MondatService {

    private static final Logger LOG = LoggerFactory.getLogger(MondatService.class);

    private final MondatRepository mondatRepository;

    private final MondatMapper mondatMapper;

    public MondatService(MondatRepository mondatRepository, MondatMapper mondatMapper) {
        this.mondatRepository = mondatRepository;
        this.mondatMapper = mondatMapper;
    }

    /**
     * Save a mondat.
     *
     * @param mondatDTO the entity to save.
     * @return the persisted entity.
     */
    public MondatDTO save(MondatDTO mondatDTO) {
        LOG.debug("Request to save Mondat : {}", mondatDTO);
        Mondat mondat = mondatMapper.toEntity(mondatDTO);
        mondat = mondatRepository.save(mondat);
        return mondatMapper.toDto(mondat);
    }

    /**
     * Update a mondat.
     *
     * @param mondatDTO the entity to save.
     * @return the persisted entity.
     */
    public MondatDTO update(MondatDTO mondatDTO) {
        LOG.debug("Request to update Mondat : {}", mondatDTO);
        Mondat mondat = mondatMapper.toEntity(mondatDTO);
        mondat = mondatRepository.save(mondat);
        return mondatMapper.toDto(mondat);
    }

    /**
     * Partially update a mondat.
     *
     * @param mondatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MondatDTO> partialUpdate(MondatDTO mondatDTO) {
        LOG.debug("Request to partially update Mondat : {}", mondatDTO);

        return mondatRepository
            .findById(mondatDTO.getId())
            .map(existingMondat -> {
                mondatMapper.partialUpdate(existingMondat, mondatDTO);

                return existingMondat;
            })
            .map(mondatRepository::save)
            .map(mondatMapper::toDto);
    }
    /**
     * Mondat pour les compte d'un utilisateur
    */
    @Transactional(readOnly = true)
    public List<MondatDTO> findAllByCurrentUser() {
    LOG.debug("Request to get all virements for current user");
    return mondatRepository.findAllByCurrentUser()
        .stream()
        .map(mondatMapper::toDto)
        .toList();
    }

    /**
     * Get all the mondats.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MondatDTO> findAll() {
        LOG.debug("Request to get all Mondats");
        return mondatRepository.findAll().stream().map(mondatMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one mondat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MondatDTO> findOne(Long id) {
        LOG.debug("Request to get Mondat : {}", id);
        return mondatRepository.findById(id).map(mondatMapper::toDto);
    }

    /**
     * Delete the mondat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Mondat : {}", id);
        mondatRepository.deleteById(id);
    }
}
