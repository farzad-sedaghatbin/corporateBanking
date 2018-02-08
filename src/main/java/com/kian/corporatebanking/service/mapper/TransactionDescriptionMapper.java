package com.kian.corporatebanking.service.mapper;

import com.kian.corporatebanking.domain.*;
import com.kian.corporatebanking.service.dto.TransactionDescriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransactionDescription and its DTO TransactionDescriptionDTO.
 */
@Mapper(componentModel = "spring", uses = {CorporateTransactionMapper.class})
public interface TransactionDescriptionMapper extends EntityMapper<TransactionDescriptionDTO, TransactionDescription> {

    @Mapping(source = "corporateTransaction.id", target = "corporateTransactionId")
    TransactionDescriptionDTO toDto(TransactionDescription transactionDescription);

    @Mapping(source = "corporateTransactionId", target = "corporateTransaction")
    TransactionDescription toEntity(TransactionDescriptionDTO transactionDescriptionDTO);

    default TransactionDescription fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionDescription transactionDescription = new TransactionDescription();
        transactionDescription.setId(id);
        return transactionDescription;
    }
}
