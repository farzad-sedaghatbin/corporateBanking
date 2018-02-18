package com.kian.corporatebanking.service.impl;

import com.kian.corporatebanking.service.CorporateTransactionService;
import com.kian.corporatebanking.service.TransactionTagService;
import com.kian.corporatebanking.domain.TransactionTag;
import com.kian.corporatebanking.repository.TransactionTagRepository;
import com.kian.corporatebanking.service.dto.TransactionTagDTO;
import com.kian.corporatebanking.service.mapper.CorporateTransactionMapper;
import com.kian.corporatebanking.service.mapper.TransactionTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TransactionTag.
 */
@Service
@Transactional
public class TransactionTagServiceImpl implements TransactionTagService {

    private final Logger log = LoggerFactory.getLogger(TransactionTagServiceImpl.class);

    private final TransactionTagRepository transactionTagRepository;

    private final TransactionTagMapper transactionTagMapper;
    private final CorporateTransactionService corporateTransactionService;
    private final CorporateTransactionMapper corporateTransactionMapper;

    public TransactionTagServiceImpl(TransactionTagRepository transactionTagRepository, TransactionTagMapper transactionTagMapper, CorporateTransactionService corporateTransactionService, CorporateTransactionMapper corporateTransactionMapper) {
        this.transactionTagRepository = transactionTagRepository;
        this.transactionTagMapper = transactionTagMapper;
        this.corporateTransactionService = corporateTransactionService;
        this.corporateTransactionMapper = corporateTransactionMapper;
    }

    /**
     * Save a transactionTag.
     *
     * @param transactionTagDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionTagDTO save(TransactionTagDTO transactionTagDTO) {
        log.debug("Request to save TransactionTag : {}", transactionTagDTO);
        TransactionTag transactionTag = transactionTagMapper.toEntity(transactionTagDTO);
        if (transactionTagDTO.getCorporateTransactionId() != null) {
            transactionTag.getCorporateTransactions().add(corporateTransactionMapper.toEntity(corporateTransactionService.findOne(transactionTagDTO.getCorporateTransactionId())));
        }
        transactionTag = transactionTagRepository.save(transactionTag);
        return transactionTagMapper.toDto(transactionTag);
    }

    /**
     * Get all the transactionTags.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionTags");
        return transactionTagRepository.findAll(pageable)
            .map(transactionTagMapper::toDto);
    }

    /**
     * Get one transactionTag by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionTagDTO findOne(Long id) {
        log.debug("Request to get TransactionTag : {}", id);
        TransactionTag transactionTag = transactionTagRepository.findOne(id);
        return transactionTagMapper.toDto(transactionTag);
    }

    /**
     * Delete the transactionTag by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionTag : {}", id);
        transactionTagRepository.delete(id);
    }

    @Override
    public TransactionTagDTO findByPartyIdAndLabel(Long partyId, String label) {
        return transactionTagMapper.toDto(transactionTagRepository.findByPartyIdAndLabel(partyId, label));
    }
}
