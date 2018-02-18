package com.kian.corporatebanking.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.kian.corporatebanking.domain.enumeration.TransactionType;
import com.kian.corporatebanking.domain.enumeration.TransactionStatus;

/**
 * A DTO for the CorporateTransaction entity.
 */
public class CorporateTransactionDTO implements Serializable {

    private Long id;

    private ZonedDateTime createDate;

    private TransactionType transactionType;

    private BigDecimal amount;

    private Long transactionId;

    private Boolean draft;

    private Long fromAccountId;

    private Long toAccountId;

    private String trackingCode;

    private String content;

    private TransactionStatus status;

    private Long creatorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Boolean isDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CorporateTransactionDTO corporateTransactionDTO = (CorporateTransactionDTO) o;
        if(corporateTransactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), corporateTransactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CorporateTransactionDTO{" +
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
