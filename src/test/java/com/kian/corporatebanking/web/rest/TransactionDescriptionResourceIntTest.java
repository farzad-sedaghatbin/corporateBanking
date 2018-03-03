package com.kian.corporatebanking.web.rest;

import com.kian.corporatebanking.CorporateBankingApp;

import com.kian.corporatebanking.domain.TransactionDescription;
import com.kian.corporatebanking.repository.TransactionDescriptionRepository;
import com.kian.corporatebanking.service.TransactionDescriptionService;
import com.kian.corporatebanking.service.dto.TransactionDescriptionDTO;
import com.kian.corporatebanking.service.mapper.TransactionDescriptionMapper;
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

/**
 * Test class for the TransactionDescriptionResource REST controller.
 *
 * @see TransactionDescriptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CorporateBankingApp.class)
public class TransactionDescriptionResourceIntTest {

    private static final String DEFAULT_LABLE = "AAAAAAAAAA";
    private static final String UPDATED_LABLE = "BBBBBBBBBB";

    private static final Long DEFAULT_PARTY_ID = 1L;
    private static final Long UPDATED_PARTY_ID = 2L;

    @Autowired
    private TransactionDescriptionRepository transactionDescriptionRepository;

    @Autowired
    private TransactionDescriptionMapper transactionDescriptionMapper;

    @Autowired
    private TransactionDescriptionService transactionDescriptionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransactionDescriptionMockMvc;

    private TransactionDescription transactionDescription;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionDescriptionResource transactionDescriptionResource = new TransactionDescriptionResource(transactionDescriptionService);
        this.restTransactionDescriptionMockMvc = MockMvcBuilders.standaloneSetup(transactionDescriptionResource)
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
    public static TransactionDescription createEntity(EntityManager em) {
        TransactionDescription transactionDescription = new TransactionDescription()
            .lable(DEFAULT_LABLE)
         /*   .partyId(DEFAULT_PARTY_ID)*/;
        return transactionDescription;
    }

    @Before
    public void initTest() {
        transactionDescription = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionDescription() throws Exception {
        int databaseSizeBeforeCreate = transactionDescriptionRepository.findAll().size();

        // Create the TransactionDescription
        TransactionDescriptionDTO transactionDescriptionDTO = transactionDescriptionMapper.toDto(transactionDescription);
        restTransactionDescriptionMockMvc.perform(post("/api/transaction-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDescriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionDescription in the database
        List<TransactionDescription> transactionDescriptionList = transactionDescriptionRepository.findAll();
        assertThat(transactionDescriptionList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionDescription testTransactionDescription = transactionDescriptionList.get(transactionDescriptionList.size() - 1);
        assertThat(testTransactionDescription.getLabel()).isEqualTo(DEFAULT_LABLE);
//        assertThat(testTransactionDescription.getPartyId()).isEqualTo(DEFAULT_PARTY_ID);
    }

    @Test
    @Transactional
    public void createTransactionDescriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionDescriptionRepository.findAll().size();

        // Create the TransactionDescription with an existing ID
        transactionDescription.setId(1L);
        TransactionDescriptionDTO transactionDescriptionDTO = transactionDescriptionMapper.toDto(transactionDescription);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionDescriptionMockMvc.perform(post("/api/transaction-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDescriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionDescription in the database
        List<TransactionDescription> transactionDescriptionList = transactionDescriptionRepository.findAll();
        assertThat(transactionDescriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTransactionDescriptions() throws Exception {
        // Initialize the database
        transactionDescriptionRepository.saveAndFlush(transactionDescription);

        // Get all the transactionDescriptionList
        restTransactionDescriptionMockMvc.perform(get("/api/transaction-descriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionDescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].lable").value(hasItem(DEFAULT_LABLE.toString())))
            .andExpect(jsonPath("$.[*].partyId").value(hasItem(DEFAULT_PARTY_ID.intValue())));
    }

    @Test
    @Transactional
    public void getTransactionDescription() throws Exception {
        // Initialize the database
        transactionDescriptionRepository.saveAndFlush(transactionDescription);

        // Get the transactionDescription
        restTransactionDescriptionMockMvc.perform(get("/api/transaction-descriptions/{id}", transactionDescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionDescription.getId().intValue()))
            .andExpect(jsonPath("$.lable").value(DEFAULT_LABLE.toString()))
            .andExpect(jsonPath("$.partyId").value(DEFAULT_PARTY_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionDescription() throws Exception {
        // Get the transactionDescription
        restTransactionDescriptionMockMvc.perform(get("/api/transaction-descriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionDescription() throws Exception {
        // Initialize the database
        transactionDescriptionRepository.saveAndFlush(transactionDescription);
        int databaseSizeBeforeUpdate = transactionDescriptionRepository.findAll().size();

        // Update the transactionDescription
        TransactionDescription updatedTransactionDescription = transactionDescriptionRepository.findOne(transactionDescription.getId());
        // Disconnect from session so that the updates on updatedTransactionDescription are not directly saved in db
        em.detach(updatedTransactionDescription);
        updatedTransactionDescription
            .lable(UPDATED_LABLE)/*
            .partyId(UPDATED_PARTY_ID)*/;
        TransactionDescriptionDTO transactionDescriptionDTO = transactionDescriptionMapper.toDto(updatedTransactionDescription);

        restTransactionDescriptionMockMvc.perform(put("/api/transaction-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDescriptionDTO)))
            .andExpect(status().isOk());

        // Validate the TransactionDescription in the database
        List<TransactionDescription> transactionDescriptionList = transactionDescriptionRepository.findAll();
        assertThat(transactionDescriptionList).hasSize(databaseSizeBeforeUpdate);
        TransactionDescription testTransactionDescription = transactionDescriptionList.get(transactionDescriptionList.size() - 1);
        assertThat(testTransactionDescription.getLabel()).isEqualTo(UPDATED_LABLE);
//        assertThat(testTransactionDescription.getPartyId()).isEqualTo(UPDATED_PARTY_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionDescription() throws Exception {
        int databaseSizeBeforeUpdate = transactionDescriptionRepository.findAll().size();

        // Create the TransactionDescription
        TransactionDescriptionDTO transactionDescriptionDTO = transactionDescriptionMapper.toDto(transactionDescription);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransactionDescriptionMockMvc.perform(put("/api/transaction-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDescriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionDescription in the database
        List<TransactionDescription> transactionDescriptionList = transactionDescriptionRepository.findAll();
        assertThat(transactionDescriptionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransactionDescription() throws Exception {
        // Initialize the database
        transactionDescriptionRepository.saveAndFlush(transactionDescription);
        int databaseSizeBeforeDelete = transactionDescriptionRepository.findAll().size();

        // Get the transactionDescription
        restTransactionDescriptionMockMvc.perform(delete("/api/transaction-descriptions/{id}", transactionDescription.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TransactionDescription> transactionDescriptionList = transactionDescriptionRepository.findAll();
        assertThat(transactionDescriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionDescription.class);
        TransactionDescription transactionDescription1 = new TransactionDescription();
        transactionDescription1.setId(1L);
        TransactionDescription transactionDescription2 = new TransactionDescription();
        transactionDescription2.setId(transactionDescription1.getId());
        assertThat(transactionDescription1).isEqualTo(transactionDescription2);
        transactionDescription2.setId(2L);
        assertThat(transactionDescription1).isNotEqualTo(transactionDescription2);
        transactionDescription1.setId(null);
        assertThat(transactionDescription1).isNotEqualTo(transactionDescription2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionDescriptionDTO.class);
        TransactionDescriptionDTO transactionDescriptionDTO1 = new TransactionDescriptionDTO();
        transactionDescriptionDTO1.setId(1L);
        TransactionDescriptionDTO transactionDescriptionDTO2 = new TransactionDescriptionDTO();
        assertThat(transactionDescriptionDTO1).isNotEqualTo(transactionDescriptionDTO2);
        transactionDescriptionDTO2.setId(transactionDescriptionDTO1.getId());
        assertThat(transactionDescriptionDTO1).isEqualTo(transactionDescriptionDTO2);
        transactionDescriptionDTO2.setId(2L);
        assertThat(transactionDescriptionDTO1).isNotEqualTo(transactionDescriptionDTO2);
        transactionDescriptionDTO1.setId(null);
        assertThat(transactionDescriptionDTO1).isNotEqualTo(transactionDescriptionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionDescriptionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionDescriptionMapper.fromId(null)).isNull();
    }
}
