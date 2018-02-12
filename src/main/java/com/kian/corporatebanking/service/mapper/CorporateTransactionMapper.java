package com.kian.corporatebanking.service.mapper;

import com.kian.corporatebanking.domain.*;
import com.kian.corporatebanking.service.dto.CorporateTransactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CorporateTransaction and its DTO CorporateTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CorporateTransactionMapper extends EntityMapper<CorporateTransactionDTO, CorporateTransaction> {


    @Mapping(target = "contents", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "descriptions", ignore = true)
    @Mapping(target = "signers", ignore = true)
    CorporateTransaction toEntity(CorporateTransactionDTO corporateTransactionDTO);

    default CorporateTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        CorporateTransaction corporateTransaction = new CorporateTransaction();
        corporateTransaction.setId(id);
        return corporateTransaction;
    }
}
