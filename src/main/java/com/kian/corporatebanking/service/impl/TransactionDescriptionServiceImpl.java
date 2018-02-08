package com.kian.corporatebanking.service.impl;

import com.kian.corporatebanking.service.TransactionDescriptionService;
import com.kian.corporatebanking.domain.TransactionDescription;
import com.kian.corporatebanking.repository.TransactionDescriptionRepository;
import com.kian.corporatebanking.service.dto.TransactionDescriptionDTO;
import com.kian.corporatebanking.service.mapper.TransactionDescriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TransactionDescription.
 */
@Service
@Transactional
public class TransactionDescriptionServiceImpl implements TransactionDescriptionService {

    private final Logger log = LoggerFactory.getLogger(TransactionDescriptionServiceImpl.class);

    private final TransactionDescriptionRepository transactionDescriptionRepository;

    private final TransactionDescriptionMapper transactionDescriptionMapper;

    public TransactionDescriptionServiceImpl(TransactionDescriptionRepository transactionDescriptionRepository, TransactionDescriptionMapper transactionDescriptionMapper) {
        this.transactionDescriptionRepository = transactionDescriptionRepository;
        this.transactionDescriptionMapper = transactionDescriptionMapper;
    }

    /**
     * Save a transactionDescription.
     *
     * @param transactionDescriptionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionDescriptionDTO save(TransactionDescriptionDTO transactionDescriptionDTO) {
        log.debug("Request to save TransactionDescription : {}", transactionDescriptionDTO);
        TransactionDescription transactionDescription = transactionDescriptionMapper.toEntity(transactionDescriptionDTO);
        transactionDescription = transactionDescriptionRepository.save(transactionDescription);
        return transactionDescriptionMapper.toDto(transactionDescription);
    }

    /**
     * Get all the transactionDescriptions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDescriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionDescriptions");
        return transactionDescriptionRepository.findAll(pageable)
            .map(transactionDescriptionMapper::toDto);
    }

    /**
     * Get one transactionDescription by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionDescriptionDTO findOne(Long id) {
        log.debug("Request to get TransactionDescription : {}", id);
        TransactionDescription transactionDescription = transactionDescriptionRepository.findOne(id);
        return transactionDescriptionMapper.toDto(transactionDescription);
    }

    /**
     * Delete the transactionDescription by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionDescription : {}", id);
        transactionDescriptionRepository.delete(id);
    }
}
