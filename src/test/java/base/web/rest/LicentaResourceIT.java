package base.web.rest;

import base.LicentaApp;
import base.domain.Authority;
import base.domain.Licenta;
import base.domain.User;
import base.repository.LicentaRepository;
import base.security.AuthoritiesConstants;
import base.service.AplicareLicentaService;
import base.service.LicentaService;
import base.service.ProfesorInfoService;
import base.service.StudentInfoService;
import base.service.UserService;
import base.service.dto.AplicareDTO;
import base.service.dto.UserDTO;
import base.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeAll;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static base.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LicentaResource} REST controller.
 */

@SpringBootTest(classes = LicentaApp.class)
public class LicentaResourceIT {

    private static final String DEFAULT_DENUMIRE = "AAAAAAAAAA";
    private static final String UPDATED_DENUMIRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIERE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIERE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATRIBUITA = false;
    private static final Boolean UPDATED_ATRIBUITA = true;

    @Autowired
    private LicentaRepository licentaRepository;

    @Autowired
    private LicentaService licentaService;

    @Autowired
    private AplicareLicentaService aplicareLicentaService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentInfoService studentInfoService;

    @Autowired
    private ProfesorInfoService profesorInfoService;

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

    private MockMvc restLicentaMockMvc;

    private Licenta licenta;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LicentaResource licentaResource = new LicentaResource(licentaService, aplicareLicentaService, userService, studentInfoService, profesorInfoService);
        this.restLicentaMockMvc = MockMvcBuilders.standaloneSetup(licentaResource)
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
    public static Licenta createEntity(EntityManager em) {
        Licenta licenta = new Licenta()
            .denumire(DEFAULT_DENUMIRE)
            .descriere(DEFAULT_DESCRIERE)
            .atribuita(DEFAULT_ATRIBUITA);
        return licenta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Licenta createUpdatedEntity(EntityManager em) {
        Licenta licenta = new Licenta()
            .denumire(UPDATED_DENUMIRE)
            .descriere(UPDATED_DESCRIERE)
            .atribuita(UPDATED_ATRIBUITA);
        return licenta;
    }

    @BeforeEach
    public void initTest() {
        licenta = createEntity(em);
    }

    @Test
    @Transactional
    public void createLicenta() throws Exception {
        int databaseSizeBeforeCreate = licentaRepository.findAll().size();

        // Create the Licenta
        restLicentaMockMvc.perform(post("/api/licentas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(licenta)))
            .andExpect(status().isCreated());

        // Validate the Licenta in the database
        List<Licenta> licentaList = licentaRepository.findAll();
        assertThat(licentaList).hasSize(databaseSizeBeforeCreate + 1);
        Licenta testLicenta = licentaList.get(licentaList.size() - 1);
        assertThat(testLicenta.getDenumire()).isEqualTo(DEFAULT_DENUMIRE);
        assertThat(testLicenta.getDescriere()).isEqualTo(DEFAULT_DESCRIERE);
        assertThat(testLicenta.isAtribuita()).isEqualTo(DEFAULT_ATRIBUITA);
    }

    @Test
    @Transactional
    public void createLicentaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = licentaRepository.findAll().size();

        // Create the Licenta with an existing ID
        licenta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLicentaMockMvc.perform(post("/api/licentas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(licenta)))
            .andExpect(status().isBadRequest());

        // Validate the Licenta in the database
        List<Licenta> licentaList = licentaRepository.findAll();
        assertThat(licentaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDenumireIsRequired() throws Exception {
        int databaseSizeBeforeTest = licentaRepository.findAll().size();
        // set the field null
        licenta.setDenumire(null);

        // Create the Licenta, which fails.

        restLicentaMockMvc.perform(post("/api/licentas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(licenta)))
            .andExpect(status().isBadRequest());

        List<Licenta> licentaList = licentaRepository.findAll();
        assertThat(licentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLicentas() throws Exception {
        // Initialize the database
        licentaRepository.saveAndFlush(licenta);

        // Get all the licentaList
        restLicentaMockMvc.perform(get("/api/licentas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumire").value(hasItem(DEFAULT_DENUMIRE)))
            .andExpect(jsonPath("$.[*].descriere").value(hasItem(DEFAULT_DESCRIERE)))
            .andExpect(jsonPath("$.[*].atribuita").value(hasItem(DEFAULT_ATRIBUITA.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getLicenta() throws Exception {
        // Initialize the database
        licentaRepository.saveAndFlush(licenta);

        // Get the licenta
        restLicentaMockMvc.perform(get("/api/licentas/{id}", licenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(licenta.getId().intValue()))
            .andExpect(jsonPath("$.denumire").value(DEFAULT_DENUMIRE))
            .andExpect(jsonPath("$.descriere").value(DEFAULT_DESCRIERE))
            .andExpect(jsonPath("$.atribuita").value(DEFAULT_ATRIBUITA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLicenta() throws Exception {
        // Get the licenta
        restLicentaMockMvc.perform(get("/api/licentas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLicenta() throws Exception {
        // Initialize the database
        licentaService.save(licenta);

        int databaseSizeBeforeUpdate = licentaRepository.findAll().size();

        // Update the licenta
        Licenta updatedLicenta = licentaRepository.findById(licenta.getId()).get();
        // Disconnect from session so that the updates on updatedLicenta are not directly saved in db
        em.detach(updatedLicenta);
        updatedLicenta
            .denumire(UPDATED_DENUMIRE)
            .descriere(UPDATED_DESCRIERE)
            .atribuita(UPDATED_ATRIBUITA);

        restLicentaMockMvc.perform(put("/api/licentas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLicenta)))
            .andExpect(status().isOk());

        // Validate the Licenta in the database
        List<Licenta> licentaList = licentaRepository.findAll();
        assertThat(licentaList).hasSize(databaseSizeBeforeUpdate);
        Licenta testLicenta = licentaList.get(licentaList.size() - 1);
        assertThat(testLicenta.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
        assertThat(testLicenta.getDescriere()).isEqualTo(UPDATED_DESCRIERE);
        assertThat(testLicenta.isAtribuita()).isEqualTo(UPDATED_ATRIBUITA);
    }

    @Test
    @Transactional
    public void updateNonExistingLicenta() throws Exception {
        int databaseSizeBeforeUpdate = licentaRepository.findAll().size();

        // Create the Licenta

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLicentaMockMvc.perform(put("/api/licentas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(licenta)))
            .andExpect(status().isBadRequest());

        // Validate the Licenta in the database
        List<Licenta> licentaList = licentaRepository.findAll();
        assertThat(licentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLicenta() throws Exception {
        // Initialize the database
        licentaService.save(licenta);

        int databaseSizeBeforeDelete = licentaRepository.findAll().size();

        // Delete the licenta
        restLicentaMockMvc.perform(delete("/api/licentas/{id}", licenta.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Licenta> licentaList = licentaRepository.findAll();
        assertThat(licentaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
