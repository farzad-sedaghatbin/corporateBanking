package com.kian.corporatebanking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.kian.corporatebanking.domain.enumeration.OperationType;

/**
 * A TransactionOperation.
 */
@Entity
@Table(name = "transaction_operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "operation_date")
    private ZonedDateTime operationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private OperationType operationType;

    @Column(name = "jhi_comment")
    private String comment;

    @ManyToOne
    private TransactionSigner transactionSigner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getOperationDate() {
        return operationDate;
    }

    public TransactionOperation operationDate(ZonedDateTime operationDate) {
        this.operationDate = operationDate;
        return this;
    }

    public void setOperationDate(ZonedDateTime operationDate) {
        this.operationDate = operationDate;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public TransactionOperation operationType(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getComment() {
        return comment;
    }

    public TransactionOperation comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TransactionSigner getTransactionSigner() {
        return transactionSigner;
    }

    public TransactionOperation transactionSigner(TransactionSigner transactionSigner) {
        this.transactionSigner = transactionSigner;
        return this;
    }

    public void setTransactionSigner(TransactionSigner transactionSigner) {
        this.transactionSigner = transactionSigner;
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
        TransactionOperation transactionOperation = (TransactionOperation) o;
        if (transactionOperation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionOperation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionOperation{" +
            "id=" + getId() +
            ", operationDate='" + getOperationDate() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
