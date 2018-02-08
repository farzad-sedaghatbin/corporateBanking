package com.kian.corporatebanking.service;

import com.kian.corporatebanking.service.dto.TransactionOperationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TransactionOperation.
 */
public interface TransactionOperationService {

    /**
     * Save a transactionOperation.
     *
     * @param transactionOperationDTO the entity to save
     * @return the persisted entity
     */
    TransactionOperationDTO save(TransactionOperationDTO transactionOperationDTO);

    /**
     * Get all the transactionOperations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TransactionOperationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transactionOperation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    TransactionOperationDTO findOne(Long id);

    /**
     * Delete the "id" transactionOperation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
