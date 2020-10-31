package base.web.rest;

import base.LicentaApp;
import base.domain.Licenta;
import base.domain.StudentInfo;
import base.domain.User;
import base.repository.AplicareLicentaRepository;
import base.repository.LicentaRepository;
import base.repository.StudentInfoRepository;
import base.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

import base.security.AuthoritiesConstants;
import base.service.dto.AplicareDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import base.domain.AplicareLicenta;

@AutoConfigureMockMvc
@SpringBootTest(classes = LicentaApp.class)
public class AplicaIT {

    @Autowired
    private LicentaRepository licentaRepository;

    @Autowired
    private AplicareLicentaRepository aplicareLicentaRepository;

    @Autowired
    private StudentInfoRepository studentInfoRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomMockMvc;

    private AplicareDTO aplicare;

    private User user;
    
    private Licenta licenta;

    private StudentInfo studentInfo;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {
        user = UserResourceIT.createEntity(em);
        userRepository.save(user);

        studentInfo = StudentInfoResourceIT.createEntity(em);
        studentInfo.setUser(user);
        studentInfoRepository.save(studentInfo);

        licenta = LicentaResourceIT.createEntity(em);
        licentaRepository.save(licenta);

        aplicare = new AplicareDTO();
        aplicare.setCurrentUserLogin(user.getLogin());
        aplicare.setIdLicentaOrConsulatie( licenta.getId().intValue());
    }
    
    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.STUDENT)
    public void aplica() throws Exception{

        int aplicariDatabaseSizeBeforeCreate = aplicareLicentaRepository.findAll().size();

        restCustomMockMvc.perform(post("/api/licenta/aplica")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aplicare)))
            .andExpect(status().isOk());

        List<AplicareLicenta> aplicareList = aplicareLicentaRepository.findAll();
        assertThat(aplicareList).hasSize(aplicariDatabaseSizeBeforeCreate + 1);
        assertEquals(aplicareList.get(0).getStudent().getId(), studentInfo.getId());
        assertEquals(aplicareList.get(0).getLicenta().getId(), licenta.getId());
        assertEquals(aplicareList.get(0).isAcceptata(), false);
        assertEquals(aplicareList.get(0).isRezolvata(), false);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = { AuthoritiesConstants.ADMIN,  AuthoritiesConstants.PROFESOR })
    public void aplicaFromOtherRoles() throws Exception{

        restCustomMockMvc.perform(post("/api/licenta/aplica")
        .contentType(TestUtil.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(aplicare)))
        .andExpect(status().isForbidden());
    }

}

