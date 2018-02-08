package com.kian.corporatebanking.web.rest;

import com.kian.corporatebanking.CorporateBankingApp;

import com.kian.corporatebanking.domain.TransactionOperation;
import com.kian.corporatebanking.repository.TransactionOperationRepository;
import com.kian.corporatebanking.service.TransactionOperationService;
import com.kian.corporatebanking.service.dto.TransactionOperationDTO;
import com.kian.corporatebanking.service.mapper.TransactionOperationMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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

import com.kian.corporatebanking.domain.enumeration.OperationType;
/**
 * Test class for the TransactionOperationResource REST controller.
 *
 * @see TransactionOperationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CorporateBankingApp.class)
public class TransactionOperationResourceIntTest {

    private static final ZonedDateTime DEFAULT_OPERATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_OPERATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final OperationType DEFAULT_OPERATION_TYPE = OperationType.REJECT;
    private static final OperationType UPDATED_OPERATION_TYPE = OperationType.APPROVE;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private TransactionOperationRepository transactionOperationRepository;

    @Autowired
    private TransactionOperationMapper transactionOperationMapper;

    @Autowired
    private TransactionOperationService transactionOperationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransactionOperationMockMvc;

    private TransactionOperation transactionOperation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionOperationResource transactionOperationResource = new TransactionOperationResource(transactionOperationService);
        this.restTransactionOperationMockMvc = MockMvcBuilders.standaloneSetup(transactionOperationResource)
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
    public static TransactionOperation createEntity(EntityManager em) {
        TransactionOperation transactionOperation = new TransactionOperation()
            .operationDate(DEFAULT_OPERATION_DATE)
            .operationType(DEFAULT_OPERATION_TYPE)
            .comment(DEFAULT_COMMENT);
        return transactionOperation;
    }

    @Before
    public void initTest() {
        transactionOperation = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionOperation() throws Exception {
        int databaseSizeBeforeCreate = transactionOperationRepository.findAll().size();

        // Create the TransactionOperation
        TransactionOperationDTO transactionOperationDTO = transactionOperationMapper.toDto(transactionOperation);
        restTransactionOperationMockMvc.perform(post("/api/transaction-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionOperationDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionOperation in the database
        List<TransactionOperation> transactionOperationList = transactionOperationRepository.findAll();
        assertThat(transactionOperationList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionOperation testTransactionOperation = transactionOperationList.get(transactionOperationList.size() - 1);
        assertThat(testTransactionOperation.getOperationDate()).isEqualTo(DEFAULT_OPERATION_DATE);
        assertThat(testTransactionOperation.getOperationType()).isEqualTo(DEFAULT_OPERATION_TYPE);
        assertThat(testTransactionOperation.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createTransactionOperationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionOperationRepository.findAll().size();

        // Create the TransactionOperation with an existing ID
        transactionOperation.setId(1L);
        TransactionOperationDTO transactionOperationDTO = transactionOperationMapper.toDto(transactionOperation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionOperationMockMvc.perform(post("/api/transaction-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionOperationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionOperation in the database
        List<TransactionOperation> transactionOperationList = transactionOperationRepository.findAll();
        assertThat(transactionOperationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTransactionOperations() throws Exception {
        // Initialize the database
        transactionOperationRepository.saveAndFlush(transactionOperation);

        // Get all the transactionOperationList
        restTransactionOperationMockMvc.perform(get("/api/transaction-operations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].operationDate").value(hasItem(sameInstant(DEFAULT_OPERATION_DATE))))
            .andExpect(jsonPath("$.[*].operationType").value(hasItem(DEFAULT_OPERATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getTransactionOperation() throws Exception {
        // Initialize the database
        transactionOperationRepository.saveAndFlush(transactionOperation);

        // Get the transactionOperation
        restTransactionOperationMockMvc.perform(get("/api/transaction-operations/{id}", transactionOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionOperation.getId().intValue()))
            .andExpect(jsonPath("$.operationDate").value(sameInstant(DEFAULT_OPERATION_DATE)))
            .andExpect(jsonPath("$.operationType").value(DEFAULT_OPERATION_TYPE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionOperation() throws Exception {
        // Get the transactionOperation
        restTransactionOperationMockMvc.perform(get("/api/transaction-operations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionOperation() throws Exception {
        // Initialize the database
        transactionOperationRepository.saveAndFlush(transactionOperation);
        int databaseSizeBeforeUpdate = transactionOperationRepository.findAll().size();

        // Update the transactionOperation
        TransactionOperation updatedTransactionOperation = transactionOperationRepository.findOne(transactionOperation.getId());
        // Disconnect from session so that the updates on updatedTransactionOperation are not directly saved in db
        em.detach(updatedTransactionOperation);
        updatedTransactionOperation
            .operationDate(UPDATED_OPERATION_DATE)
            .operationType(UPDATED_OPERATION_TYPE)
            .comment(UPDATED_COMMENT);
        TransactionOperationDTO transactionOperationDTO = transactionOperationMapper.toDto(updatedTransactionOperation);

        restTransactionOperationMockMvc.perform(put("/api/transaction-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionOperationDTO)))
            .andExpect(status().isOk());

        // Validate the TransactionOperation in the database
        List<TransactionOperation> transactionOperationList = transactionOperationRepository.findAll();
        assertThat(transactionOperationList).hasSize(databaseSizeBeforeUpdate);
        TransactionOperation testTransactionOperation = transactionOperationList.get(transactionOperationList.size() - 1);
        assertThat(testTransactionOperation.getOperationDate()).isEqualTo(UPDATED_OPERATION_DATE);
        assertThat(testTransactionOperation.getOperationType()).isEqualTo(UPDATED_OPERATION_TYPE);
        assertThat(testTransactionOperation.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionOperation() throws Exception {
        int databaseSizeBeforeUpdate = transactionOperationRepository.findAll().size();

        // Create the TransactionOperation
        TransactionOperationDTO transactionOperationDTO = transactionOperationMapper.toDto(transactionOperation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransactionOperationMockMvc.perform(put("/api/transaction-operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionOperationDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionOperation in the database
        List<TransactionOperation> transactionOperationList = transactionOperationRepository.findAll();
        assertThat(transactionOperationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransactionOperation() throws Exception {
        // Initialize the database
        transactionOperationRepository.saveAndFlush(transactionOperation);
        int databaseSizeBeforeDelete = transactionOperationRepository.findAll().size();

        // Get the transactionOperation
        restTransactionOperationMockMvc.perform(delete("/api/transaction-operations/{id}", transactionOperation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TransactionOperation> transactionOperationList = transactionOperationRepository.findAll();
        assertThat(transactionOperationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionOperation.class);
        TransactionOperation transactionOperation1 = new TransactionOperation();
        transactionOperation1.setId(1L);
        TransactionOperation transactionOperation2 = new TransactionOperation();
        transactionOperation2.setId(transactionOperation1.getId());
        assertThat(transactionOperation1).isEqualTo(transactionOperation2);
        transactionOperation2.setId(2L);
        assertThat(transactionOperation1).isNotEqualTo(transactionOperation2);
        transactionOperation1.setId(null);
        assertThat(transactionOperation1).isNotEqualTo(transactionOperation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionOperationDTO.class);
        TransactionOperationDTO transactionOperationDTO1 = new TransactionOperationDTO();
        transactionOperationDTO1.setId(1L);
        TransactionOperationDTO transactionOperationDTO2 = new TransactionOperationDTO();
        assertThat(transactionOperationDTO1).isNotEqualTo(transactionOperationDTO2);
        transactionOperationDTO2.setId(transactionOperationDTO1.getId());
        assertThat(transactionOperationDTO1).isEqualTo(transactionOperationDTO2);
        transactionOperationDTO2.setId(2L);
        assertThat(transactionOperationDTO1).isNotEqualTo(transactionOperationDTO2);
        transactionOperationDTO1.setId(null);
        assertThat(transactionOperationDTO1).isNotEqualTo(transactionOperationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionOperationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionOperationMapper.fromId(null)).isNull();
    }
}
