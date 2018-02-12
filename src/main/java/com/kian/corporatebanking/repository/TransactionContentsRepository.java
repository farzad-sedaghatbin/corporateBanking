package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.TransactionContents;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransactionContents entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionContentsRepository extends JpaRepository<TransactionContents, Long> {

}
