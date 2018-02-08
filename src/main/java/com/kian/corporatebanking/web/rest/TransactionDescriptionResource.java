package com.kian.corporatebanking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kian.corporatebanking.service.TransactionDescriptionService;
import com.kian.corporatebanking.web.rest.errors.BadRequestAlertException;
import com.kian.corporatebanking.web.rest.util.HeaderUtil;
import com.kian.corporatebanking.web.rest.util.PaginationUtil;
import com.kian.corporatebanking.service.dto.TransactionDescriptionDTO;
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
 * REST controller for managing TransactionDescription.
 */
@RestController
@RequestMapping("/api")
public class TransactionDescriptionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionDescriptionResource.class);

    private static final String ENTITY_NAME = "transactionDescription";

    private final TransactionDescriptionService transactionDescriptionService;

    public TransactionDescriptionResource(TransactionDescriptionService transactionDescriptionService) {
        this.transactionDescriptionService = transactionDescriptionService;
    }

    /**
     * POST  /transaction-descriptions : Create a new transactionDescription.
     *
     * @param transactionDescriptionDTO the transactionDescriptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionDescriptionDTO, or with status 400 (Bad Request) if the transactionDescription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transaction-descriptions")
    @Timed
    public ResponseEntity<TransactionDescriptionDTO> createTransactionDescription(@RequestBody TransactionDescriptionDTO transactionDescriptionDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionDescription : {}", transactionDescriptionDTO);
        if (transactionDescriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionDescription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionDescriptionDTO result = transactionDescriptionService.save(transactionDescriptionDTO);
        return ResponseEntity.created(new URI("/api/transaction-descriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transaction-descriptions : Updates an existing transactionDescription.
     *
     * @param transactionDescriptionDTO the transactionDescriptionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionDescriptionDTO,
     * or with status 400 (Bad Request) if the transactionDescriptionDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionDescriptionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transaction-descriptions")
    @Timed
    public ResponseEntity<TransactionDescriptionDTO> updateTransactionDescription(@RequestBody TransactionDescriptionDTO transactionDescriptionDTO) throws URISyntaxException {
        log.debug("REST request to update TransactionDescription : {}", transactionDescriptionDTO);
        if (transactionDescriptionDTO.getId() == null) {
            return createTransactionDescription(transactionDescriptionDTO);
        }
        TransactionDescriptionDTO result = transactionDescriptionService.save(transactionDescriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionDescriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-descriptions : get all the transactionDescriptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transactionDescriptions in body
     */
    @GetMapping("/transaction-descriptions")
    @Timed
    public ResponseEntity<List<TransactionDescriptionDTO>> getAllTransactionDescriptions(Pageable pageable) {
        log.debug("REST request to get a page of TransactionDescriptions");
        Page<TransactionDescriptionDTO> page = transactionDescriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transaction-descriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transaction-descriptions/:id : get the "id" transactionDescription.
     *
     * @param id the id of the transactionDescriptionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionDescriptionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transaction-descriptions/{id}")
    @Timed
    public ResponseEntity<TransactionDescriptionDTO> getTransactionDescription(@PathVariable Long id) {
        log.debug("REST request to get TransactionDescription : {}", id);
        TransactionDescriptionDTO transactionDescriptionDTO = transactionDescriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionDescriptionDTO));
    }

    /**
     * DELETE  /transaction-descriptions/:id : delete the "id" transactionDescription.
     *
     * @param id the id of the transactionDescriptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transaction-descriptions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactionDescription(@PathVariable Long id) {
        log.debug("REST request to delete TransactionDescription : {}", id);
        transactionDescriptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
