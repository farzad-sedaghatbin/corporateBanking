package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.CorporateTransaction;
import com.kian.corporatebanking.service.dto.CorporateTransactionDTO;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the CorporateTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporateTransactionRepository extends JpaRepository<CorporateTransaction, Long> {

    List<CorporateTransactionDTO> findByCreatorIdAndFromAccountId(Long creatorId, Long fromAccountId);

}
