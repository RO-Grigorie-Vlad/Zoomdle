package base.web.rest;

import base.LicentaApp;
import base.domain.Licenta;
import base.domain.StudentInfo;
import base.repository.AplicareLicentaRepository;
import base.repository.LicentaRepository;
import base.repository.StudentInfoRepository;

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
import base.service.dto.RaspunsAplicatie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import base.domain.AplicareLicenta;

@AutoConfigureMockMvc
@SpringBootTest(classes = LicentaApp.class)
public class RaspundeIT {

    @Autowired
    private LicentaRepository licentaRepository;

    @Autowired
    private AplicareLicentaRepository aplicareLicentaRepository;

    @Autowired
    private StudentInfoRepository studentInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomMockMvc;

    private RaspunsAplicatie raspunsPozitiv;
    private RaspunsAplicatie raspunsNegativ;

    
    private Licenta licenta;
    private Licenta licenta2;

    private AplicareLicenta aplicareLicenta;
    private AplicareLicenta aplicareLicenta2;
    private AplicareLicenta aplicareLicenta3;
    private AplicareLicenta aplicareLicenta4;

    private StudentInfo studentInfo;
    private StudentInfo studentInfo2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void init() {

        // Doi studenti , doua licente. Fiecare student a aplicat la ambele licente.

        studentInfoRepository.save(StudentInfoResourceIT.createEntity(em));
        studentInfoRepository.save(StudentInfoResourceIT.createEntity(em));
        studentInfo = studentInfoRepository.findAll().get(0);
        studentInfo2 = studentInfoRepository.findAll().get(1);

        licentaRepository.save(LicentaResourceIT.createEntity(em));
        licentaRepository.save(LicentaResourceIT.createEntity(em));
        licenta = licentaRepository.findAll().get(0);
        licenta2 = licentaRepository.findAll().get(1);

        aplicareLicenta = AplicareLicentaResourceIT.createEntity(em);
        aplicareLicenta.setLicenta(licenta);
        aplicareLicenta.setStudent(studentInfo);
        aplicareLicenta.setAcceptata(false);
        aplicareLicenta.setRezolvata(false);
        aplicareLicentaRepository.save(aplicareLicenta);

        aplicareLicenta2 = AplicareLicentaResourceIT.createEntity(em);
        aplicareLicenta2.setLicenta(licenta2);
        aplicareLicenta2.setStudent(studentInfo);
        aplicareLicenta2.setAcceptata(false);
        aplicareLicenta2.setRezolvata(false);
        aplicareLicentaRepository.save(aplicareLicenta2);

        aplicareLicenta3 = AplicareLicentaResourceIT.createEntity(em);
        aplicareLicenta3.setLicenta(licenta2);
        aplicareLicenta3.setStudent(studentInfo2);
        aplicareLicenta3.setAcceptata(false);
        aplicareLicenta3.setRezolvata(false);
        aplicareLicentaRepository.save(aplicareLicenta3);

        aplicareLicenta4 = AplicareLicentaResourceIT.createEntity(em);
        aplicareLicenta4.setLicenta(licenta);
        aplicareLicenta4.setStudent(studentInfo2);
        aplicareLicenta4.setAcceptata(false);
        aplicareLicenta4.setRezolvata(false);
        aplicareLicentaRepository.save(aplicareLicenta4);
    
    }
    
    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.PROFESOR)
    public void raspundeNegativ() throws Exception{

        raspunsNegativ = new RaspunsAplicatie();
        raspunsNegativ.setAplicareID(aplicareLicenta.getId().intValue());
        raspunsNegativ.setRaspuns(false);

        restCustomMockMvc.perform(post("/api/aplicare-licentas/raspunde")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raspunsNegativ)))
            .andExpect(status().isOk());

        List<AplicareLicenta> aplicareList = aplicareLicentaRepository.findAll();
        assertEquals(aplicareList.get(0).getStudent().getId(), studentInfo.getId());
        assertEquals(aplicareList.get(0).getLicenta().getId(), licenta.getId());
        assertEquals(aplicareList.get(0).isAcceptata(), false);
        assertEquals(aplicareList.get(0).isRezolvata(), true);

    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.PROFESOR)
    public void raspundePozitiv() throws Exception{

        raspunsPozitiv = new RaspunsAplicatie();
        raspunsPozitiv.setAplicareID(aplicareLicenta3.getId().intValue());
        raspunsPozitiv.setRaspuns(true);

        restCustomMockMvc.perform(post("/api/aplicare-licentas/raspunde")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raspunsPozitiv)))
            .andExpect(status().isOk());
    
        List<AplicareLicenta> aplicareList = aplicareLicentaRepository.findAll();
        licenta2 = licentaRepository.findAll().get(1);
        assertEquals(aplicareList.get(1).isAcceptata(), false); // * verifica daca toate celelate aplicari la licenta2
        assertEquals(aplicareList.get(1).isRezolvata(), true);  // (aplicareLicenta2) au fost rezolvate si respinse
        assertEquals(aplicareList.get(2).isAcceptata(), true);  // * verifica daca aplicarea (aplicareLicenta3) a fost 
        assertEquals(aplicareList.get(2).isRezolvata(), true);  //  rezolvata si acceptata  
        assertEquals(licenta2.getStudentInfo().getId(), studentInfo2.getId());
        assertEquals(aplicareList.get(3).isAcceptata(), false); // * verifica daca toate celelate aplicari (aplicareLicenta4) 
        assertEquals(aplicareList.get(3).isRezolvata(), true);  //  ale studentului (studentInfo2) au fost rezolvate si respinse

    }

    @Test
    @Transactional
    @WithMockUser(authorities = { AuthoritiesConstants.ADMIN,  AuthoritiesConstants.STUDENT })
    public void raspundeFromOtherRoles() throws Exception {

        raspunsPozitiv = new RaspunsAplicatie();
        raspunsPozitiv.setAplicareID(aplicareLicenta2.getId().intValue());
        raspunsPozitiv.setRaspuns(true);

        restCustomMockMvc.perform(post("/api/aplicare-licentas/raspunde")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raspunsPozitiv)))
            .andExpect(status().isForbidden());

    }

}

