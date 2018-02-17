package com.kian.corporatebanking.web.rest;

import com.kian.corporatebanking.CorporateBankingApp;

import com.kian.corporatebanking.domain.CorporateTransaction;
import com.kian.corporatebanking.repository.CorporateTransactionRepository;
import com.kian.corporatebanking.service.CorporateTransactionService;
import com.kian.corporatebanking.service.TransactionSignerService;
import com.kian.corporatebanking.service.TransactionTagService;
import com.kian.corporatebanking.service.dto.CorporateTransactionDTO;
import com.kian.corporatebanking.service.mapper.CorporateTransactionMapper;
import com.kian.corporatebanking.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.kian.corporatebanking.web.rest.TestUtil.sameInstant;
import static com.kian.corporatebanking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kian.corporatebanking.domain.enumeration.TransactionType;
import com.kian.corporatebanking.domain.enumeration.TransactionStatus;
/**
 * Test class for the CorporateTransactionResource REST controller.
 *
 * @see CorporateTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CorporateBankingApp.class)

@Commit
public class CorporateTransactionResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.RTGS;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.ACH;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Long DEFAULT_TRANSACTION_ID = 1L;
    private static final Long UPDATED_TRANSACTION_ID = 2L;

    private static final Boolean DEFAULT_DRAFT = false;
    private static final Boolean UPDATED_DRAFT = true;

    private static final Long DEFAULT_FROM_ACCOUNT_ID = 1L;
    private static final Long UPDATED_FROM_ACCOUNT_ID = 2L;

    private static final Long DEFAULT_TO_ACCOUNT_ID = 1L;
    private static final Long UPDATED_TO_ACCOUNT_ID = 2L;

    private static final String DEFAULT_TRACKING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_CODE = "BBBBBBBBBB";

    private static final TransactionStatus DEFAULT_STATUS = TransactionStatus.CREATE;
    private static final TransactionStatus UPDATED_STATUS = TransactionStatus.DONE;

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;

    @Autowired
    private CorporateTransactionRepository corporateTransactionRepository;

    @Autowired
    private CorporateTransactionMapper corporateTransactionMapper;

    @Autowired
    private CorporateTransactionService corporateTransactionService;
    @Autowired
    private TransactionSignerService transactionSignerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCorporateTransactionMockMvc;

    private CorporateTransaction corporateTransaction;
    private TransactionTagService transactionTagService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorporateTransactionResource corporateTransactionResource = new CorporateTransactionResource(corporateTransactionService, transactionSignerService, transactionTagService);
        this.restCorporateTransactionMockMvc = MockMvcBuilders.standaloneSetup(corporateTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorporateTransaction createEntity(EntityManager em) {
        CorporateTransaction corporateTransaction = new CorporateTransaction()
            .createDate(DEFAULT_CREATE_DATE)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .amount(DEFAULT_AMOUNT)
            .transactionId(DEFAULT_TRANSACTION_ID)
            .draft(DEFAULT_DRAFT)
            .fromAccountId(DEFAULT_FROM_ACCOUNT_ID)
            .toAccountId(DEFAULT_TO_ACCOUNT_ID)
            .trackingCode(DEFAULT_TRACKING_CODE)
            .status(DEFAULT_STATUS)
            .creatorId(DEFAULT_CREATOR_ID);
        return corporateTransaction;
    }

    @Before
    public void initTest() {
        corporateTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorporateTransaction() throws Exception {
        int databaseSizeBeforeCreate = corporateTransactionRepository.findAll().size();

        // Create the CorporateTransaction
        CorporateTransactionDTO corporateTransactionDTO = corporateTransactionMapper.toDto(corporateTransaction);
        restCorporateTransactionMockMvc.perform(post("/api/corporate-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporateTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the CorporateTransaction in the database
        List<CorporateTransaction> corporateTransactionList = corporateTransactionRepository.findAll();
        assertThat(corporateTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        CorporateTransaction testCorporateTransaction = corporateTransactionList.get(corporateTransactionList.size() - 1);
        assertThat(testCorporateTransaction.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCorporateTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testCorporateTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testCorporateTransaction.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testCorporateTransaction.isDraft()).isEqualTo(DEFAULT_DRAFT);
        assertThat(testCorporateTransaction.getFromAccountId()).isEqualTo(DEFAULT_FROM_ACCOUNT_ID);
        assertThat(testCorporateTransaction.getToAccountId()).isEqualTo(DEFAULT_TO_ACCOUNT_ID);
        assertThat(testCorporateTransaction.getTrackingCode()).isEqualTo(DEFAULT_TRACKING_CODE);
        assertThat(testCorporateTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCorporateTransaction.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    public void createCorporateTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = corporateTransactionRepository.findAll().size();

        // Create the CorporateTransaction with an existing ID
        corporateTransaction.setId(1L);
        CorporateTransactionDTO corporateTransactionDTO = corporateTransactionMapper.toDto(corporateTransaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorporateTransactionMockMvc.perform(post("/api/corporate-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporateTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CorporateTransaction in the database
        List<CorporateTransaction> corporateTransactionList = corporateTransactionRepository.findAll();
        assertThat(corporateTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCorporateTransactions() throws Exception {
        // Initialize the database
        corporateTransactionRepository.saveAndFlush(corporateTransaction);

        // Get all the corporateTransactionList
        restCorporateTransactionMockMvc.perform(get("/api/corporate-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corporateTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].draft").value(hasItem(DEFAULT_DRAFT.booleanValue())))
            .andExpect(jsonPath("$.[*].fromAccountId").value(hasItem(DEFAULT_FROM_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].toAccountId").value(hasItem(DEFAULT_TO_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].trackingCode").value(hasItem(DEFAULT_TRACKING_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID.intValue())));
    }

    @Test
    @Transactional
    public void getCorporateTransaction() throws Exception {
        // Initialize the database
        corporateTransactionRepository.saveAndFlush(corporateTransaction);

        // Get the corporateTransaction
        restCorporateTransactionMockMvc.perform(get("/api/corporate-transactions/{id}", corporateTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(corporateTransaction.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID.intValue()))
            .andExpect(jsonPath("$.draft").value(DEFAULT_DRAFT.booleanValue()))
            .andExpect(jsonPath("$.fromAccountId").value(DEFAULT_FROM_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.toAccountId").value(DEFAULT_TO_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.trackingCode").value(DEFAULT_TRACKING_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.creatorId").value(DEFAULT_CREATOR_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCorporateTransaction() throws Exception {
        // Get the corporateTransaction
        restCorporateTransactionMockMvc.perform(get("/api/corporate-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorporateTransaction() throws Exception {
        // Initialize the database
        corporateTransactionRepository.saveAndFlush(corporateTransaction);
        int databaseSizeBeforeUpdate = corporateTransactionRepository.findAll().size();

        // Update the corporateTransaction
        CorporateTransaction updatedCorporateTransaction = corporateTransactionRepository.findOne(corporateTransaction.getId());
        // Disconnect from session so that the updates on updatedCorporateTransaction are not directly saved in db
        em.detach(updatedCorporateTransaction);
        updatedCorporateTransaction
            .createDate(UPDATED_CREATE_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .amount(UPDATED_AMOUNT)
            .transactionId(UPDATED_TRANSACTION_ID)
            .draft(UPDATED_DRAFT)
            .fromAccountId(UPDATED_FROM_ACCOUNT_ID)
            .toAccountId(UPDATED_TO_ACCOUNT_ID)
            .trackingCode(UPDATED_TRACKING_CODE)
            .status(UPDATED_STATUS)
            .creatorId(UPDATED_CREATOR_ID);
        CorporateTransactionDTO corporateTransactionDTO = corporateTransactionMapper.toDto(updatedCorporateTransaction);

        restCorporateTransactionMockMvc.perform(put("/api/corporate-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporateTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the CorporateTransaction in the database
        List<CorporateTransaction> corporateTransactionList = corporateTransactionRepository.findAll();
        assertThat(corporateTransactionList).hasSize(databaseSizeBeforeUpdate);
        CorporateTransaction testCorporateTransaction = corporateTransactionList.get(corporateTransactionList.size() - 1);
        assertThat(testCorporateTransaction.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCorporateTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testCorporateTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCorporateTransaction.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testCorporateTransaction.isDraft()).isEqualTo(UPDATED_DRAFT);
        assertThat(testCorporateTransaction.getFromAccountId()).isEqualTo(UPDATED_FROM_ACCOUNT_ID);
        assertThat(testCorporateTransaction.getToAccountId()).isEqualTo(UPDATED_TO_ACCOUNT_ID);
        assertThat(testCorporateTransaction.getTrackingCode()).isEqualTo(UPDATED_TRACKING_CODE);
        assertThat(testCorporateTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCorporateTransaction.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingCorporateTransaction() throws Exception {
        int databaseSizeBeforeUpdate = corporateTransactionRepository.findAll().size();

        // Create the CorporateTransaction
        CorporateTransactionDTO corporateTransactionDTO = corporateTransactionMapper.toDto(corporateTransaction);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCorporateTransactionMockMvc.perform(put("/api/corporate-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(corporateTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the CorporateTransaction in the database
        List<CorporateTransaction> corporateTransactionList = corporateTransactionRepository.findAll();
        assertThat(corporateTransactionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCorporateTransaction() throws Exception {
        // Initialize the database
        corporateTransactionRepository.saveAndFlush(corporateTransaction);
        int databaseSizeBeforeDelete = corporateTransactionRepository.findAll().size();

        // Get the corporateTransaction
        restCorporateTransactionMockMvc.perform(delete("/api/corporate-transactions/{id}", corporateTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CorporateTransaction> corporateTransactionList = corporateTransactionRepository.findAll();
        assertThat(corporateTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorporateTransaction.class);
        CorporateTransaction corporateTransaction1 = new CorporateTransaction();
        corporateTransaction1.setId(1L);
        CorporateTransaction corporateTransaction2 = new CorporateTransaction();
        corporateTransaction2.setId(corporateTransaction1.getId());
        assertThat(corporateTransaction1).isEqualTo(corporateTransaction2);
        corporateTransaction2.setId(2L);
        assertThat(corporateTransaction1).isNotEqualTo(corporateTransaction2);
        corporateTransaction1.setId(null);
        assertThat(corporateTransaction1).isNotEqualTo(corporateTransaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorporateTransactionDTO.class);
        CorporateTransactionDTO corporateTransactionDTO1 = new CorporateTransactionDTO();
        corporateTransactionDTO1.setId(1L);
        CorporateTransactionDTO corporateTransactionDTO2 = new CorporateTransactionDTO();
        assertThat(corporateTransactionDTO1).isNotEqualTo(corporateTransactionDTO2);
        corporateTransactionDTO2.setId(corporateTransactionDTO1.getId());
        assertThat(corporateTransactionDTO1).isEqualTo(corporateTransactionDTO2);
        corporateTransactionDTO2.setId(2L);
        assertThat(corporateTransactionDTO1).isNotEqualTo(corporateTransactionDTO2);
        corporateTransactionDTO1.setId(null);
        assertThat(corporateTransactionDTO1).isNotEqualTo(corporateTransactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(corporateTransactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(corporateTransactionMapper.fromId(null)).isNull();
    }
}
