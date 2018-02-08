package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.TransactionDescription;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransactionDescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionDescriptionRepository extends JpaRepository<TransactionDescription, Long> {

}
