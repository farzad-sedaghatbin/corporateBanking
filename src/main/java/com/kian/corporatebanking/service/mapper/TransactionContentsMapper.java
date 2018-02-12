package com.kian.corporatebanking.service.mapper;

import com.kian.corporatebanking.domain.*;
import com.kian.corporatebanking.service.dto.TransactionContentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransactionContents and its DTO TransactionContentsDTO.
 */
@Mapper(componentModel = "spring", uses = {CorporateTransactionMapper.class})
public interface TransactionContentsMapper extends EntityMapper<TransactionContentsDTO, TransactionContents> {

    @Mapping(source = "corporateTransaction.id", target = "corporateTransactionId")
    TransactionContentsDTO toDto(TransactionContents transactionContents);

    @Mapping(source = "corporateTransactionId", target = "corporateTransaction")
    TransactionContents toEntity(TransactionContentsDTO transactionContentsDTO);

    default TransactionContents fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionContents transactionContents = new TransactionContents();
        transactionContents.setId(id);
        return transactionContents;
    }
}
