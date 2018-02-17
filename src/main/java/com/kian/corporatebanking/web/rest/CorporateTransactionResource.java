package com.kian.corporatebanking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kian.corporatebanking.domain.enumeration.TransactionStatus;
import com.kian.corporatebanking.service.CorporateTransactionService;
import com.kian.corporatebanking.service.TransactionSignerService;
import com.kian.corporatebanking.service.TransactionTagService;
import com.kian.corporatebanking.service.dto.DashboardDTO;
import com.kian.corporatebanking.web.rest.errors.BadRequestAlertException;
import com.kian.corporatebanking.web.rest.util.HeaderUtil;
import com.kian.corporatebanking.service.dto.CorporateTransactionDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing CorporateTransaction.
 */
@RestController
@RequestMapping("/api")
public class CorporateTransactionResource {

    private final Logger log = LoggerFactory.getLogger(CorporateTransactionResource.class);

    private static final String ENTITY_NAME = "corporateTransaction";

    private final CorporateTransactionService corporateTransactionService;
    private final TransactionSignerService transactionSignerService;
    private final TransactionTagService transactionTagService;

    public CorporateTransactionResource(CorporateTransactionService corporateTransactionService, TransactionSignerService transactionSignerService, TransactionTagService transactionTagService) {
        this.corporateTransactionService = corporateTransactionService;
        this.transactionSignerService = transactionSignerService;
        this.transactionTagService = transactionTagService;
    }

    /**
     * POST  /corporate-transactions : Create a new corporateTransaction.
     *
     * @param corporateTransactionDTO the corporateTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new corporateTransactionDTO, or with status 400 (Bad Request) if the corporateTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/corporate-transactions")
    @Timed
    public ResponseEntity<CorporateTransactionDTO> createCorporateTransaction(@RequestBody CorporateTransactionDTO corporateTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save CorporateTransaction : {}", corporateTransactionDTO);
        if (corporateTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new corporateTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CorporateTransactionDTO result = corporateTransactionService.save(corporateTransactionDTO);
        return ResponseEntity.created(new URI("/api/corporate-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/execute-corporate-transactions")
    @Timed
    //todo change signature
    public ResponseEntity<String> executeCorporateTransaction(@RequestBody Long id) throws URISyntaxException {
        log.debug("REST request to execute CorporateTransaction : {}", id);
        //todo BankingAPI
        CorporateTransactionDTO result = corporateTransactionService.findOne(id);
        result.setStatus(TransactionStatus.DONE);
        corporateTransactionService.save(result);
        return ResponseEntity.ok("200");
    }

    /**
     * PUT  /corporate-transactions : Updates an existing corporateTransaction.
     *
     * @param corporateTransactionDTO the corporateTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated corporateTransactionDTO,
     * or with status 400 (Bad Request) if the corporateTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the corporateTransactionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/corporate-transactions")
    @Timed
    public ResponseEntity<CorporateTransactionDTO> updateCorporateTransaction(@RequestBody CorporateTransactionDTO corporateTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update CorporateTransaction : {}", corporateTransactionDTO);
        if (corporateTransactionDTO.getId() == null) {
            return createCorporateTransaction(corporateTransactionDTO);
        }
        CorporateTransactionDTO result = corporateTransactionService.save(corporateTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, corporateTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /corporate-transactions : get all the corporateTransactions.
     *
     * @param id the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of corporateTransactions in body
     */
    @GetMapping("/corporate-transactions/{id}")
    @Timed
    public ResponseEntity<List<CorporateTransactionDTO>> getAllCorporateTransactions(@PathVariable Long id) {
        log.debug("REST request to get a page of CorporateTransactions");
        //todo find party from token
        List<CorporateTransactionDTO> corporateTransactionDTOS = corporateTransactionService.findByCreatorIdAndFromAccountId(1l, id);
        if (corporateTransactionDTOS == null) {
            corporateTransactionDTOS = new ArrayList<>();
        }
        corporateTransactionDTOS.addAll(transactionSignerService.getAllCorporateTransactionByPartyId(1l));
        //todo change return type pageable
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setReadyList(corporateTransactionDTOS.stream().filter(dto -> dto.getStatus().equals(TransactionStatus.READY)).collect(Collectors.toList()));
        //todo change DTO and find mine an others
        dashboardDTO.setMineList(corporateTransactionDTOS.stream().filter(dto -> !dto.getStatus().equals(TransactionStatus.CREATE)).collect(Collectors.toList()));
        dashboardDTO.setOtherList(corporateTransactionDTOS.stream().filter(dto -> !dto.getStatus().equals(TransactionStatus.CREATE)).collect(Collectors.toList()));

        return ResponseEntity.ok(corporateTransactionDTOS);
    }


    @GetMapping("/corporate-transactions-by-tags/{label}")
    @Timed
    public ResponseEntity<Set<CorporateTransactionDTO>> allCorporateTransactionsByTag(@PathVariable String label) {
        log.debug("REST request to get a page of CorporateTransactions");
        //todo find party from token
        Set<CorporateTransactionDTO> corporateTransactionDTOS = transactionTagService.findByPartyIdAndLabel(1l, label).getTags();
        if (corporateTransactionDTOS == null) {
            corporateTransactionDTOS = new HashSet<>();
        }
        return ResponseEntity.ok(corporateTransactionDTOS);
    }
   @GetMapping("/corporate-transactions-by-party/{contactId}")
    @Timed
    public ResponseEntity<List<CorporateTransactionDTO>> allCorporateTransactionsByToParty(@PathVariable Long contactId) {
        log.debug("REST request to get a page of CorporateTransactions");
        //todo find party from token find contact from contact
        List<CorporateTransactionDTO> corporateTransactionDTOS = corporateTransactionService.findByToAccountId(contactId);
        if (corporateTransactionDTOS == null) {
            corporateTransactionDTOS = new ArrayList<>();
        }
        return ResponseEntity.ok(corporateTransactionDTOS);
    }

    /**
     * GET  /corporate-transactions/:id : get the "id" corporateTransaction.
     *
     * @param id the id of the corporateTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the corporateTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/detail-corporate-transactions/{id}")
    @Timed
    public ResponseEntity<CorporateTransactionDTO> getCorporateTransaction(@PathVariable Long id) {
        log.debug("REST request to get CorporateTransaction : {}", id);
        CorporateTransactionDTO corporateTransactionDTO = corporateTransactionService.findOne(id);
        //todo fetch eager for toAccount from contact
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(corporateTransactionDTO));
    }

    /**
     * DELETE  /corporate-transactions/:id : delete the "id" corporateTransaction.
     *
     * @param id the id of the corporateTransactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/corporate-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteCorporateTransaction(@PathVariable Long id) {
        log.debug("REST request to delete CorporateTransaction : {}", id);
        corporateTransactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
