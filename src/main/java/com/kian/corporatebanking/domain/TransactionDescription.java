package com.kian.corporatebanking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A TransactionDescription.
 */
@Entity
@Table(name = "transaction_description")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "lable")
    private String lable;

    @Column(name = "party_id")
    private Long partyId;

    @ManyToOne
    private CorporateTransaction corporateTransaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLable() {
        return lable;
    }

    public TransactionDescription lable(String lable) {
        this.lable = lable;
        return this;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public Long getPartyId() {
        return partyId;
    }

    public TransactionDescription partyId(Long partyId) {
        this.partyId = partyId;
        return this;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public CorporateTransaction getCorporateTransaction() {
        return corporateTransaction;
    }

    public TransactionDescription corporateTransaction(CorporateTransaction corporateTransaction) {
        this.corporateTransaction = corporateTransaction;
        return this;
    }

    public void setCorporateTransaction(CorporateTransaction corporateTransaction) {
        this.corporateTransaction = corporateTransaction;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionDescription transactionDescription = (TransactionDescription) o;
        if (transactionDescription.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionDescription.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionDescription{" +
            "id=" + getId() +
            ", lable='" + getLable() + "'" +
            ", partyId=" + getPartyId() +
            "}";
    }
}
