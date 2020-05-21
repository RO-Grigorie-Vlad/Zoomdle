package base.web.rest;

import base.LicentaApp;
import base.domain.AplicareConsultatie;
import base.repository.AplicareConsultatieRepository;
import base.service.AplicareConsultatieService;
import base.service.ConsultatieService;
import base.service.ProfesorInfoService;
import base.service.StudentInfoService;
import base.service.UserService;
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
import java.util.List;

import static base.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AplicareConsultatieResource} REST controller.
 */
@SpringBootTest(classes = LicentaApp.class)
public class AplicareConsultatieResourceIT {

    private static final Boolean DEFAULT_REZOLVATA = false;
    private static final Boolean UPDATED_REZOLVATA = true;

    private static final Boolean DEFAULT_ACCEPTATA = false;
    private static final Boolean UPDATED_ACCEPTATA = true;

    @Autowired
    private AplicareConsultatieRepository aplicareConsultatieRepository;

    @Autowired
    private AplicareConsultatieService aplicareConsultatieService;

    @Autowired
    private StudentInfoService studentInfoService;

    @Autowired
    private ProfesorInfoService profesorInfoService;

    @Autowired
    private ConsultatieService consultatieService;

    @Autowired
    private UserService userService;

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

    private MockMvc restAplicareConsultatieMockMvc;

    private AplicareConsultatie aplicareConsultatie;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AplicareConsultatieResource aplicareConsultatieResource = new AplicareConsultatieResource(aplicareConsultatieService,  studentInfoService, 
                                                                                     userService, profesorInfoService, consultatieService);
        this.restAplicareConsultatieMockMvc = MockMvcBuilders.standaloneSetup(aplicareConsultatieResource)
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
    public static AplicareConsultatie createEntity(EntityManager em) {
        AplicareConsultatie aplicareConsultatie = new AplicareConsultatie()
            .rezolvata(DEFAULT_REZOLVATA)
            .acceptata(DEFAULT_ACCEPTATA);
        return aplicareConsultatie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AplicareConsultatie createUpdatedEntity(EntityManager em) {
        AplicareConsultatie aplicareConsultatie = new AplicareConsultatie()
            .rezolvata(UPDATED_REZOLVATA)
            .acceptata(UPDATED_ACCEPTATA);
        return aplicareConsultatie;
    }

    @BeforeEach
    public void initTest() {
        aplicareConsultatie = createEntity(em);
    }

    @Test
    @Transactional
    public void createAplicareConsultatie() throws Exception {
        int databaseSizeBeforeCreate = aplicareConsultatieRepository.findAll().size();

        // Create the AplicareConsultatie
        restAplicareConsultatieMockMvc.perform(post("/api/aplicare-consultaties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aplicareConsultatie)))
            .andExpect(status().isCreated());

        // Validate the AplicareConsultatie in the database
        List<AplicareConsultatie> aplicareConsultatieList = aplicareConsultatieRepository.findAll();
        assertThat(aplicareConsultatieList).hasSize(databaseSizeBeforeCreate + 1);
        AplicareConsultatie testAplicareConsultatie = aplicareConsultatieList.get(aplicareConsultatieList.size() - 1);
        assertThat(testAplicareConsultatie.isRezolvata()).isEqualTo(DEFAULT_REZOLVATA);
        assertThat(testAplicareConsultatie.isAcceptata()).isEqualTo(DEFAULT_ACCEPTATA);
    }

    @Test
    @Transactional
    public void createAplicareConsultatieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aplicareConsultatieRepository.findAll().size();

        // Create the AplicareConsultatie with an existing ID
        aplicareConsultatie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAplicareConsultatieMockMvc.perform(post("/api/aplicare-consultaties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aplicareConsultatie)))
            .andExpect(status().isBadRequest());

        // Validate the AplicareConsultatie in the database
        List<AplicareConsultatie> aplicareConsultatieList = aplicareConsultatieRepository.findAll();
        assertThat(aplicareConsultatieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAplicareConsultaties() throws Exception {
        // Initialize the database
        aplicareConsultatieRepository.saveAndFlush(aplicareConsultatie);

        // Get all the aplicareConsultatieList
        restAplicareConsultatieMockMvc.perform(get("/api/aplicare-consultaties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aplicareConsultatie.getId().intValue())))
            .andExpect(jsonPath("$.[*].rezolvata").value(hasItem(DEFAULT_REZOLVATA.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptata").value(hasItem(DEFAULT_ACCEPTATA.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getAplicareConsultatie() throws Exception {
        // Initialize the database
        aplicareConsultatieRepository.saveAndFlush(aplicareConsultatie);

        // Get the aplicareConsultatie
        restAplicareConsultatieMockMvc.perform(get("/api/aplicare-consultaties/{id}", aplicareConsultatie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aplicareConsultatie.getId().intValue()))
            .andExpect(jsonPath("$.rezolvata").value(DEFAULT_REZOLVATA.booleanValue()))
            .andExpect(jsonPath("$.acceptata").value(DEFAULT_ACCEPTATA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAplicareConsultatie() throws Exception {
        // Get the aplicareConsultatie
        restAplicareConsultatieMockMvc.perform(get("/api/aplicare-consultaties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAplicareConsultatie() throws Exception {
        // Initialize the database
        aplicareConsultatieService.save(aplicareConsultatie);

        int databaseSizeBeforeUpdate = aplicareConsultatieRepository.findAll().size();

        // Update the aplicareConsultatie
        AplicareConsultatie updatedAplicareConsultatie = aplicareConsultatieRepository.findById(aplicareConsultatie.getId()).get();
        // Disconnect from session so that the updates on updatedAplicareConsultatie are not directly saved in db
        em.detach(updatedAplicareConsultatie);
        updatedAplicareConsultatie
            .rezolvata(UPDATED_REZOLVATA)
            .acceptata(UPDATED_ACCEPTATA);

        restAplicareConsultatieMockMvc.perform(put("/api/aplicare-consultaties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAplicareConsultatie)))
            .andExpect(status().isOk());

        // Validate the AplicareConsultatie in the database
        List<AplicareConsultatie> aplicareConsultatieList = aplicareConsultatieRepository.findAll();
        assertThat(aplicareConsultatieList).hasSize(databaseSizeBeforeUpdate);
        AplicareConsultatie testAplicareConsultatie = aplicareConsultatieList.get(aplicareConsultatieList.size() - 1);
        assertThat(testAplicareConsultatie.isRezolvata()).isEqualTo(UPDATED_REZOLVATA);
        assertThat(testAplicareConsultatie.isAcceptata()).isEqualTo(UPDATED_ACCEPTATA);
    }

    @Test
    @Transactional
    public void updateNonExistingAplicareConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = aplicareConsultatieRepository.findAll().size();

        // Create the AplicareConsultatie

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAplicareConsultatieMockMvc.perform(put("/api/aplicare-consultaties")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aplicareConsultatie)))
            .andExpect(status().isBadRequest());

        // Validate the AplicareConsultatie in the database
        List<AplicareConsultatie> aplicareConsultatieList = aplicareConsultatieRepository.findAll();
        assertThat(aplicareConsultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAplicareConsultatie() throws Exception {
        // Initialize the database
        aplicareConsultatieService.save(aplicareConsultatie);

        int databaseSizeBeforeDelete = aplicareConsultatieRepository.findAll().size();

        // Delete the aplicareConsultatie
        restAplicareConsultatieMockMvc.perform(delete("/api/aplicare-consultaties/{id}", aplicareConsultatie.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AplicareConsultatie> aplicareConsultatieList = aplicareConsultatieRepository.findAll();
        assertThat(aplicareConsultatieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
