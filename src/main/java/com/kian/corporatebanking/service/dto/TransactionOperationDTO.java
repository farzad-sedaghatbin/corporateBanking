package com.kian.corporatebanking.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kian.corporatebanking.domain.enumeration.OperationType;

/**
 * A DTO for the TransactionOperation entity.
 */
public class TransactionOperationDTO implements Serializable {


    public static void main(String[] args) {
        TransactionOperationDTO transactionOperationDTO= new TransactionOperationDTO();
        transactionOperationDTO.setOperationType(OperationType.APPROVE);
        transactionOperationDTO.setComment("hello");
        transactionOperationDTO.setTransactionSignerId(1l);
        try {
            System.out.println(new ObjectMapper().writeValueAsString(transactionOperationDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private Long id;

    private ZonedDateTime operationDate;

    private OperationType operationType;

    private String comment;

    private Long transactionSignerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(ZonedDateTime operationDate) {
        this.operationDate = operationDate;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getTransactionSignerId() {
        return transactionSignerId;
    }

    public void setTransactionSignerId(Long transactionSignerId) {
        this.transactionSignerId = transactionSignerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionOperationDTO transactionOperationDTO = (TransactionOperationDTO) o;
        if(transactionOperationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionOperationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionOperationDTO{" +
            "id=" + getId() +
            ", operationDate='" + getOperationDate() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
