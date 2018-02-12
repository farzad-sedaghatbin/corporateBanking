package com.kian.corporatebanking.web.rest;

import com.kian.corporatebanking.CorporateBankingApp;

import com.kian.corporatebanking.domain.TransactionSigner;
import com.kian.corporatebanking.repository.TransactionSignerRepository;
import com.kian.corporatebanking.service.TransactionSignerService;
import com.kian.corporatebanking.service.dto.TransactionSignerDTO;
import com.kian.corporatebanking.service.mapper.TransactionSignerMapper;
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
import java.util.List;

import static com.kian.corporatebanking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kian.corporatebanking.domain.enumeration.RoleType;
/**
 * Test class for the TransactionSignerResource REST controller.
 *
 * @see TransactionSignerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CorporateBankingApp.class)
public class TransactionSignerResourceIntTest {

    private static final Integer DEFAULT_SIGN_ORDER = 1;
    private static final Integer UPDATED_SIGN_ORDER = 2;

    private static final RoleType DEFAULT_ROLE_TYPE = RoleType.MAKER;
    private static final RoleType UPDATED_ROLE_TYPE = RoleType.CHECKER;

    private static final Long DEFAULT_PART_ID = 1L;
    private static final Long UPDATED_PART_ID = 2L;

    @Autowired
    private TransactionSignerRepository transactionSignerRepository;

    @Autowired
    private TransactionSignerMapper transactionSignerMapper;

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

    private MockMvc restTransactionSignerMockMvc;

    private TransactionSigner transactionSigner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionSignerResource transactionSignerResource = new TransactionSignerResource(transactionSignerService);
        this.restTransactionSignerMockMvc = MockMvcBuilders.standaloneSetup(transactionSignerResource)
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
    public static TransactionSigner createEntity(EntityManager em) {
        TransactionSigner transactionSigner = new TransactionSigner()
            .signOrder(DEFAULT_SIGN_ORDER)
            .roleType(DEFAULT_ROLE_TYPE)
            .partId(DEFAULT_PART_ID);
        return transactionSigner;
    }

    @Before
    public void initTest() {
        transactionSigner = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionSigner() throws Exception {
        int databaseSizeBeforeCreate = transactionSignerRepository.findAll().size();

        // Create the TransactionSigner
        TransactionSignerDTO transactionSignerDTO = transactionSignerMapper.toDto(transactionSigner);
        restTransactionSignerMockMvc.perform(post("/api/transaction-signers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionSignerDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionSigner in the database
        List<TransactionSigner> transactionSignerList = transactionSignerRepository.findAll();
        assertThat(transactionSignerList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionSigner testTransactionSigner = transactionSignerList.get(transactionSignerList.size() - 1);
        assertThat(testTransactionSigner.getSignOrder()).isEqualTo(DEFAULT_SIGN_ORDER);
        assertThat(testTransactionSigner.getRoleType()).isEqualTo(DEFAULT_ROLE_TYPE);
        assertThat(testTransactionSigner.getPartId()).isEqualTo(DEFAULT_PART_ID);
    }

    @Test
    @Transactional
    public void createTransactionSignerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionSignerRepository.findAll().size();

        // Create the TransactionSigner with an existing ID
        transactionSigner.setId(1L);
        TransactionSignerDTO transactionSignerDTO = transactionSignerMapper.toDto(transactionSigner);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionSignerMockMvc.perform(post("/api/transaction-signers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionSignerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionSigner in the database
        List<TransactionSigner> transactionSignerList = transactionSignerRepository.findAll();
        assertThat(transactionSignerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTransactionSigners() throws Exception {
        // Initialize the database
        transactionSignerRepository.saveAndFlush(transactionSigner);

        // Get all the transactionSignerList
        restTransactionSignerMockMvc.perform(get("/api/transaction-signers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionSigner.getId().intValue())))
            .andExpect(jsonPath("$.[*].signOrder").value(hasItem(DEFAULT_SIGN_ORDER)))
            .andExpect(jsonPath("$.[*].roleType").value(hasItem(DEFAULT_ROLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].partId").value(hasItem(DEFAULT_PART_ID.intValue())));
    }

    @Test
    @Transactional
    public void getTransactionSigner() throws Exception {
        // Initialize the database
        transactionSignerRepository.saveAndFlush(transactionSigner);

        // Get the transactionSigner
        restTransactionSignerMockMvc.perform(get("/api/transaction-signers/{id}", transactionSigner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionSigner.getId().intValue()))
            .andExpect(jsonPath("$.signOrder").value(DEFAULT_SIGN_ORDER))
            .andExpect(jsonPath("$.roleType").value(DEFAULT_ROLE_TYPE.toString()))
            .andExpect(jsonPath("$.partId").value(DEFAULT_PART_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionSigner() throws Exception {
        // Get the transactionSigner
        restTransactionSignerMockMvc.perform(get("/api/transaction-signers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionSigner() throws Exception {
        // Initialize the database
        transactionSignerRepository.saveAndFlush(transactionSigner);
        int databaseSizeBeforeUpdate = transactionSignerRepository.findAll().size();

        // Update the transactionSigner
        TransactionSigner updatedTransactionSigner = transactionSignerRepository.findOne(transactionSigner.getId());
        // Disconnect from session so that the updates on updatedTransactionSigner are not directly saved in db
        em.detach(updatedTransactionSigner);
        updatedTransactionSigner
            .signOrder(UPDATED_SIGN_ORDER)
            .roleType(UPDATED_ROLE_TYPE)
            .partId(UPDATED_PART_ID);
        TransactionSignerDTO transactionSignerDTO = transactionSignerMapper.toDto(updatedTransactionSigner);

        restTransactionSignerMockMvc.perform(put("/api/transaction-signers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionSignerDTO)))
            .andExpect(status().isOk());

        // Validate the TransactionSigner in the database
        List<TransactionSigner> transactionSignerList = transactionSignerRepository.findAll();
        assertThat(transactionSignerList).hasSize(databaseSizeBeforeUpdate);
        TransactionSigner testTransactionSigner = transactionSignerList.get(transactionSignerList.size() - 1);
        assertThat(testTransactionSigner.getSignOrder()).isEqualTo(UPDATED_SIGN_ORDER);
        assertThat(testTransactionSigner.getRoleType()).isEqualTo(UPDATED_ROLE_TYPE);
        assertThat(testTransactionSigner.getPartId()).isEqualTo(UPDATED_PART_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionSigner() throws Exception {
        int databaseSizeBeforeUpdate = transactionSignerRepository.findAll().size();

        // Create the TransactionSigner
        TransactionSignerDTO transactionSignerDTO = transactionSignerMapper.toDto(transactionSigner);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransactionSignerMockMvc.perform(put("/api/transaction-signers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionSignerDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionSigner in the database
        List<TransactionSigner> transactionSignerList = transactionSignerRepository.findAll();
        assertThat(transactionSignerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransactionSigner() throws Exception {
        // Initialize the database
        transactionSignerRepository.saveAndFlush(transactionSigner);
        int databaseSizeBeforeDelete = transactionSignerRepository.findAll().size();

        // Get the transactionSigner
        restTransactionSignerMockMvc.perform(delete("/api/transaction-signers/{id}", transactionSigner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TransactionSigner> transactionSignerList = transactionSignerRepository.findAll();
        assertThat(transactionSignerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionSigner.class);
        TransactionSigner transactionSigner1 = new TransactionSigner();
        transactionSigner1.setId(1L);
        TransactionSigner transactionSigner2 = new TransactionSigner();
        transactionSigner2.setId(transactionSigner1.getId());
        assertThat(transactionSigner1).isEqualTo(transactionSigner2);
        transactionSigner2.setId(2L);
        assertThat(transactionSigner1).isNotEqualTo(transactionSigner2);
        transactionSigner1.setId(null);
        assertThat(transactionSigner1).isNotEqualTo(transactionSigner2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionSignerDTO.class);
        TransactionSignerDTO transactionSignerDTO1 = new TransactionSignerDTO();
        transactionSignerDTO1.setId(1L);
        TransactionSignerDTO transactionSignerDTO2 = new TransactionSignerDTO();
        assertThat(transactionSignerDTO1).isNotEqualTo(transactionSignerDTO2);
        transactionSignerDTO2.setId(transactionSignerDTO1.getId());
        assertThat(transactionSignerDTO1).isEqualTo(transactionSignerDTO2);
        transactionSignerDTO2.setId(2L);
        assertThat(transactionSignerDTO1).isNotEqualTo(transactionSignerDTO2);
        transactionSignerDTO1.setId(null);
        assertThat(transactionSignerDTO1).isNotEqualTo(transactionSignerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionSignerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionSignerMapper.fromId(null)).isNull();
    }
}
