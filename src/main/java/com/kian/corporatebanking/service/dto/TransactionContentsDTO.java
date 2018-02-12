package com.kian.corporatebanking.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the TransactionContents entity.
 */
public class TransactionContentsDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] content;
    private String contentContentType;

    private Long corporateTransactionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
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

        TransactionContentsDTO transactionContentsDTO = (TransactionContentsDTO) o;
        if(transactionContentsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionContentsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionContentsDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            "}";
    }
}
