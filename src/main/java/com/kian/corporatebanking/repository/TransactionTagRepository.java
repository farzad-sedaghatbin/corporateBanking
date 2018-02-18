package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.TransactionTag;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the TransactionTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionTagRepository extends JpaRepository<TransactionTag, Long> {


    TransactionTag findByPartyIdAndLabel(Long partyId,String label);

}
