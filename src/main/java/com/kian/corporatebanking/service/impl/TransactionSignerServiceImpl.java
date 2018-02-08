package com.kian.corporatebanking.service.impl;

import com.kian.corporatebanking.service.TransactionSignerService;
import com.kian.corporatebanking.domain.TransactionSigner;
import com.kian.corporatebanking.repository.TransactionSignerRepository;
import com.kian.corporatebanking.service.dto.TransactionSignerDTO;
import com.kian.corporatebanking.service.mapper.TransactionSignerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TransactionSigner.
 */
@Service
@Transactional
public class TransactionSignerServiceImpl implements TransactionSignerService {

    private final Logger log = LoggerFactory.getLogger(TransactionSignerServiceImpl.class);

    private final TransactionSignerRepository transactionSignerRepository;

    private final TransactionSignerMapper transactionSignerMapper;

    public TransactionSignerServiceImpl(TransactionSignerRepository transactionSignerRepository, TransactionSignerMapper transactionSignerMapper) {
        this.transactionSignerRepository = transactionSignerRepository;
        this.transactionSignerMapper = transactionSignerMapper;
    }

    /**
     * Save a transactionSigner.
     *
     * @param transactionSignerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionSignerDTO save(TransactionSignerDTO transactionSignerDTO) {
        log.debug("Request to save TransactionSigner : {}", transactionSignerDTO);
        TransactionSigner transactionSigner = transactionSignerMapper.toEntity(transactionSignerDTO);
        transactionSigner = transactionSignerRepository.save(transactionSigner);
        return transactionSignerMapper.toDto(transactionSigner);
    }

    /**
     * Get all the transactionSigners.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionSignerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionSigners");
        return transactionSignerRepository.findAll(pageable)
            .map(transactionSignerMapper::toDto);
    }

    /**
     * Get one transactionSigner by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionSignerDTO findOne(Long id) {
        log.debug("Request to get TransactionSigner : {}", id);
        TransactionSigner transactionSigner = transactionSignerRepository.findOne(id);
        return transactionSignerMapper.toDto(transactionSigner);
    }

    /**
     * Delete the transactionSigner by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionSigner : {}", id);
        transactionSignerRepository.delete(id);
    }
}
