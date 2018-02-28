package com.kian.corporatebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.kian.corporatebanking.domain.enumeration.TransactionType;

import com.kian.corporatebanking.domain.enumeration.TransactionStatus;

/**
 * A CorporateTransaction.
 */
@Entity
@Table(name = "corporate_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CorporateTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "amount", precision=10, scale=2)
    private BigDecimal amount;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "draft")
    private Boolean draft;

    @Column(name = "from_account_id")
    private Long fromAccountId;

    @Column(name = "to_account_id")
    private Long toAccountId;

    @Column(name = "tracking_code")
    private String trackingCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "attachment_path")
    private String attachmentPath;

    @OneToMany(mappedBy = "corporateTransaction")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TransactionContents> contents = new HashSet<>();

    @ManyToOne()
    private TransactionDescription descriptions;

    @OneToMany(mappedBy = "corporateTransaction")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TransactionSigner> signers = new HashSet<>();


    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public CorporateTransaction createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public CorporateTransaction transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CorporateTransaction amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public CorporateTransaction transactionId(Long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Boolean isDraft() {
        return draft;
    }

    public CorporateTransaction draft(Boolean draft) {
        this.draft = draft;
        return this;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public CorporateTransaction fromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
        return this;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public CorporateTransaction toAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
        return this;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public CorporateTransaction trackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
        return this;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public CorporateTransaction status(TransactionStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public CorporateTransaction creatorId(Long creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Set<TransactionContents> getContents() {
        return contents;
    }

    public CorporateTransaction contents(Set<TransactionContents> transactionContents) {
        this.contents = transactionContents;
        return this;
    }

    public CorporateTransaction addContent(TransactionContents transactionContents) {
        this.contents.add(transactionContents);
        transactionContents.setCorporateTransaction(this);
        return this;
    }

    public CorporateTransaction removeContent(TransactionContents transactionContents) {
        this.contents.remove(transactionContents);
        transactionContents.setCorporateTransaction(null);
        return this;
    }

    public void setContents(Set<TransactionContents> transactionContents) {
        this.contents = transactionContents;
    }


    public TransactionDescription getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(TransactionDescription descriptions) {
        this.descriptions = descriptions;
    }

    public Set<TransactionSigner> getSigners() {
        return signers;
    }

    public CorporateTransaction signers(Set<TransactionSigner> transactionSigners) {
        this.signers = transactionSigners;
        return this;
    }

    public CorporateTransaction addSigners(TransactionSigner transactionSigner) {
        this.signers.add(transactionSigner);
        transactionSigner.setCorporateTransaction(this);
        return this;
    }

    public CorporateTransaction removeSigners(TransactionSigner transactionSigner) {
        this.signers.remove(transactionSigner);
        transactionSigner.setCorporateTransaction(null);
        return this;
    }

    public void setSigners(Set<TransactionSigner> transactionSigners) {
        this.signers = transactionSigners;
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
        CorporateTransaction corporateTransaction = (CorporateTransaction) o;
        if (corporateTransaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), corporateTransaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CorporateTransaction{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", amount=" + getAmount() +
            ", transactionId=" + getTransactionId() +
            ", draft='" + isDraft() + "'" +
            ", fromAccountId=" + getFromAccountId() +
            ", toAccountId=" + getToAccountId() +
            ", trackingCode='" + getTrackingCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", creatorId=" + getCreatorId() +
            "}";
    }
}
