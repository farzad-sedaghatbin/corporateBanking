package com.kian.corporatebanking.service.impl;

import com.kian.corporatebanking.domain.TransactionContents;
import com.kian.corporatebanking.domain.TransactionSigner;
import com.kian.corporatebanking.domain.enumeration.OperationType;
import com.kian.corporatebanking.domain.enumeration.RoleType;
import com.kian.corporatebanking.domain.enumeration.TransactionStatus;
import com.kian.corporatebanking.service.CorporateTransactionService;
import com.kian.corporatebanking.domain.CorporateTransaction;
import com.kian.corporatebanking.repository.CorporateTransactionRepository;
import com.kian.corporatebanking.service.TransactionContentsService;
import com.kian.corporatebanking.service.TransactionSignerService;
import com.kian.corporatebanking.service.dto.CorporateTransactionDTO;
import com.kian.corporatebanking.service.dto.TransactionContentsDTO;
import com.kian.corporatebanking.service.dto.TransactionSignerDTO;
import com.kian.corporatebanking.service.mapper.CorporateTransactionMapper;
import com.kian.corporatebanking.service.mapper.TransactionContentsMapper;
import com.kian.corporatebanking.service.mapper.TransactionSignerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;


/**
 * Service Implementation for managing CorporateTransaction.
 */
@Service
@Transactional
public class CorporateTransactionServiceImpl implements CorporateTransactionService {

    private final Logger log = LoggerFactory.getLogger(CorporateTransactionServiceImpl.class);

    private final CorporateTransactionRepository corporateTransactionRepository;

    private final CorporateTransactionMapper corporateTransactionMapper;
    private final TransactionContentsMapper transactionContentsMapper;
    private final TransactionSignerMapper transactionSignerMapper;
    private final TransactionContentsService transactionContentsService;
    private final TransactionSignerService transactionSignerService;


    public CorporateTransactionServiceImpl(CorporateTransactionRepository corporateTransactionRepository, CorporateTransactionMapper corporateTransactionMapper, TransactionContentsMapper transactionContentsMapper, TransactionSignerMapper transactionSignerMapper, TransactionContentsService transactionContentsService, TransactionSignerService transactionSignerService) {
        this.corporateTransactionRepository = corporateTransactionRepository;
        this.corporateTransactionMapper = corporateTransactionMapper;
        this.transactionContentsMapper = transactionContentsMapper;
        this.transactionSignerMapper = transactionSignerMapper;
        this.transactionContentsService = transactionContentsService;
        this.transactionSignerService = transactionSignerService;
    }

    /**
     * Save a corporateTransaction.
     *
     * @param corporateTransactionDTO the entity to save
     * @return the persisted entity`
     */
    @Override
    public CorporateTransactionDTO save(CorporateTransactionDTO corporateTransactionDTO) {
        log.debug("Request to save CorporateTransaction : {}", corporateTransactionDTO);
        corporateTransactionDTO.setStatus(TransactionStatus.REJECT);
        CorporateTransaction corporateTransaction = corporateTransactionMapper.toEntity(corporateTransactionDTO);
        corporateTransaction = corporateTransactionRepository.save(corporateTransaction);
        if (corporateTransactionDTO.getContent() != null) {
            TransactionContentsDTO transactionContentsDTO = new TransactionContentsDTO();
            transactionContentsDTO.setContent(Base64.getDecoder().decode(corporateTransactionDTO.getContent()));
            transactionContentsDTO.setCorporateTransactionId(corporateTransaction.getId());
            transactionContentsDTO = transactionContentsService.save(transactionContentsDTO);
            TransactionContents transactionContents = transactionContentsMapper.toEntity(transactionContentsDTO);

        }

        //todo : fetch relative signers
        if(!corporateTransactionDTO.isDraft() && corporateTransaction.getId()==null) {

            TransactionSignerDTO dto = new TransactionSignerDTO();
            dto.setPartId(1l);
            dto.setRoleType(RoleType.CHECKER);
            dto.setOperationType(OperationType.NOTHING);
            TransactionSignerDTO dto2 = new TransactionSignerDTO();
            dto2.setPartId(2l);
            dto2.setOperationType(OperationType.NOTHING);
            dto2.setRoleType(RoleType.CHECKER);
            TransactionSignerDTO dto3 = new TransactionSignerDTO();
            dto3.setPartId(3l);
            dto3.setRoleType(RoleType.MAKER);
            dto3.setOperationType(OperationType.APPROVE);
            List<TransactionSignerDTO> signerDTOS = new ArrayList<>();
            signerDTOS.add(dto);
            signerDTOS.add(dto2);
            signerDTOS.add(dto3);
            CorporateTransaction finalCorporateTransaction = corporateTransaction;
            signerDTOS.forEach(signer -> {
                signer.setCorporateTransactionId(finalCorporateTransaction.getId());
                transactionSignerService.save(signer);

            });
        }
        return corporateTransactionMapper.toDto(corporateTransaction);
    }

    /**
     * Get all the corporateTransactions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CorporateTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CorporateTransactions");
        return corporateTransactionRepository.findAll(pageable)
            .map(corporateTransactionMapper::toDto);
    }

    /**
     * Get one corporateTransaction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CorporateTransactionDTO findOne(Long id) {
        log.debug("Request to get CorporateTransaction : {}", id);
        CorporateTransaction corporateTransaction = corporateTransactionRepository.findOne(id);
        return corporateTransactionMapper.toDto(corporateTransaction);
    }

    /**
     * Delete the corporateTransaction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CorporateTransaction : {}", id);
        corporateTransactionRepository.delete(id);
    }

    @Override
    public void checkStatus(CorporateTransaction corporateTransaction) {
        Set<TransactionSignerDTO> signerList = transactionSignerService.findByCorporateTransaction(corporateTransaction);
        long rejectCount = signerList.stream().filter(signer -> signer.getOperationType().equals(OperationType.REJECT)).count();

        if (rejectCount > 0) {
            corporateTransaction.setStatus(TransactionStatus.REJECT);
            save(corporateTransactionMapper.toDto(corporateTransaction));
        } else {
            long signCount = signerList.stream().filter(signer -> signer.getOperationType().equals(OperationType.APPROVE) && signer.getRoleType().equals(RoleType.CHECKER)).count();
            if (signCount == signerList.size() - 1) {
                corporateTransaction.setStatus(TransactionStatus.READY);
                save(corporateTransactionMapper.toDto(corporateTransaction));

            }
        }
    }

    @Override
    public Set<CorporateTransactionDTO> findByCreatorIdAndFromAccountId(Long creatorId, Long fromAccountId) {
        return corporateTransactionMapper.toDto(corporateTransactionRepository.findByCreatorIdAndFromAccountId(creatorId, fromAccountId));
    }

    @Override
    public Set<CorporateTransactionDTO> findByToAccountId(Long toAccountId) {
        return corporateTransactionMapper.toDto(corporateTransactionRepository.findByToAccountId(toAccountId));
    }
}
