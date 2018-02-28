package com.kian.corporatebanking.service;

import com.kian.corporatebanking.domain.TransactionDescription;
import com.kian.corporatebanking.service.dto.TransactionDescriptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TransactionDescription.
 */
public interface TransactionDescriptionService {

    /**
     * Save a transactionDescription.
     *
     * @param transactionDescriptionDTO the entity to save
     * @return the persisted entity
     */
    TransactionDescriptionDTO save(TransactionDescriptionDTO transactionDescriptionDTO);

    /**
     * Get all the transactionDescriptions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TransactionDescriptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transactionDescription.
     *
     * @param id the id of the entity
     * @return the entity
     */
    TransactionDescriptionDTO findOne(Long id);

    /**
     * Delete the "id" transactionDescription.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    TransactionDescription findByLabel(String label);

}
