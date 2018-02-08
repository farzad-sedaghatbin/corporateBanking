package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.TransactionTag;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransactionTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionTagRepository extends JpaRepository<TransactionTag, Long> {

}
