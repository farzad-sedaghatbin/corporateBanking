package com.kian.corporatebanking.service.mapper;

import com.kian.corporatebanking.domain.*;
import com.kian.corporatebanking.service.dto.TransactionTagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransactionTag and its DTO TransactionTagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransactionTagMapper extends EntityMapper<TransactionTagDTO, TransactionTag> {



    default TransactionTag fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionTag transactionTag = new TransactionTag();
        transactionTag.setId(id);
        return transactionTag;
    }
}
