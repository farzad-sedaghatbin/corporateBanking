package com.kian.corporatebanking.web.rest;

import com.kian.corporatebanking.CorporateBankingApp;

import com.kian.corporatebanking.domain.TransactionContents;
import com.kian.corporatebanking.repository.TransactionContentsRepository;
import com.kian.corporatebanking.service.TransactionContentsService;
import com.kian.corporatebanking.service.dto.TransactionContentsDTO;
import com.kian.corporatebanking.service.mapper.TransactionContentsMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.kian.corporatebanking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransactionContentsResource REST controller.
 *
 * @see TransactionContentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CorporateBankingApp.class)
public class TransactionContentsResourceIntTest {

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private TransactionContentsRepository transactionContentsRepository;

    @Autowired
    private TransactionContentsMapper transactionContentsMapper;

    @Autowired
    private TransactionContentsService transactionContentsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransactionContentsMockMvc;

    private TransactionContents transactionContents;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionContentsResource transactionContentsResource = new TransactionContentsResource(transactionContentsService);
        this.restTransactionContentsMockMvc = MockMvcBuilders.standaloneSetup(transactionContentsResource)
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
    public static TransactionContents createEntity(EntityManager em) {
        TransactionContents transactionContents = new TransactionContents()
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE);
        return transactionContents;
    }

    @Before
    public void initTest() {
        transactionContents = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionContents() throws Exception {
        int databaseSizeBeforeCreate = transactionContentsRepository.findAll().size();

        // Create the TransactionContents
        TransactionContentsDTO transactionContentsDTO = transactionContentsMapper.toDto(transactionContents);
        restTransactionContentsMockMvc.perform(post("/api/transaction-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionContentsDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionContents in the database
        List<TransactionContents> transactionContentsList = transactionContentsRepository.findAll();
        assertThat(transactionContentsList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionContents testTransactionContents = transactionContentsList.get(transactionContentsList.size() - 1);
        assertThat(testTransactionContents.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTransactionContents.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createTransactionContentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionContentsRepository.findAll().size();

        // Create the TransactionContents with an existing ID
        transactionContents.setId(1L);
        TransactionContentsDTO transactionContentsDTO = transactionContentsMapper.toDto(transactionContents);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionContentsMockMvc.perform(post("/api/transaction-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionContentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionContents in the database
        List<TransactionContents> transactionContentsList = transactionContentsRepository.findAll();
        assertThat(transactionContentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTransactionContents() throws Exception {
        // Initialize the database
        transactionContentsRepository.saveAndFlush(transactionContents);

        // Get all the transactionContentsList
        restTransactionContentsMockMvc.perform(get("/api/transaction-contents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionContents.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))));
    }

    @Test
    @Transactional
    public void getTransactionContents() throws Exception {
        // Initialize the database
        transactionContentsRepository.saveAndFlush(transactionContents);

        // Get the transactionContents
        restTransactionContentsMockMvc.perform(get("/api/transaction-contents/{id}", transactionContents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionContents.getId().intValue()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionContents() throws Exception {
        // Get the transactionContents
        restTransactionContentsMockMvc.perform(get("/api/transaction-contents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionContents() throws Exception {
        // Initialize the database
        transactionContentsRepository.saveAndFlush(transactionContents);
        int databaseSizeBeforeUpdate = transactionContentsRepository.findAll().size();

        // Update the transactionContents
        TransactionContents updatedTransactionContents = transactionContentsRepository.findOne(transactionContents.getId());
        // Disconnect from session so that the updates on updatedTransactionContents are not directly saved in db
        em.detach(updatedTransactionContents);
        updatedTransactionContents
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE);
        TransactionContentsDTO transactionContentsDTO = transactionContentsMapper.toDto(updatedTransactionContents);

        restTransactionContentsMockMvc.perform(put("/api/transaction-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionContentsDTO)))
            .andExpect(status().isOk());

        // Validate the TransactionContents in the database
        List<TransactionContents> transactionContentsList = transactionContentsRepository.findAll();
        assertThat(transactionContentsList).hasSize(databaseSizeBeforeUpdate);
        TransactionContents testTransactionContents = transactionContentsList.get(transactionContentsList.size() - 1);
        assertThat(testTransactionContents.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTransactionContents.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionContents() throws Exception {
        int databaseSizeBeforeUpdate = transactionContentsRepository.findAll().size();

        // Create the TransactionContents
        TransactionContentsDTO transactionContentsDTO = transactionContentsMapper.toDto(transactionContents);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransactionContentsMockMvc.perform(put("/api/transaction-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionContentsDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionContents in the database
        List<TransactionContents> transactionContentsList = transactionContentsRepository.findAll();
        assertThat(transactionContentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransactionContents() throws Exception {
        // Initialize the database
        transactionContentsRepository.saveAndFlush(transactionContents);
        int databaseSizeBeforeDelete = transactionContentsRepository.findAll().size();

        // Get the transactionContents
        restTransactionContentsMockMvc.perform(delete("/api/transaction-contents/{id}", transactionContents.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TransactionContents> transactionContentsList = transactionContentsRepository.findAll();
        assertThat(transactionContentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionContents.class);
        TransactionContents transactionContents1 = new TransactionContents();
        transactionContents1.setId(1L);
        TransactionContents transactionContents2 = new TransactionContents();
        transactionContents2.setId(transactionContents1.getId());
        assertThat(transactionContents1).isEqualTo(transactionContents2);
        transactionContents2.setId(2L);
        assertThat(transactionContents1).isNotEqualTo(transactionContents2);
        transactionContents1.setId(null);
        assertThat(transactionContents1).isNotEqualTo(transactionContents2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionContentsDTO.class);
        TransactionContentsDTO transactionContentsDTO1 = new TransactionContentsDTO();
        transactionContentsDTO1.setId(1L);
        TransactionContentsDTO transactionContentsDTO2 = new TransactionContentsDTO();
        assertThat(transactionContentsDTO1).isNotEqualTo(transactionContentsDTO2);
        transactionContentsDTO2.setId(transactionContentsDTO1.getId());
        assertThat(transactionContentsDTO1).isEqualTo(transactionContentsDTO2);
        transactionContentsDTO2.setId(2L);
        assertThat(transactionContentsDTO1).isNotEqualTo(transactionContentsDTO2);
        transactionContentsDTO1.setId(null);
        assertThat(transactionContentsDTO1).isNotEqualTo(transactionContentsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionContentsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionContentsMapper.fromId(null)).isNull();
    }
}
