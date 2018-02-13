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

    private String label;

    private Long partyId;

    private Set<CorporateTransactionDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Set<CorporateTransactionDTO> getTags() {
        return tags;
    }

    public void setTags(Set<CorporateTransactionDTO> corporateTransactions) {
        this.tags = corporateTransactions;
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
            ", label='" + getLabel() + "'" +
            ", partyId=" + getPartyId() +
            "}";
    }
}
