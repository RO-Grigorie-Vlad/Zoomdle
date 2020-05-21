package base.web.rest;

import base.domain.AplicareLicenta;
import base.domain.Licenta;
import base.domain.ProfesorInfo;
import base.domain.StudentInfo;
import base.domain.User;
import base.security.AuthoritiesConstants;
import base.security.SecurityUtils;
import base.service.AplicareLicentaService;
import base.service.LicentaService;
import base.service.ProfesorInfoService;
import base.service.StudentInfoService;
import base.service.UserService;
import base.service.dto.RaspunsAplicatie;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import base.service.MailService;

import javax.transaction.Transactional;

/**
 * REST controller for managing {@link base.domain.AplicareLicenta}.
 */
@RestController
@RequestMapping("/api")
public class AplicareLicentaResource {

    private final Logger log = LoggerFactory.getLogger(AplicareLicentaResource.class);

    private static final String ENTITY_NAME = "aplicareLicenta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AplicareLicentaService aplicareLicentaService;
    private final LicentaService licentaService;
    private final StudentInfoService studentInfoService;
    private final UserService userService;
    private final ProfesorInfoService profesorInfoService;

    private final MailService mailService;

    public AplicareLicentaResource(AplicareLicentaService aplicareLicentaService, LicentaService licentaService, StudentInfoService studentInfoService, 
                                        UserService userService, ProfesorInfoService profesorInfoService, 
                                        MailService mailService) {
        this.aplicareLicentaService = aplicareLicentaService;
        this.licentaService = licentaService;
        this.studentInfoService = studentInfoService;
        this.userService = userService;
        this.profesorInfoService = profesorInfoService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /aplicare-licentas} : Create a new aplicareLicenta.
     *
     * @param aplicareLicenta the aplicareLicenta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aplicareLicenta, or with status {@code 400 (Bad Request)} if the aplicareLicenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aplicare-licentas")
    @PreAuthorize("hasRole('ROLE_STUDENT')") //should be just ROLE_STUDENT
    public ResponseEntity<AplicareLicenta> createAplicareLicenta(@RequestBody AplicareLicenta aplicareLicenta) throws URISyntaxException {
        log.debug("REST request to save AplicareLicenta : {}", aplicareLicenta);
        if (aplicareLicenta.getId() != null) {
            throw new BadRequestAlertException("A new aplicareLicenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AplicareLicenta result = aplicareLicentaService.save(aplicareLicenta);
        return ResponseEntity.created(new URI("/api/aplicare-licentas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    

    //Accepta/Refuza aplicatia

    @PostMapping("/aplicare-licentas/raspunde")
    @Transactional
    @PreAuthorize("hasRole('ROLE_PROFESOR')")
    public void raspundeLaAplicatie(@RequestBody RaspunsAplicatie pair) throws URISyntaxException {
        log.debug("REST request to RESPOND to a AplicareLicenta");
        Long aplicatieLicentaID = (long) pair.getAplicareLicentaID();
        Boolean raspuns = pair.getRaspuns();
        log.debug("Raspunsul este : " + raspuns);

        Optional<AplicareLicenta> aplicare = this.aplicareLicentaService.findOne(aplicatieLicentaID);
        if(aplicare.isPresent()){
            AplicareLicenta aplicareLicenta = aplicare.get();
            aplicareLicenta.setAcceptata(raspuns);
            aplicareLicenta.setRezolvata(true);
            this.aplicareLicentaService.save(aplicareLicenta);
            Long studentID = 0L;

            if(raspuns == true){
                Licenta licenta = aplicareLicenta.getLicenta();
                studentID = aplicareLicenta.getStudent().getId();
                List<AplicareLicenta> aplicari = this.aplicareLicentaService.findAllByLicenta(licenta);
                if(aplicari.size() != 0){
                    for (AplicareLicenta aplicareLicenta2 : aplicari) {
                        if(aplicareLicenta2.getId() != aplicareLicenta.getId()){
                            if(!aplicareLicenta2.isRezolvata()){
                                aplicareLicenta2.setAcceptata(false);
                                aplicareLicenta2.setRezolvata(true);
                                this.aplicareLicentaService.save(aplicareLicenta2);
                            }

                        }
                    }
                }
                List<AplicareLicenta> celelalteAplicariAleStudentului = this.aplicareLicentaService.findAllByStudent2(studentID);
                if(celelalteAplicariAleStudentului.size() != 0){
                    for (AplicareLicenta aplicareLicenta2 : celelalteAplicariAleStudentului) {
                        if(aplicareLicenta2.getId() != aplicareLicenta.getId())
                        {
                            if(!aplicareLicenta2.isRezolvata()){
                                aplicareLicenta2.setAcceptata(false);
                                aplicareLicenta2.setRezolvata(true);
                                this.aplicareLicentaService.save(aplicareLicenta2);
                            }
                            
                        }
                    }
                }
                
                licenta.setAtribuita(true);
                licenta.setStudentInfo(aplicareLicenta.getStudent());

                Optional<StudentInfo> student = this.studentInfoService.findOne(studentID);
                if(student.isPresent()){
                    student.get().setProfesor(licenta.getProfesor());
                    this.studentInfoService.save(student.get());
                }
                this.licentaService.save(licenta);

                this.mailService.sendNotificationEmail(aplicareLicenta.getStudent().getUser(), true, aplicareLicenta.getLicenta().getDenumire());
            }
            else{
                this.mailService.sendNotificationEmail(aplicareLicenta.getStudent().getUser(), false, aplicareLicenta.getLicenta().getDenumire());
            }
            
        }
    }

    /**
     * {@code PUT  /aplicare-licentas} : Updates an existing aplicareLicenta.
     *
     * @param aplicareLicenta the aplicareLicenta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aplicareLicenta,
     * or with status {@code 400 (Bad Request)} if the aplicareLicenta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aplicareLicenta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aplicare-licentas")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AplicareLicenta> updateAplicareLicenta(@RequestBody AplicareLicenta aplicareLicenta) throws URISyntaxException {
        log.debug("REST request to update AplicareLicenta : {}", aplicareLicenta);
        if (aplicareLicenta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if(aplicareLicenta.isAcceptata() && !aplicareLicenta.isRezolvata()){
            aplicareLicenta.setRezolvata(true);
        }
        AplicareLicenta result = aplicareLicentaService.save(aplicareLicenta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aplicareLicenta.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /aplicare-licentas} : get all the aplicareLicentas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aplicareLicentas in body.
     */
    @GetMapping("/aplicare-licentas")
    @Transactional
    public ResponseEntity<List<AplicareLicenta>> getAllAplicareLicentas(Pageable pageable) {
    
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT)) {
            log.debug("STUDENT REST request to get a page of AplicareLicentas");
            Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            Optional<StudentInfo> student = this.studentInfoService.findOneByUser(user.get());
            Page<AplicareLicenta> page = this.aplicareLicentaService.findAllByStudent(pageable, student.get());
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
        else if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PROFESOR)){
            log.debug("PROFESOR REST request to get a page of AplicareLicentas");
            Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            Optional<ProfesorInfo> profesor = this.profesorInfoService.findOneByUser(user.get());


            List<Licenta> licente = this.licentaService.findAllByProfesor(profesor.get());
            if(licente.size() != 0){
                Page<AplicareLicenta> pageAplicariLicenta2 = this.aplicareLicentaService.findAllbyProfesor(profesor.get().getId(), pageable);
                HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageAplicariLicenta2);
                return ResponseEntity.ok().headers(headers).body(pageAplicariLicenta2.getContent());
            }
            else{
                Page<AplicareLicenta> emptypage = Page.empty();
                HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), emptypage);
                return ResponseEntity.ok().headers(headers).body(emptypage.getContent());
            }       
        }
        log.debug("REST request to get a page of AplicareLicentas");
        Page<AplicareLicenta> page = aplicareLicentaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/aplicare-licentas/student")
    @Transactional
    public List<Long> verificaAplicari(Pageable pageable) {
        log.debug("AplicareLicentaResource /aplicare-licentas/student : verificaAplicari");
        Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
        if(userLoginO.isPresent()){
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            if(user.isPresent()){
                Optional<StudentInfo> student = this.studentInfoService.findOneByUser(user.get());
                if(student.isPresent()){
                    Page<AplicareLicenta> page = this.aplicareLicentaService.findAllByStudent(pageable, student.get());
                    if(!page.isEmpty()){
                        List<AplicareLicenta> listaAplicari = page.getContent();
                        List<Long> rezultat = new ArrayList<Long>();
                        for (AplicareLicenta aplicareLicenta : listaAplicari) {
                            rezultat.add(aplicareLicenta.getLicenta().getId());    
                        }
                        return rezultat;    
                    }         
                }
                //List<Long> rezultat = this.aplicareLicentaService.findAllIDByStudent(student.get().getId());  
            } 
        }
        
        return new ArrayList<Long>(); 
    }

    /**
     * {@code GET  /aplicare-licentas/:id} : get the "id" aplicareLicenta.
     *
     * @param id the id of the aplicareLicenta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aplicareLicenta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aplicare-licentas/{id}")
    public ResponseEntity<AplicareLicenta> getAplicareLicenta(@PathVariable Long id) {
        log.debug("REST request to get AplicareLicenta : {}", id);
        Optional<AplicareLicenta> aplicareLicenta = aplicareLicentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aplicareLicenta);
    }

    /**
     * {@code DELETE  /aplicare-licentas/:id} : delete the "id" aplicareLicenta.
     *
     * @param id the id of the aplicareLicenta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aplicare-licentas/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Void> deleteAplicareLicenta(@PathVariable Long id) {
        log.debug("REST request to delete AplicareLicenta : {}", id);
        aplicareLicentaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
