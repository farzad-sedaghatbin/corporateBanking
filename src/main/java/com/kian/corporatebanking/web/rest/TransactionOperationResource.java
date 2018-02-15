package com.kian.corporatebanking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kian.corporatebanking.domain.enumeration.OperationType;
import com.kian.corporatebanking.service.TransactionOperationService;
import com.kian.corporatebanking.service.dto.TransactionSignerDTO;
import com.kian.corporatebanking.web.rest.errors.BadRequestAlertException;
import com.kian.corporatebanking.web.rest.util.HeaderUtil;
import com.kian.corporatebanking.web.rest.util.PaginationUtil;
import com.kian.corporatebanking.service.dto.TransactionOperationDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TransactionOperation.
 */
@RestController
@RequestMapping("/api")
public class TransactionOperationResource {

    private final Logger log = LoggerFactory.getLogger(TransactionOperationResource.class);

    private static final String ENTITY_NAME = "transactionOperation";

    private final TransactionOperationService transactionOperationService;

    public TransactionOperationResource(TransactionOperationService transactionOperationService) {
        this.transactionOperationService = transactionOperationService;
    }

    /**
     * POST  /transaction-operations : Create a new transactionOperation.
     *
     * @param transactionOperationDTO the transactionOperationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionOperationDTO, or with status 400 (Bad Request) if the transactionOperation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transaction-operations")
    @Timed
    public ResponseEntity<TransactionOperationDTO> createTransactionOperation(@RequestBody TransactionOperationDTO transactionOperationDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionOperation : {}", transactionOperationDTO);
        if (transactionOperationDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionOperation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionOperationDTO result = transactionOperationService.save(transactionOperationDTO);
        return ResponseEntity.created(new URI("/api/transaction-operations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/approved")
    @Timed
    public ResponseEntity<Void> createTransactionSigner(@RequestBody TransactionOperationDTO transactionOperationDTO) throws URISyntaxException {
        log.debug("REST request to Approved TransactionSigner : {}", transactionOperationDTO);
        //todo fetch signer record from token
        transactionOperationDTO.setOperationDate(ZonedDateTime.now());
        transactionOperationDTO.setOperationType(OperationType.APPROVE);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, transactionOperationDTO.getId().toString())).build();
    }
    /**
     * PUT  /transaction-operations : Updates an existing transactionOperation.
     *
     * @param transactionOperationDTO the transactionOperationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionOperationDTO,
     * or with status 400 (Bad Request) if the transactionOperationDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionOperationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transaction-operations")
    @Timed
    public ResponseEntity<TransactionOperationDTO> updateTransactionOperation(@RequestBody TransactionOperationDTO transactionOperationDTO) throws URISyntaxException {
        log.debug("REST request to update TransactionOperation : {}", transactionOperationDTO);
        if (transactionOperationDTO.getId() == null) {
            return createTransactionOperation(transactionOperationDTO);
        }
        TransactionOperationDTO result = transactionOperationService.save(transactionOperationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionOperationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-operations : get all the transactionOperations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transactionOperations in body
     */
    @GetMapping("/transaction-operations")
    @Timed
    public ResponseEntity<List<TransactionOperationDTO>> getAllTransactionOperations(Pageable pageable) {
        log.debug("REST request to get a page of TransactionOperations");
        Page<TransactionOperationDTO> page = transactionOperationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transaction-operations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transaction-operations/:id : get the "id" transactionOperation.
     *
     * @param id the id of the transactionOperationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionOperationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transaction-operations/{id}")
    @Timed
    public ResponseEntity<TransactionOperationDTO> getTransactionOperation(@PathVariable Long id) {
        log.debug("REST request to get TransactionOperation : {}", id);
        TransactionOperationDTO transactionOperationDTO = transactionOperationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionOperationDTO));
    }

    /**
     * DELETE  /transaction-operations/:id : delete the "id" transactionOperation.
     *
     * @param id the id of the transactionOperationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transaction-operations/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactionOperation(@PathVariable Long id) {
        log.debug("REST request to delete TransactionOperation : {}", id);
        transactionOperationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
