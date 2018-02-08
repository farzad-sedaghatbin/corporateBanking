package com.kian.corporatebanking.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TransactionSigner entity.
 */
public class TransactionSignerDTO implements Serializable {

    private Long id;

    private Integer signOrder;

    private Long partId;

    private Long corporateTransactionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSignOrder() {
        return signOrder;
    }

    public void setSignOrder(Integer signOrder) {
        this.signOrder = signOrder;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
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

        TransactionSignerDTO transactionSignerDTO = (TransactionSignerDTO) o;
        if(transactionSignerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionSignerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionSignerDTO{" +
            "id=" + getId() +
            ", signOrder=" + getSignOrder() +
            ", partId=" + getPartId() +
            "}";
    }
}
