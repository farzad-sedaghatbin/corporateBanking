package com.kian.corporatebanking.service;

import com.kian.corporatebanking.domain.CorporateTransaction;
import com.kian.corporatebanking.domain.TransactionSigner;
import com.kian.corporatebanking.service.dto.CorporateTransactionDTO;
import com.kian.corporatebanking.service.dto.TransactionSignerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing TransactionSigner.
 */
public interface TransactionSignerService {

    /**
     * Save a transactionSigner.
     *
     * @param transactionSignerDTO the entity to save
     * @return the persisted entity
     */
    TransactionSignerDTO save(TransactionSignerDTO transactionSignerDTO);

    /**
     * Get all the transactionSigners.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TransactionSignerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transactionSigner.
     *
     * @param id the id of the entity
     * @return the entity
     */
    TransactionSignerDTO findOne(Long id);

    /**
     * Delete the "id" transactionSigner.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<TransactionSignerDTO> findByCorporateTransaction(CorporateTransaction corporateTransaction);

    List<TransactionSignerDTO> findByPartyId(Long partyId);

    List<CorporateTransactionDTO> getAllCorporateTransactionByPartyId(Long partyId);

}
