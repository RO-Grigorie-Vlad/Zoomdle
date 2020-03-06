package base.web.rest;

import base.LicentaApp;
import base.domain.Consultatie;
import base.repository.ConsultatieRepository;
import base.service.ConsultatieService;
import base.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static base.web.rest.TestUtil.sameInstant;
import static base.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ConsultatieResource} REST controller.
 */
@SpringBootTest(classes = LicentaApp.class)
public class ConsultatieResourceIT {

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_REZOLVATA = false;
    private static final Boolean UPDATED_REZOLVATA = true;

    private static final Boolean DEFAULT_ACCEPTATA = false;
    private static final Boolean UPDATED_ACCEPTATA = true;

    @Autowired
    private ConsultatieRepository consultatieRepository;

    @Autowired
    private ConsultatieService consultatieService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restConsultatieMockMvc;

    private Consultatie consultatie;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConsultatieResource consultatieResource = new ConsultatieResource(consultatieService);
        this.restConsultatieMockMvc = MockMvcBuilders.standaloneSetup(consultatieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultatie createEntity(EntityManager em) {
        Consultatie consultatie = new Consultatie()
            .data(DEFAULT_DATA)
            .rezolvata(DEFAULT_REZOLVATA)
            .acceptata(DEFAULT_ACCEPTATA);
        return consultatie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultatie createUpdatedEntity(EntityManager em) {
        Consultatie consultatie = new Consultatie()
            .data(UPDATED_DATA)
            .rezolvata(UPDATED_REZOLVATA)
            .acceptata(UPDATED_ACCEPTATA);
        return consultatie;
    }

    @BeforeEach
    public void initTest() {
        consultatie = createEntity(em);
    }

    @Test
    @Transactional
    public void createConsultatie() throws Exception {
        int databaseSizeBeforeCreate = consultatieRepository.findAll().size();

        // Create the Consultatie
        restConsultatieMockMvc.perform(post("/api/consultaties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(consultatie)))
            .andExpect(status().isCreated());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeCreate + 1);
        Consultatie testConsultatie = consultatieList.get(consultatieList.size() - 1);
        assertThat(testConsultatie.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testConsultatie.isRezolvata()).isEqualTo(DEFAULT_REZOLVATA);
        assertThat(testConsultatie.isAcceptata()).isEqualTo(DEFAULT_ACCEPTATA);
    }

    @Test
    @Transactional
    public void createConsultatieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = consultatieRepository.findAll().size();

        // Create the Consultatie with an existing ID
        consultatie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultatieMockMvc.perform(post("/api/consultaties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(consultatie)))
            .andExpect(status().isBadRequest());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultatieRepository.findAll().size();
        // set the field null
        consultatie.setData(null);

        // Create the Consultatie, which fails.

        restConsultatieMockMvc.perform(post("/api/consultaties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(consultatie)))
            .andExpect(status().isBadRequest());

        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConsultaties() throws Exception {
        // Initialize the database
        consultatieRepository.saveAndFlush(consultatie);

        // Get all the consultatieList
        restConsultatieMockMvc.perform(get("/api/consultaties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultatie.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].rezolvata").value(hasItem(DEFAULT_REZOLVATA.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptata").value(hasItem(DEFAULT_ACCEPTATA.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getConsultatie() throws Exception {
        // Initialize the database
        consultatieRepository.saveAndFlush(consultatie);

        // Get the consultatie
        restConsultatieMockMvc.perform(get("/api/consultaties/{id}", consultatie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultatie.getId().intValue()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.rezolvata").value(DEFAULT_REZOLVATA.booleanValue()))
            .andExpect(jsonPath("$.acceptata").value(DEFAULT_ACCEPTATA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConsultatie() throws Exception {
        // Get the consultatie
        restConsultatieMockMvc.perform(get("/api/consultaties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConsultatie() throws Exception {
        // Initialize the database
        consultatieService.save(consultatie);

        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();

        // Update the consultatie
        Consultatie updatedConsultatie = consultatieRepository.findById(consultatie.getId()).get();
        // Disconnect from session so that the updates on updatedConsultatie are not directly saved in db
        em.detach(updatedConsultatie);
        updatedConsultatie
            .data(UPDATED_DATA)
            .rezolvata(UPDATED_REZOLVATA)
            .acceptata(UPDATED_ACCEPTATA);

        restConsultatieMockMvc.perform(put("/api/consultaties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedConsultatie)))
            .andExpect(status().isOk());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
        Consultatie testConsultatie = consultatieList.get(consultatieList.size() - 1);
        assertThat(testConsultatie.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testConsultatie.isRezolvata()).isEqualTo(UPDATED_REZOLVATA);
        assertThat(testConsultatie.isAcceptata()).isEqualTo(UPDATED_ACCEPTATA);
    }

    @Test
    @Transactional
    public void updateNonExistingConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();

        // Create the Consultatie

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultatieMockMvc.perform(put("/api/consultaties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(consultatie)))
            .andExpect(status().isBadRequest());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConsultatie() throws Exception {
        // Initialize the database
        consultatieService.save(consultatie);

        int databaseSizeBeforeDelete = consultatieRepository.findAll().size();

        // Delete the consultatie
        restConsultatieMockMvc.perform(delete("/api/consultaties/{id}", consultatie.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
