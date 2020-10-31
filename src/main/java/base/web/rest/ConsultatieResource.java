package base.web.rest;

import base.domain.AplicareConsultatie;
import base.domain.Consultatie;
import base.domain.Licenta;
import base.domain.ProfesorInfo;
import base.domain.StudentInfo;
import base.domain.User;
import base.security.AuthoritiesConstants;
import base.security.SecurityUtils;
import base.service.AplicareConsultatieService;
import base.service.ConsultatieService;
import base.service.LicentaService;
import base.service.ProfesorInfoService;
import base.service.StudentInfoService;
import base.service.UserService;
import base.service.dto.AplicareDTO;
import base.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.Instant;
/**
 * REST controller for managing {@link base.domain.Consultatie}.
 */
@RestController
@RequestMapping("/api")
@EnableScheduling
public class ConsultatieResource {

    private final Logger log = LoggerFactory.getLogger(ConsultatieResource.class);

    private static final String ENTITY_NAME = "consultatie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultatieService consultatieService;

    private final UserService userService;

    private final StudentInfoService studentInfoService;

    private final AplicareConsultatieService aplicareConsultatieService;

    private final ProfesorInfoService profesorInfoService;

    private final LicentaService licentaservice;

    public ConsultatieResource(ConsultatieService consultatieService, UserService userService, StudentInfoService studentInfoService, 
                                AplicareConsultatieService aplicareConsultatieService, 
                                ProfesorInfoService profesorInfoService, LicentaService licentaservice) {
        this.consultatieService = consultatieService;
        this.userService = userService;
        this.studentInfoService = studentInfoService;
        this.aplicareConsultatieService = aplicareConsultatieService;
        this.profesorInfoService = profesorInfoService;
        this.licentaservice = licentaservice;
        
    }

    /**
     * {@code POST  /consultaties} : Create a new consultatie.
     *
     * @param consultatie the consultatie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultatie, or with status {@code 400 (Bad Request)} if the consultatie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consultaties")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFESOR')")
    public ResponseEntity<Consultatie> createConsultatie(@Valid @RequestBody Consultatie consultatie) throws URISyntaxException {
        log.debug("REST request to save Consultatie : {}", consultatie);
        if (consultatie.getId() != null) {
            throw new BadRequestAlertException("A new consultatie cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
        if(userLoginO.isPresent()){
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            if(user.isPresent()){
                Optional<ProfesorInfo> profesor = this.profesorInfoService.findOneByUser(user.get());
                if(profesor.isPresent()){
                    consultatie.setProfesor(profesor.get());
                }
                
            }    
        }
        if(consultatie.getStudent() != null){
            consultatie.setAcceptata(true);
        }
        Consultatie result = consultatieService.save(consultatie);
        return ResponseEntity.created(new URI("/api/consultaties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @Scheduled(cron = "0 10 * * * ?") // la 10 minute
    public void rejectConsultatieIfExpired() {
        log.debug("Current time is : " + Calendar.getInstance().getTime());
            Instant now = Instant.now();
            ZoneId zoneId = ZoneId.of("Europe/Athens");
            ZonedDateTime dateAndTimeInLA = ZonedDateTime.ofInstant(now, zoneId);
            List<Consultatie> listaConsultatie = this.consultatieService.findAll2();
            if(listaConsultatie.size() > 0){
                for (Consultatie consultatie : listaConsultatie) {
                    if(!consultatie.isRezolvata()){
                        long difference = dateAndTimeInLA.compareTo(consultatie.getData());
                        if(difference > 0){
                            log.info("Time of Consultatie expired. Automaticly set it to Rezolvata = true and Acceptata = false");
                                consultatie.setAcceptata(false);
                                consultatie.setRezolvata(true);
                                this.consultatieService.save(consultatie);
                        }
                    }
                }

            }
    }

    @Scheduled(cron = "0 59 23 * * 0") // in fiecare duminica la miezul noptii ; 0 = duminica
    public void resetConsultationsWeekly() {
        log.debug("Current time is : " + Calendar.getInstance().getTime());
            List<Consultatie> listaConsultatie = this.consultatieService.findAll2();
            if(listaConsultatie.size() > 0){
                for (Consultatie consultatie : listaConsultatie) {
                    Consultatie nouaConsultatie = new Consultatie();
                    nouaConsultatie.setAcceptata(false);
                    nouaConsultatie.setStudent(null);
                    nouaConsultatie.setRezolvata(false);
                    nouaConsultatie.setProfesor(consultatie.getProfesor());
                    ZonedDateTime time = consultatie.getData();
                    time = time.plusWeeks(1);
                    nouaConsultatie.setData(time);
                    this.consultatieService.delete(consultatie.getId());
                    this.consultatieService.save(nouaConsultatie);
            }
        }
    }



    // Aplica la Consultatie

    @PostMapping("/consultatie/aplica")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Transactional
    public void aplicaLaConsultatie(@Valid @RequestBody AplicareDTO aplicare) {

        Optional<Consultatie> result = this.consultatieService.findOne((long) aplicare.getIdLicentaOrConsulatie());
        Optional<User> user =  this.userService.findOneByLogin(aplicare.getCurrentUserLogin());
        Optional<StudentInfo> student = null;

        if(user.isPresent()){
            student =  this.studentInfoService.findOneByUser(user.get());
            if(result.isPresent() && student.isPresent()){
            
                AplicareConsultatie nouaAplicare = new AplicareConsultatie();
                nouaAplicare.setConsultatie(result.get());
                nouaAplicare.setStudent(student.get());
                nouaAplicare.setAcceptata(false);
                nouaAplicare.setRezolvata(false);
                this.aplicareConsultatieService.save(nouaAplicare);       
            }
            else{
                log.debug("Consultatie NOT PRESENT or/and Student NOT PRESENT");
            }
        }
        else{
            log.debug("USER NOT PRESENT");
        }
    }

    /**
     * {@code PUT  /consultaties} : Updates an existing consultatie.
     *
     * @param consultatie the consultatie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultatie,
     * or with status {@code 400 (Bad Request)} if the consultatie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultatie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consultaties")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFESOR')")
    public ResponseEntity<Consultatie> updateConsultatie(@Valid @RequestBody Consultatie consultatie) throws URISyntaxException {
        log.debug("REST request to update Consultatie : {}", consultatie);
        if (consultatie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("Europe/Athens");
        ZonedDateTime dateAndTimeInLA = ZonedDateTime.ofInstant(now, zoneId);

        long difference = dateAndTimeInLA.compareTo(consultatie.getData());
        if(difference > 0){
            log.info("Time of Consultatie expired. Automaticly set it to Rezolvata = true and Acceptata = false");
                consultatie.setAcceptata(false);
                consultatie.setRezolvata(true);
                this.consultatieService.save(consultatie);
        }
        else if(difference < 0){
            if(consultatie.isRezolvata() == true){
                consultatie.setRezolvata(false);
            }
        }

        Consultatie result = consultatieService.save(consultatie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultatie.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /consultaties} : get all the consultaties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consultaties in body.
     */
    @GetMapping("/consultaties")
    @Transactional
    public ResponseEntity<List<Consultatie>> getAllConsultaties(Pageable pageable) {

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT)) {
            Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            Optional<StudentInfo> student = this.studentInfoService.findOneByUser(user.get());
            
            Optional<Licenta> licenta = this.licentaservice.findOneByStudentInfo(student.get());
            if(licenta.isPresent()){
                if(licenta.get().getProfesor() != null){
                    log.debug("STUDENT REST request to get a page of his profesor coordonator Consultatii");
                    Page<Consultatie> pageAplicariLicenta = this.consultatieService.findAllByProfesorPage(licenta.get().getProfesor().getId(), pageable);
                    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageAplicariLicenta);
                    return ResponseEntity.ok().headers(headers).body(pageAplicariLicenta.getContent());
                }
            }
            else{
                log.debug("REST request - student doesnt have profesor coordonator -> returned all Constualties");
                Page<Consultatie> page = consultatieService.findAll(pageable);
                HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
                return ResponseEntity.ok().headers(headers).body(page.getContent());
            }
        }
        else if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PROFESOR)){
            log.debug("PROFESOR REST request to his Constualties");
            Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            Optional<ProfesorInfo> profesor = this.profesorInfoService.findOneByUser(user.get());

            Page<Consultatie> listaConsultatii = this.consultatieService.findAllByProfesorPage(profesor.get().getId(), pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), listaConsultatii);
            return ResponseEntity.ok().headers(headers).body(listaConsultatii.getContent());
        }
        log.debug("REST request from ADMIN to get a page of Consultaties");
        Page<Consultatie> page = consultatieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consultaties/:id} : get the "id" consultatie.
     *
     * @param id the id of the consultatie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultatie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consultaties/{id}")
    public ResponseEntity<Consultatie> getConsultatie(@PathVariable Long id) {
        log.debug("REST request to get Consultatie : {}", id);
        Optional<Consultatie> consultatie = consultatieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultatie);
    }

    /**
     * {@code DELETE  /consultaties/:id} : delete the "id" consultatie.
     *
     * @param id the id of the consultatie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/consultaties/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFESOR')")
    @Transactional
    public ResponseEntity<Void> deleteConsultatie(@PathVariable Long id) {

        log.debug("REST request to delete Consultatie : {}", id);
        Optional<Consultatie> consultatie = this.consultatieService.findOne(id);
        List<AplicareConsultatie> aplicari = null;

        if(consultatie.isPresent()){
            aplicari = this.aplicareConsultatieService.findAllByConsultatie(consultatie.get());
            for (AplicareConsultatie aplicareConsultatie : aplicari) {
                long aplicareID = aplicareConsultatie.getId();
                this.aplicareConsultatieService.delete(aplicareID);
            }    
        }

        consultatieService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
