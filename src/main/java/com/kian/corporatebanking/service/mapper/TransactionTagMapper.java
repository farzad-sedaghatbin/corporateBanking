package com.kian.corporatebanking.service.mapper;

import com.kian.corporatebanking.domain.*;
import com.kian.corporatebanking.service.dto.TransactionTagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransactionTag and its DTO TransactionTagDTO.
 */
@Mapper(componentModel = "spring", uses = {CorporateTransactionMapper.class})
public interface TransactionTagMapper extends EntityMapper<TransactionTagDTO, TransactionTag> {

    @Mapping(source = "corporateTransaction.id", target = "corporateTransactionId")
    TransactionTagDTO toDto(TransactionTag transactionTag);

    @Mapping(source = "corporateTransactionId", target = "corporateTransaction")
    TransactionTag toEntity(TransactionTagDTO transactionTagDTO);

    default TransactionTag fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionTag transactionTag = new TransactionTag();
        transactionTag.setId(id);
        return transactionTag;
    }
}
