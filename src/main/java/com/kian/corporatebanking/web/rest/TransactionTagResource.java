package com.kian.corporatebanking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kian.corporatebanking.service.TransactionTagService;
import com.kian.corporatebanking.web.rest.errors.BadRequestAlertException;
import com.kian.corporatebanking.web.rest.util.HeaderUtil;
import com.kian.corporatebanking.web.rest.util.PaginationUtil;
import com.kian.corporatebanking.service.dto.TransactionTagDTO;
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
 * REST controller for managing TransactionTag.
 */
@RestController
@RequestMapping("/api")
public class TransactionTagResource {

    private final Logger log = LoggerFactory.getLogger(TransactionTagResource.class);

    private static final String ENTITY_NAME = "transactionTag";

    private final TransactionTagService transactionTagService;

    public TransactionTagResource(TransactionTagService transactionTagService) {
        this.transactionTagService = transactionTagService;
    }



    /**
     * POST  /transaction-tags : Create a new transactionTag.
     *
     * @param transactionTagDTO the transactionTagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionTagDTO, or with status 400 (Bad Request) if the transactionTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transaction-tags")
    @Timed
    public ResponseEntity<TransactionTagDTO> createTransactionTag(@RequestBody TransactionTagDTO transactionTagDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionTag : {}", transactionTagDTO);
        if (transactionTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionTagDTO result = transactionTagService.save(transactionTagDTO);
        return ResponseEntity.created(new URI("/api/transaction-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transaction-tags : Updates an existing transactionTag.
     *
     * @param transactionTagDTO the transactionTagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionTagDTO,
     * or with status 400 (Bad Request) if the transactionTagDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionTagDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transaction-tags")
    @Timed
    public ResponseEntity<TransactionTagDTO> updateTransactionTag(@RequestBody TransactionTagDTO transactionTagDTO) throws URISyntaxException {
        log.debug("REST request to update TransactionTag : {}", transactionTagDTO);
        if (transactionTagDTO.getId() == null) {
            return createTransactionTag(transactionTagDTO);
        }
        TransactionTagDTO result = transactionTagService.save(transactionTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-tags : get all the transactionTags.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transactionTags in body
     */
    @GetMapping("/transaction-tags")
    @Timed
    public ResponseEntity<List<TransactionTagDTO>> getAllTransactionTags(Pageable pageable) {
        log.debug("REST request to get a page of TransactionTags");
        Page<TransactionTagDTO> page = transactionTagService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transaction-tags");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transaction-tags/:id : get the "id" transactionTag.
     *
     * @param id the id of the transactionTagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionTagDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transaction-tags/{id}")
    @Timed
    public ResponseEntity<TransactionTagDTO> getTransactionTag(@PathVariable Long id) {
        log.debug("REST request to get TransactionTag : {}", id);
        TransactionTagDTO transactionTagDTO = transactionTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionTagDTO));
    }

    /**
     * DELETE  /transaction-tags/:id : delete the "id" transactionTag.
     *
     * @param id the id of the transactionTagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transaction-tags/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactionTag(@PathVariable Long id) {
        log.debug("REST request to delete TransactionTag : {}", id);
        transactionTagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
