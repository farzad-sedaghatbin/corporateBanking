package com.kian.corporatebanking.service.mapper;

import com.kian.corporatebanking.domain.*;
import com.kian.corporatebanking.service.dto.TransactionOperationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransactionOperation and its DTO TransactionOperationDTO.
 */
@Mapper(componentModel = "spring", uses = {TransactionSignerMapper.class})
public interface TransactionOperationMapper extends EntityMapper<TransactionOperationDTO, TransactionOperation> {

    @Mapping(source = "transactionSigner.id", target = "transactionSignerId")
    TransactionOperationDTO toDto(TransactionOperation transactionOperation);

    @Mapping(source = "transactionSignerId", target = "transactionSigner")
    TransactionOperation toEntity(TransactionOperationDTO transactionOperationDTO);

    default TransactionOperation fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionOperation transactionOperation = new TransactionOperation();
        transactionOperation.setId(id);
        return transactionOperation;
    }
}
