package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.CorporateTransaction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the CorporateTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporateTransactionRepository extends JpaRepository<CorporateTransaction, Long> {

    Set<CorporateTransaction> findByCreatorIdAndFromAccountId(Long creatorId, Long fromAccountId);

    Set<CorporateTransaction> findByToAccountId(Long toAccountId);


    Set<CorporateTransaction> findByFromAccountIdAndDescriptions_Label(Long id,String label);


    Set<CorporateTransaction> findByFromAccountIdAndDraft(Long id,Boolean draft);


}
