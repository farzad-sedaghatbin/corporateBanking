package com.kian.corporatebanking.repository;

import com.kian.corporatebanking.domain.CorporateTransaction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the CorporateTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporateTransactionRepository extends JpaRepository<CorporateTransaction, Long> {
    @Query("select distinct corporate_transaction from CorporateTransaction corporate_transaction left join fetch corporate_transaction.tags")
    List<CorporateTransaction> findAllWithEagerRelationships();

    @Query("select corporate_transaction from CorporateTransaction corporate_transaction left join fetch corporate_transaction.tags where corporate_transaction.id =:id")
    CorporateTransaction findOneWithEagerRelationships(@Param("id") Long id);

}
