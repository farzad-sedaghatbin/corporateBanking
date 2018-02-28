package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.CorporateTransaction;
import com.kian.corporatebanking.domain.TransactionSigner;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the TransactionSigner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionSignerRepository extends JpaRepository<TransactionSigner, Long> {

    public List<TransactionSigner> findByCorporateTransaction(CorporateTransaction corporateTransaction);
    public Set<TransactionSigner> findByPartId(Long id);


}
