package base.web.rest;

import base.LicentaApp;
import base.domain.ProfesorInfo;
import base.domain.User;
import base.repository.ProfesorInfoRepository;
import base.service.ProfesorInfoService;
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
 * Integration tests for the {@link ProfesorInfoResource} REST controller.
 */
@SpringBootTest(classes = LicentaApp.class)
public class ProfesorInfoResourceIT {

    @Autowired
    private ProfesorInfoRepository profesorInfoRepository;

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

    private MockMvc restProfesorInfoMockMvc;

    private ProfesorInfo profesorInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfesorInfoResource profesorInfoResource = new ProfesorInfoResource(profesorInfoService);
        this.restProfesorInfoMockMvc = MockMvcBuilders.standaloneSetup(profesorInfoResource)
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
    public static ProfesorInfo createEntity(EntityManager em) {
        ProfesorInfo profesorInfo = new ProfesorInfo();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profesorInfo.setUser(user);
        return profesorInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfesorInfo createUpdatedEntity(EntityManager em) {
        ProfesorInfo profesorInfo = new ProfesorInfo();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profesorInfo.setUser(user);
        return profesorInfo;
    }

    @BeforeEach
    public void initTest() {
        profesorInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfesorInfo() throws Exception {
        int databaseSizeBeforeCreate = profesorInfoRepository.findAll().size();

        // Create the ProfesorInfo
        restProfesorInfoMockMvc.perform(post("/api/profesor-infos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profesorInfo)))
            .andExpect(status().isCreated());

        // Validate the ProfesorInfo in the database
        List<ProfesorInfo> profesorInfoList = profesorInfoRepository.findAll();
        assertThat(profesorInfoList).hasSize(databaseSizeBeforeCreate + 1);
        //ProfesorInfo testProfesorInfo = profesorInfoList.get(profesorInfoList.size() - 1);
    }

    @Test
    @Transactional
    public void createProfesorInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profesorInfoRepository.findAll().size();

        // Create the ProfesorInfo with an existing ID
        profesorInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfesorInfoMockMvc.perform(post("/api/profesor-infos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profesorInfo)))
            .andExpect(status().isBadRequest());

        // Validate the ProfesorInfo in the database
        List<ProfesorInfo> profesorInfoList = profesorInfoRepository.findAll();
        assertThat(profesorInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProfesorInfos() throws Exception {
        // Initialize the database
        profesorInfoRepository.saveAndFlush(profesorInfo);

        // Get all the profesorInfoList
        restProfesorInfoMockMvc.perform(get("/api/profesor-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profesorInfo.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getProfesorInfo() throws Exception {
        // Initialize the database
        profesorInfoRepository.saveAndFlush(profesorInfo);

        // Get the profesorInfo
        restProfesorInfoMockMvc.perform(get("/api/profesor-infos/{id}", profesorInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profesorInfo.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProfesorInfo() throws Exception {
        // Get the profesorInfo
        restProfesorInfoMockMvc.perform(get("/api/profesor-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfesorInfo() throws Exception {
        // Initialize the database
        profesorInfoService.save(profesorInfo);

        int databaseSizeBeforeUpdate = profesorInfoRepository.findAll().size();

        // Update the profesorInfo
        ProfesorInfo updatedProfesorInfo = profesorInfoRepository.findById(profesorInfo.getId()).get();
        // Disconnect from session so that the updates on updatedProfesorInfo are not directly saved in db
        em.detach(updatedProfesorInfo);

        restProfesorInfoMockMvc.perform(put("/api/profesor-infos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfesorInfo)))
            .andExpect(status().isOk());

        // Validate the ProfesorInfo in the database
        List<ProfesorInfo> profesorInfoList = profesorInfoRepository.findAll();
        assertThat(profesorInfoList).hasSize(databaseSizeBeforeUpdate);
        //ProfesorInfo testProfesorInfo = profesorInfoList.get(profesorInfoList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingProfesorInfo() throws Exception {
        int databaseSizeBeforeUpdate = profesorInfoRepository.findAll().size();

        // Create the ProfesorInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfesorInfoMockMvc.perform(put("/api/profesor-infos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profesorInfo)))
            .andExpect(status().isBadRequest());

        // Validate the ProfesorInfo in the database
        List<ProfesorInfo> profesorInfoList = profesorInfoRepository.findAll();
        assertThat(profesorInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfesorInfo() throws Exception {
        // Initialize the database
        profesorInfoService.save(profesorInfo);

        int databaseSizeBeforeDelete = profesorInfoRepository.findAll().size();

        // Delete the profesorInfo
        restProfesorInfoMockMvc.perform(delete("/api/profesor-infos/{id}", profesorInfo.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProfesorInfo> profesorInfoList = profesorInfoRepository.findAll();
        assertThat(profesorInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
