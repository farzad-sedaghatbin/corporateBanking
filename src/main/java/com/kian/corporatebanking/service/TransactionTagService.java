package com.kian.corporatebanking.service;

import com.kian.corporatebanking.service.dto.TransactionTagDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TransactionTag.
 */
public interface TransactionTagService {

    /**
     * Save a transactionTag.
     *
     * @param transactionTagDTO the entity to save
     * @return the persisted entity
     */
    TransactionTagDTO save(TransactionTagDTO transactionTagDTO);

    /**
     * Get all the transactionTags.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TransactionTagDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transactionTag.
     *
     * @param id the id of the entity
     * @return the entity
     */
    TransactionTagDTO findOne(Long id);

    /**
     * Delete the "id" transactionTag.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
