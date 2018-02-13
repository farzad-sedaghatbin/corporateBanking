package com.kian.corporatebanking.service;

import com.kian.corporatebanking.domain.CorporateTransaction;
import com.kian.corporatebanking.service.dto.CorporateTransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing CorporateTransaction.
 */
public interface CorporateTransactionService {

    /**
     * Save a corporateTransaction.
     *
     * @param corporateTransactionDTO the entity to save
     * @return the persisted entity
     */
    CorporateTransactionDTO save(CorporateTransactionDTO corporateTransactionDTO);

    /**
     * Get all the corporateTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CorporateTransactionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" corporateTransaction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CorporateTransactionDTO findOne(Long id);

    /**
     * Delete the "id" corporateTransaction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    void checkStatus(CorporateTransaction corporateTransaction);

    List<CorporateTransactionDTO> findByCreatorIdAndFromAccountId(Long creatorId,Long transactionId);
}
