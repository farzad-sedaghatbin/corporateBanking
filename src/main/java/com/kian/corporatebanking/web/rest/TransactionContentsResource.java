package com.kian.corporatebanking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kian.corporatebanking.service.TransactionContentsService;
import com.kian.corporatebanking.web.rest.errors.BadRequestAlertException;
import com.kian.corporatebanking.web.rest.util.HeaderUtil;
import com.kian.corporatebanking.service.dto.TransactionContentsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TransactionContents.
 */
@RestController
@RequestMapping("/api")
public class TransactionContentsResource {

    private final Logger log = LoggerFactory.getLogger(TransactionContentsResource.class);

    private static final String ENTITY_NAME = "transactionContents";

    private final TransactionContentsService transactionContentsService;

    public TransactionContentsResource(TransactionContentsService transactionContentsService) {
        this.transactionContentsService = transactionContentsService;
    }

    /**
     * POST  /transaction-contents : Create a new transactionContents.
     *
     * @param transactionContentsDTO the transactionContentsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionContentsDTO, or with status 400 (Bad Request) if the transactionContents has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transaction-contents")
    @Timed
    public ResponseEntity<TransactionContentsDTO> createTransactionContents(@RequestBody TransactionContentsDTO transactionContentsDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionContents : {}", transactionContentsDTO);
        if (transactionContentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionContents cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionContentsDTO result = transactionContentsService.save(transactionContentsDTO);
        return ResponseEntity.created(new URI("/api/transaction-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transaction-contents : Updates an existing transactionContents.
     *
     * @param transactionContentsDTO the transactionContentsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionContentsDTO,
     * or with status 400 (Bad Request) if the transactionContentsDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionContentsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transaction-contents")
    @Timed
    public ResponseEntity<TransactionContentsDTO> updateTransactionContents(@RequestBody TransactionContentsDTO transactionContentsDTO) throws URISyntaxException {
        log.debug("REST request to update TransactionContents : {}", transactionContentsDTO);
        if (transactionContentsDTO.getId() == null) {
            return createTransactionContents(transactionContentsDTO);
        }
        TransactionContentsDTO result = transactionContentsService.save(transactionContentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionContentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-contents : get all the transactionContents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transactionContents in body
     */
    @GetMapping("/transaction-contents")
    @Timed
    public List<TransactionContentsDTO> getAllTransactionContents() {
        log.debug("REST request to get all TransactionContents");
        return transactionContentsService.findAll();
        }

    /**
     * GET  /transaction-contents/:id : get the "id" transactionContents.
     *
     * @param id the id of the transactionContentsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionContentsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transaction-contents/{id}")
    @Timed
    public ResponseEntity<TransactionContentsDTO> getTransactionContents(@PathVariable Long id) {
        log.debug("REST request to get TransactionContents : {}", id);
        TransactionContentsDTO transactionContentsDTO = transactionContentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionContentsDTO));
    }

    /**
     * DELETE  /transaction-contents/:id : delete the "id" transactionContents.
     *
     * @param id the id of the transactionContentsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transaction-contents/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactionContents(@PathVariable Long id) {
        log.debug("REST request to delete TransactionContents : {}", id);
        transactionContentsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
