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
    @Query("select distinct transaction_tag from TransactionTag transaction_tag left join fetch transaction_tag.tags")
    List<TransactionTag> findAllWithEagerRelationships();

    @Query("select transaction_tag from TransactionTag transaction_tag left join fetch transaction_tag.tags where transaction_tag.id =:id")
    TransactionTag findOneWithEagerRelationships(@Param("id") Long id);

}
