package base.web.rest;

import base.LicentaApp;
import base.domain.StudentInfo;
import base.domain.User;
import base.repository.StudentInfoRepository;
import base.service.StudentInfoService;
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
 * Integration tests for the {@link StudentInfoResource} REST controller.
 */
@SpringBootTest(classes = LicentaApp.class)
public class StudentInfoResourceIT {

    @Autowired
    private StudentInfoRepository studentInfoRepository;

    @Autowired
    private StudentInfoService studentInfoService;

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

    private MockMvc restStudentInfoMockMvc;

    private StudentInfo studentInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudentInfoResource studentInfoResource = new StudentInfoResource(studentInfoService);
        this.restStudentInfoMockMvc = MockMvcBuilders.standaloneSetup(studentInfoResource)
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
    public static StudentInfo createEntity(EntityManager em) {
        StudentInfo studentInfo = new StudentInfo();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        studentInfo.setUser(user);
        return studentInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentInfo createUpdatedEntity(EntityManager em) {
        StudentInfo studentInfo = new StudentInfo();
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        studentInfo.setUser(user);
        return studentInfo;
    }

    @BeforeEach
    public void initTest() {
        studentInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudentInfo() throws Exception {
        int databaseSizeBeforeCreate = studentInfoRepository.findAll().size();

        // Create the StudentInfo
        restStudentInfoMockMvc.perform(post("/api/student-infos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentInfo)))
            .andExpect(status().isCreated());

        // Validate the StudentInfo in the database
        List<StudentInfo> studentInfoList = studentInfoRepository.findAll();
        assertThat(studentInfoList).hasSize(databaseSizeBeforeCreate + 1);
        StudentInfo testStudentInfo = studentInfoList.get(studentInfoList.size() - 1);
    }

    @Test
    @Transactional
    public void createStudentInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentInfoRepository.findAll().size();

        // Create the StudentInfo with an existing ID
        studentInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentInfoMockMvc.perform(post("/api/student-infos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentInfo)))
            .andExpect(status().isBadRequest());

        // Validate the StudentInfo in the database
        List<StudentInfo> studentInfoList = studentInfoRepository.findAll();
        assertThat(studentInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllStudentInfos() throws Exception {
        // Initialize the database
        studentInfoRepository.saveAndFlush(studentInfo);

        // Get all the studentInfoList
        restStudentInfoMockMvc.perform(get("/api/student-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentInfo.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getStudentInfo() throws Exception {
        // Initialize the database
        studentInfoRepository.saveAndFlush(studentInfo);

        // Get the studentInfo
        restStudentInfoMockMvc.perform(get("/api/student-infos/{id}", studentInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentInfo.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStudentInfo() throws Exception {
        // Get the studentInfo
        restStudentInfoMockMvc.perform(get("/api/student-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentInfo() throws Exception {
        // Initialize the database
        studentInfoService.save(studentInfo);

        int databaseSizeBeforeUpdate = studentInfoRepository.findAll().size();

        // Update the studentInfo
        StudentInfo updatedStudentInfo = studentInfoRepository.findById(studentInfo.getId()).get();
        // Disconnect from session so that the updates on updatedStudentInfo are not directly saved in db
        em.detach(updatedStudentInfo);

        restStudentInfoMockMvc.perform(put("/api/student-infos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudentInfo)))
            .andExpect(status().isOk());

        // Validate the StudentInfo in the database
        List<StudentInfo> studentInfoList = studentInfoRepository.findAll();
        assertThat(studentInfoList).hasSize(databaseSizeBeforeUpdate);
        StudentInfo testStudentInfo = studentInfoList.get(studentInfoList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingStudentInfo() throws Exception {
        int databaseSizeBeforeUpdate = studentInfoRepository.findAll().size();

        // Create the StudentInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentInfoMockMvc.perform(put("/api/student-infos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentInfo)))
            .andExpect(status().isBadRequest());

        // Validate the StudentInfo in the database
        List<StudentInfo> studentInfoList = studentInfoRepository.findAll();
        assertThat(studentInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudentInfo() throws Exception {
        // Initialize the database
        studentInfoService.save(studentInfo);

        int databaseSizeBeforeDelete = studentInfoRepository.findAll().size();

        // Delete the studentInfo
        restStudentInfoMockMvc.perform(delete("/api/student-infos/{id}", studentInfo.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentInfo> studentInfoList = studentInfoRepository.findAll();
        assertThat(studentInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
