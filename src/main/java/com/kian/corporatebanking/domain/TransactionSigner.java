package com.kian.corporatebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.kian.corporatebanking.domain.enumeration.RoleType;

/**
 * A TransactionSigner.
 */
@Entity
@Table(name = "transaction_signer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionSigner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "sign_order")
    private Integer signOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RoleType roleType;

    @Column(name = "part_id")
    private Long partId;

    @ManyToOne
    private CorporateTransaction corporateTransaction;

    @OneToMany(mappedBy = "transactionSigner")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TransactionOperation> operations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSignOrder() {
        return signOrder;
    }

    public TransactionSigner signOrder(Integer signOrder) {
        this.signOrder = signOrder;
        return this;
    }

    public void setSignOrder(Integer signOrder) {
        this.signOrder = signOrder;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public TransactionSigner roleType(RoleType roleType) {
        this.roleType = roleType;
        return this;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Long getPartId() {
        return partId;
    }

    public TransactionSigner partId(Long partId) {
        this.partId = partId;
        return this;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public CorporateTransaction getCorporateTransaction() {
        return corporateTransaction;
    }

    public TransactionSigner corporateTransaction(CorporateTransaction corporateTransaction) {
        this.corporateTransaction = corporateTransaction;
        return this;
    }

    public void setCorporateTransaction(CorporateTransaction corporateTransaction) {
        this.corporateTransaction = corporateTransaction;
    }

    public Set<TransactionOperation> getOperations() {
        return operations;
    }

    public TransactionSigner operations(Set<TransactionOperation> transactionOperations) {
        this.operations = transactionOperations;
        return this;
    }

    public TransactionSigner addOperations(TransactionOperation transactionOperation) {
        this.operations.add(transactionOperation);
        transactionOperation.setTransactionSigner(this);
        return this;
    }

    public TransactionSigner removeOperations(TransactionOperation transactionOperation) {
        this.operations.remove(transactionOperation);
        transactionOperation.setTransactionSigner(null);
        return this;
    }

    public void setOperations(Set<TransactionOperation> transactionOperations) {
        this.operations = transactionOperations;
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
        TransactionSigner transactionSigner = (TransactionSigner) o;
        if (transactionSigner.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionSigner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionSigner{" +
            "id=" + getId() +
            ", signOrder=" + getSignOrder() +
            ", roleType='" + getRoleType() + "'" +
            ", partId=" + getPartId() +
            "}";
    }
}
