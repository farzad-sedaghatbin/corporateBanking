package com.kian.corporatebanking.service.impl;

import com.kian.corporatebanking.service.TransactionContentsService;
import com.kian.corporatebanking.domain.TransactionContents;
import com.kian.corporatebanking.repository.TransactionContentsRepository;
import com.kian.corporatebanking.service.dto.TransactionContentsDTO;
import com.kian.corporatebanking.service.mapper.TransactionContentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TransactionContents.
 */
@Service
@Transactional
public class TransactionContentsServiceImpl implements TransactionContentsService {

    private final Logger log = LoggerFactory.getLogger(TransactionContentsServiceImpl.class);

    private final TransactionContentsRepository transactionContentsRepository;

    private final TransactionContentsMapper transactionContentsMapper;

    public TransactionContentsServiceImpl(TransactionContentsRepository transactionContentsRepository, TransactionContentsMapper transactionContentsMapper) {
        this.transactionContentsRepository = transactionContentsRepository;
        this.transactionContentsMapper = transactionContentsMapper;
    }

    /**
     * Save a transactionContents.
     *
     * @param transactionContentsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionContentsDTO save(TransactionContentsDTO transactionContentsDTO) {
        log.debug("Request to save TransactionContents : {}", transactionContentsDTO);
        TransactionContents transactionContents = transactionContentsMapper.toEntity(transactionContentsDTO);
        transactionContents = transactionContentsRepository.save(transactionContents);
        return transactionContentsMapper.toDto(transactionContents);
    }

    /**
     * Get all the transactionContents.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransactionContentsDTO> findAll() {
        log.debug("Request to get all TransactionContents");
        return transactionContentsRepository.findAll().stream()
            .map(transactionContentsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one transactionContents by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionContentsDTO findOne(Long id) {
        log.debug("Request to get TransactionContents : {}", id);
        TransactionContents transactionContents = transactionContentsRepository.findOne(id);
        return transactionContentsMapper.toDto(transactionContents);
    }

    /**
     * Delete the transactionContents by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionContents : {}", id);
        transactionContentsRepository.delete(id);
    }
}
