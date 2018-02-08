package com.kian.corporatebanking.service.impl;

import com.kian.corporatebanking.service.CorporateTransactionService;
import com.kian.corporatebanking.domain.CorporateTransaction;
import com.kian.corporatebanking.repository.CorporateTransactionRepository;
import com.kian.corporatebanking.service.dto.CorporateTransactionDTO;
import com.kian.corporatebanking.service.mapper.CorporateTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing CorporateTransaction.
 */
@Service
@Transactional
public class CorporateTransactionServiceImpl implements CorporateTransactionService {

    private final Logger log = LoggerFactory.getLogger(CorporateTransactionServiceImpl.class);

    private final CorporateTransactionRepository corporateTransactionRepository;

    private final CorporateTransactionMapper corporateTransactionMapper;

    public CorporateTransactionServiceImpl(CorporateTransactionRepository corporateTransactionRepository, CorporateTransactionMapper corporateTransactionMapper) {
        this.corporateTransactionRepository = corporateTransactionRepository;
        this.corporateTransactionMapper = corporateTransactionMapper;
    }

    /**
     * Save a corporateTransaction.
     *
     * @param corporateTransactionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CorporateTransactionDTO save(CorporateTransactionDTO corporateTransactionDTO) {
        log.debug("Request to save CorporateTransaction : {}", corporateTransactionDTO);
        CorporateTransaction corporateTransaction = corporateTransactionMapper.toEntity(corporateTransactionDTO);
        corporateTransaction = corporateTransactionRepository.save(corporateTransaction);
        return corporateTransactionMapper.toDto(corporateTransaction);
    }

    /**
     * Get all the corporateTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CorporateTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CorporateTransactions");
        return corporateTransactionRepository.findAll(pageable)
            .map(corporateTransactionMapper::toDto);
    }

    /**
     * Get one corporateTransaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CorporateTransactionDTO findOne(Long id) {
        log.debug("Request to get CorporateTransaction : {}", id);
        CorporateTransaction corporateTransaction = corporateTransactionRepository.findOne(id);
        return corporateTransactionMapper.toDto(corporateTransaction);
    }

    /**
     * Delete the corporateTransaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CorporateTransaction : {}", id);
        corporateTransactionRepository.delete(id);
    }
}
