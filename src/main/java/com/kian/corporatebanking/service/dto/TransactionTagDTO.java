package com.kian.corporatebanking.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TransactionTag entity.
 */
public class TransactionTagDTO implements Serializable {

    private Long id;

    private String lable;

    private Long partyId;

    private Long corporateTransactionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Long getCorporateTransactionId() {
        return corporateTransactionId;
    }

    public void setCorporateTransactionId(Long corporateTransactionId) {
        this.corporateTransactionId = corporateTransactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionTagDTO transactionTagDTO = (TransactionTagDTO) o;
        if(transactionTagDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionTagDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionTagDTO{" +
            "id=" + getId() +
            ", lable='" + getLable() + "'" +
            ", partyId=" + getPartyId() +
            "}";
    }
}
