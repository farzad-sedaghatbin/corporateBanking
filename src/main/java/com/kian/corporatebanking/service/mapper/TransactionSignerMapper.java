package com.kian.corporatebanking.service.mapper;

import com.kian.corporatebanking.domain.*;
import com.kian.corporatebanking.service.dto.TransactionSignerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransactionSigner and its DTO TransactionSignerDTO.
 */
@Mapper(componentModel = "spring", uses = {CorporateTransactionMapper.class})
public interface TransactionSignerMapper extends EntityMapper<TransactionSignerDTO, TransactionSigner> {

    @Mapping(source = "corporateTransaction.id", target = "corporateTransactionId")
    TransactionSignerDTO toDto(TransactionSigner transactionSigner);

    @Mapping(source = "corporateTransactionId", target = "corporateTransaction")
    @Mapping(target = "operations", ignore = true)
    TransactionSigner toEntity(TransactionSignerDTO transactionSignerDTO);

    default TransactionSigner fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionSigner transactionSigner = new TransactionSigner();
        transactionSigner.setId(id);
        return transactionSigner;
    }
}
