package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.TransactionOperation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransactionOperation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionOperationRepository extends JpaRepository<TransactionOperation, Long> {

}
