package com.kian.corporatebanking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TransactionTag.
 */
@Entity
@Table(name = "transaction_tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_label")
    private String label;

    @Column(name = "party_id")
    private Long partyId;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "corporate_transaction_tags",
               joinColumns = @JoinColumn(name="corporate_transactions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="id"))
    private Set<CorporateTransaction> corporateTransactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public TransactionTag label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getPartyId() {
        return partyId;
    }

    public TransactionTag partyId(Long partyId) {
        this.partyId = partyId;
        return this;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Set<CorporateTransaction> getCorporateTransactions() {
        return corporateTransactions;
    }


    public void setCorporateTransactions(Set<CorporateTransaction> corporateTransactions) {
        this.corporateTransactions = corporateTransactions;
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
        TransactionTag transactionTag = (TransactionTag) o;
        if (transactionTag.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionTag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionTag{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", partyId=" + getPartyId() +
            "}";
    }
}
