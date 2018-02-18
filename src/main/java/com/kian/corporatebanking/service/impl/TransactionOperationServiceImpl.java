package com.kian.corporatebanking.service.impl;

import com.kian.corporatebanking.domain.TransactionSigner;
import com.kian.corporatebanking.service.CorporateTransactionService;
import com.kian.corporatebanking.service.TransactionOperationService;
import com.kian.corporatebanking.domain.TransactionOperation;
import com.kian.corporatebanking.repository.TransactionOperationRepository;
import com.kian.corporatebanking.service.TransactionSignerService;
import com.kian.corporatebanking.service.dto.TransactionOperationDTO;
import com.kian.corporatebanking.service.mapper.CorporateTransactionMapper;
import com.kian.corporatebanking.service.mapper.TransactionOperationMapper;
import com.kian.corporatebanking.service.mapper.TransactionSignerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TransactionOperation.
 */
@Service
@Transactional
public class TransactionOperationServiceImpl implements TransactionOperationService {

    private final Logger log = LoggerFactory.getLogger(TransactionOperationServiceImpl.class);

    private final TransactionOperationRepository transactionOperationRepository;

    private final TransactionOperationMapper transactionOperationMapper;
    private final CorporateTransactionMapper corporateTransactionMapper;

    private final TransactionSignerMapper transactionSignerMapper;

    private final TransactionSignerService transactionSignerService;

    private final CorporateTransactionService corporateTransactionService;

    public TransactionOperationServiceImpl(TransactionOperationRepository transactionOperationRepository, TransactionOperationMapper transactionOperationMapper, CorporateTransactionMapper corporateTransactionMapper, TransactionSignerMapper transactionSignerMapper, TransactionSignerService transactionSignerService, CorporateTransactionService corporateTransactionService) {
        this.transactionOperationRepository = transactionOperationRepository;
        this.transactionOperationMapper = transactionOperationMapper;
        this.corporateTransactionMapper = corporateTransactionMapper;
        this.transactionSignerMapper = transactionSignerMapper;
        this.transactionSignerService = transactionSignerService;
        this.corporateTransactionService = corporateTransactionService;
    }

    /**
     * Save a transactionOperation.
     *
     * @param transactionOperationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionOperationDTO save(TransactionOperationDTO transactionOperationDTO) {
        log.debug("Request to save TransactionOperation : {}", transactionOperationDTO);
        TransactionOperation transactionOperation = transactionOperationMapper.toEntity(transactionOperationDTO);
        transactionOperation = transactionOperationRepository.save(transactionOperation);
        //soal
        TransactionSigner transactionSigner = transactionSignerMapper.toEntity(transactionSignerService.findOne(transactionOperationDTO.getTransactionSignerId()));
        transactionSigner.setOperationType(transactionOperation.getOperationType());
        transactionSignerService.save(transactionSignerMapper.toDto(transactionSigner));
        corporateTransactionService.checkStatus(corporateTransactionMapper.toEntity(corporateTransactionService.findOne(transactionSigner.getCorporateTransaction().getId())));
        return transactionOperationMapper.toDto(transactionOperation);
    }

    /**
     * Get all the transactionOperations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionOperationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionOperations");
        return transactionOperationRepository.findAll(pageable)
            .map(transactionOperationMapper::toDto);
    }

    /**
     * Get one transactionOperation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionOperationDTO findOne(Long id) {
        log.debug("Request to get TransactionOperation : {}", id);
        TransactionOperation transactionOperation = transactionOperationRepository.findOne(id);
        return transactionOperationMapper.toDto(transactionOperation);
    }

    /**
     * Delete the transactionOperation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionOperation : {}", id);
        transactionOperationRepository.delete(id);
    }



}
