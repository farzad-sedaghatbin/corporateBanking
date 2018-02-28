package com.kian.corporatebanking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kian.corporatebanking.service.TransactionSignerService;
import com.kian.corporatebanking.web.rest.errors.BadRequestAlertException;
import com.kian.corporatebanking.web.rest.util.HeaderUtil;
import com.kian.corporatebanking.web.rest.util.PaginationUtil;
import com.kian.corporatebanking.service.dto.TransactionSignerDTO;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TransactionSigner.
 */
@RestController
@RequestMapping("/api")
public class TransactionSignerResource {

    private final Logger log = LoggerFactory.getLogger(TransactionSignerResource.class);

    private static final String ENTITY_NAME = "transactionSigner";

    private final TransactionSignerService transactionSignerService;

    public TransactionSignerResource(TransactionSignerService transactionSignerService) {
        this.transactionSignerService = transactionSignerService;
    }

    /**
     * POST  /transaction-signers : Create a new transactionSigner.
     *
     * @param transactionSignerDTO the transactionSignerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionSignerDTO, or with status 400 (Bad Request) if the transactionSigner has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transaction-signers")
    @Timed
    public ResponseEntity<TransactionSignerDTO> createTransactionSigner(@RequestBody TransactionSignerDTO transactionSignerDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionSigner : {}", transactionSignerDTO);
        if (transactionSignerDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionSigner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionSignerDTO result = transactionSignerService.save(transactionSignerDTO);
        return ResponseEntity.created(new URI("/api/transaction-signers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * PUT  /transaction-signers : Updates an existing transactionSigner.
     *
     * @param transactionSignerDTO the transactionSignerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionSignerDTO,
     * or with status 400 (Bad Request) if the transactionSignerDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionSignerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transaction-signers")
    @Timed
    public ResponseEntity<TransactionSignerDTO> updateTransactionSigner(@RequestBody TransactionSignerDTO transactionSignerDTO) throws URISyntaxException {
        log.debug("REST request to update TransactionSigner : {}", transactionSignerDTO);
        if (transactionSignerDTO.getId() == null) {
            return createTransactionSigner(transactionSignerDTO);
        }
        TransactionSignerDTO result = transactionSignerService.save(transactionSignerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionSignerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-signers : get all the transactionSigners.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transactionSigners in body
     */
    @GetMapping("/transaction-signers")
    @Timed
    public ResponseEntity<List<TransactionSignerDTO>> getAllTransactionSigners(Pageable pageable) {
        log.debug("REST request to get a page of TransactionSigners");
        Page<TransactionSignerDTO> page = transactionSignerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transaction-signers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transaction-signers/:id : get the "id" transactionSigner.
     *
     * @param id the id of the transactionSignerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionSignerDTO, or with status 404 (Not Found)
     */

    /**
     * DELETE  /transaction-signers/:id : delete the "id" transactionSigner.
     *
     * @param id the id of the transactionSignerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transaction-signers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactionSigner(@PathVariable Long id) {
        log.debug("REST request to delete TransactionSigner : {}", id);
        transactionSignerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
