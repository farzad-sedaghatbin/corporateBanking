package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.TransactionSigner;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransactionSigner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionSignerRepository extends JpaRepository<TransactionSigner, Long> {

}
