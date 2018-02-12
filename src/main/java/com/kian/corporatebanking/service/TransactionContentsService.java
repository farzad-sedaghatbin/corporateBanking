package com.kian.corporatebanking.service;

import com.kian.corporatebanking.service.dto.TransactionContentsDTO;
import java.util.List;

/**
 * Service Interface for managing TransactionContents.
 */
public interface TransactionContentsService {

    /**
     * Save a transactionContents.
     *
     * @param transactionContentsDTO the entity to save
     * @return the persisted entity
     */
    TransactionContentsDTO save(TransactionContentsDTO transactionContentsDTO);

    /**
     * Get all the transactionContents.
     *
     * @return the list of entities
     */
    List<TransactionContentsDTO> findAll();

    /**
     * Get the "id" transactionContents.
     *
     * @param id the id of the entity
     * @return the entity
     */
    TransactionContentsDTO findOne(Long id);

    /**
     * Delete the "id" transactionContents.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
