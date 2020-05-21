package base.web.rest;

import base.LicentaApp;
import base.domain.AplicareLicenta;
import base.repository.AplicareLicentaRepository;
import base.service.AplicareLicentaService;
import base.service.LicentaService;
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

import base.service.MailService;

import javax.persistence.EntityManager;
import java.util.List;

import static base.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AplicareLicentaResource} REST controller.
 */
@SpringBootTest(classes = LicentaApp.class)
public class AplicareLicentaResourceIT {

    private static final Boolean DEFAULT_REZOLVATA = false;
    private static final Boolean UPDATED_REZOLVATA = true;

    private static final Boolean DEFAULT_ACCEPTATA = false;
    private static final Boolean UPDATED_ACCEPTATA = true;

    @Autowired
    private AplicareLicentaRepository aplicareLicentaRepository;

    @Autowired
    private AplicareLicentaService aplicareLicentaService;

    @Autowired
    private LicentaService licentaService;

    @Autowired
    private StudentInfoService studentInfoService;

    @Autowired
    private ProfesorInfoService profesorInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

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

    private MockMvc restAplicareLicentaMockMvc;

    private AplicareLicenta aplicareLicenta;

    

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AplicareLicentaResource aplicareLicentaResource = new AplicareLicentaResource(aplicareLicentaService, licentaService,  studentInfoService, userService, profesorInfoService, 
                                                                                            mailService);
        this.restAplicareLicentaMockMvc = MockMvcBuilders.standaloneSetup(aplicareLicentaResource)
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
    public static AplicareLicenta createEntity(EntityManager em) {
        AplicareLicenta aplicareLicenta = new AplicareLicenta()
            .rezolvata(DEFAULT_REZOLVATA)
            .acceptata(DEFAULT_ACCEPTATA);
        return aplicareLicenta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AplicareLicenta createUpdatedEntity(EntityManager em) {
        AplicareLicenta aplicareLicenta = new AplicareLicenta()
            .rezolvata(UPDATED_REZOLVATA)
            .acceptata(UPDATED_ACCEPTATA);
        return aplicareLicenta;
    }

    @BeforeEach
    public void initTest() {
        aplicareLicenta = createEntity(em);
    }

    @Test
    @Transactional
    public void createAplicareLicenta() throws Exception {
        int databaseSizeBeforeCreate = aplicareLicentaRepository.findAll().size();

        // Create the AplicareLicenta
        restAplicareLicentaMockMvc.perform(post("/api/aplicare-licentas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aplicareLicenta)))
            .andExpect(status().isCreated());

        // Validate the AplicareLicenta in the database
        List<AplicareLicenta> aplicareLicentaList = aplicareLicentaRepository.findAll();
        assertThat(aplicareLicentaList).hasSize(databaseSizeBeforeCreate + 1);
        AplicareLicenta testAplicareLicenta = aplicareLicentaList.get(aplicareLicentaList.size() - 1);
        assertThat(testAplicareLicenta.isRezolvata()).isEqualTo(DEFAULT_REZOLVATA);
        assertThat(testAplicareLicenta.isAcceptata()).isEqualTo(DEFAULT_ACCEPTATA);
    }

    @Test
    @Transactional
    public void createAplicareLicentaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aplicareLicentaRepository.findAll().size();

        // Create the AplicareLicenta with an existing ID
        aplicareLicenta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAplicareLicentaMockMvc.perform(post("/api/aplicare-licentas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aplicareLicenta)))
            .andExpect(status().isBadRequest());

        // Validate the AplicareLicenta in the database
        List<AplicareLicenta> aplicareLicentaList = aplicareLicentaRepository.findAll();
        assertThat(aplicareLicentaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAplicareLicentas() throws Exception {
        // Initialize the database
        aplicareLicentaRepository.saveAndFlush(aplicareLicenta);

        // Get all the aplicareLicentaList
        restAplicareLicentaMockMvc.perform(get("/api/aplicare-licentas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aplicareLicenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].rezolvata").value(hasItem(DEFAULT_REZOLVATA.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptata").value(hasItem(DEFAULT_ACCEPTATA.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getAplicareLicenta() throws Exception {
        // Initialize the database
        aplicareLicentaRepository.saveAndFlush(aplicareLicenta);

        // Get the aplicareLicenta
        restAplicareLicentaMockMvc.perform(get("/api/aplicare-licentas/{id}", aplicareLicenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aplicareLicenta.getId().intValue()))
            .andExpect(jsonPath("$.rezolvata").value(DEFAULT_REZOLVATA.booleanValue()))
            .andExpect(jsonPath("$.acceptata").value(DEFAULT_ACCEPTATA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAplicareLicenta() throws Exception {
        // Get the aplicareLicenta
        restAplicareLicentaMockMvc.perform(get("/api/aplicare-licentas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAplicareLicenta() throws Exception {
        // Initialize the database
        aplicareLicentaService.save(aplicareLicenta);

        int databaseSizeBeforeUpdate = aplicareLicentaRepository.findAll().size();

        // Update the aplicareLicenta
        AplicareLicenta updatedAplicareLicenta = aplicareLicentaRepository.findById(aplicareLicenta.getId()).get();
        // Disconnect from session so that the updates on updatedAplicareLicenta are not directly saved in db
        em.detach(updatedAplicareLicenta);
        updatedAplicareLicenta
            .rezolvata(UPDATED_REZOLVATA)
            .acceptata(UPDATED_ACCEPTATA);

        restAplicareLicentaMockMvc.perform(put("/api/aplicare-licentas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAplicareLicenta)))
            .andExpect(status().isOk());

        // Validate the AplicareLicenta in the database
        List<AplicareLicenta> aplicareLicentaList = aplicareLicentaRepository.findAll();
        assertThat(aplicareLicentaList).hasSize(databaseSizeBeforeUpdate);
        AplicareLicenta testAplicareLicenta = aplicareLicentaList.get(aplicareLicentaList.size() - 1);
        assertThat(testAplicareLicenta.isRezolvata()).isEqualTo(UPDATED_REZOLVATA);
        assertThat(testAplicareLicenta.isAcceptata()).isEqualTo(UPDATED_ACCEPTATA);
    }

    @Test
    @Transactional
    public void updateNonExistingAplicareLicenta() throws Exception {
        int databaseSizeBeforeUpdate = aplicareLicentaRepository.findAll().size();

        // Create the AplicareLicenta

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAplicareLicentaMockMvc.perform(put("/api/aplicare-licentas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aplicareLicenta)))
            .andExpect(status().isBadRequest());

        // Validate the AplicareLicenta in the database
        List<AplicareLicenta> aplicareLicentaList = aplicareLicentaRepository.findAll();
        assertThat(aplicareLicentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAplicareLicenta() throws Exception {
        // Initialize the database
        aplicareLicentaService.save(aplicareLicenta);

        int databaseSizeBeforeDelete = aplicareLicentaRepository.findAll().size();

        // Delete the aplicareLicenta
        restAplicareLicentaMockMvc.perform(delete("/api/aplicare-licentas/{id}", aplicareLicenta.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AplicareLicenta> aplicareLicentaList = aplicareLicentaRepository.findAll();
        assertThat(aplicareLicentaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
