package com.kian.corporatebanking.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DetailDTO implements Serializable {

    private CorporateTransactionDTO corporateTransactionDTO;
    private Set<TransactionSignerDTO> signerList;

    public CorporateTransactionDTO getCorporateTransactionDTO() {
        return corporateTransactionDTO;
    }

    public void setCorporateTransactionDTO(CorporateTransactionDTO corporateTransactionDTO) {
        this.corporateTransactionDTO = corporateTransactionDTO;
    }

    public Set<TransactionSignerDTO> getSignerList() {
        return signerList;
    }

    public void setSignerList(Set<TransactionSignerDTO> signerList) {
        this.signerList = signerList;
    }
}
