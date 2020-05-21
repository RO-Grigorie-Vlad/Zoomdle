package base.web.rest;

import base.domain.AplicareLicenta;
import base.domain.Licenta;
import base.domain.ProfesorInfo;
import base.domain.StudentInfo;
import base.domain.User;
import base.security.SecurityUtils;
import base.service.AplicareLicentaService;
import base.service.LicentaService;
import base.service.ProfesorInfoService;
import base.service.StudentInfoService;
import base.service.UserService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;

import base.service.dto.LicentaDTO;
/**
 * REST controller for managing {@link base.domain.Licenta}.
 */
@RestController
@RequestMapping("/api")
public class LicentaResource {

    private final Logger log = LoggerFactory.getLogger(LicentaResource.class);

    private static final String ENTITY_NAME = "licenta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LicentaService licentaService;
    private final AplicareLicentaService aplicareLicentaService;
    private final UserService userService;
    private final StudentInfoService studentInfoService;
    private final ProfesorInfoService profesorInfoService;

    public LicentaResource(LicentaService licentaService, AplicareLicentaService aplicareLicentaService, UserService userService, StudentInfoService studentInfoService,ProfesorInfoService profesorInfoService) {
        this.licentaService = licentaService;
        this.aplicareLicentaService = aplicareLicentaService;
        this.userService = userService;
        this.studentInfoService = studentInfoService;
        this.profesorInfoService = profesorInfoService;
    }

    /**
     * {@code POST  /licentas} : Create a new licenta.
     *
     * @param licenta the licenta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new licenta, or with status {@code 400 (Bad Request)} if the licenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/licentas")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFESOR')")
    @Transactional
    public ResponseEntity<Licenta> createLicenta(@Valid @RequestBody Licenta licenta) throws URISyntaxException {
        log.debug("REST request to save Licenta : {}", licenta);

        //Optional<User> user =  this.userService.findOneByLogin(licenta.getCurrentUserLogin());
        Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
        if(userLoginO.isPresent()){
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            if(user.isPresent()){
                Optional<ProfesorInfo> profesor = this.profesorInfoService.findOneByUser(user.get());
                if(profesor.isPresent()){
                    licenta.setProfesor(profesor.get());
                }
            }    
        }
        if(licenta.getStudentInfo() != null){
            log.debug("Atribuita to : " + licenta.getStudentInfo().getUser().getLastName());
            licenta.setAtribuita(true);
            if(licenta.getProfesor() != null){
                StudentInfo student = licenta.getStudentInfo();
                student.setProfesor(licenta.getProfesor());
                this.studentInfoService.save(student);
            }
        }
        else{
            log.debug("Licenta neatribuita");
            licenta.setAtribuita(false);
        }
        
        if (licenta.getId() != null) {
            throw new BadRequestAlertException("A new licenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Licenta result = licentaService.save(licenta);
        return ResponseEntity.created(new URI("/api/licentas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    // NEW NEW :

    @PostMapping("/licenta/aplica")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Transactional
    public void aplicaLaLicenta(@Valid @RequestBody LicentaDTO aplicare) {

        log.debug("Got a LicentaDTO from the frontend: CurrentUser = " + aplicare.getCurrentUserLogin() + " aplica la licenta cu id-ul: " + aplicare.getLicentaID());

        Optional<Licenta> result = this.licentaService.findOne((long) aplicare.getLicentaID());
        Optional<User> user =  this.userService.findOneByLogin(aplicare.getCurrentUserLogin());
        Optional<StudentInfo> student = null;

        if(user.isPresent()){
            student =  this.studentInfoService.findOneByUser(user.get());
            if(result.isPresent() && student.isPresent()){
            
                AplicareLicenta nouaAplicare = new AplicareLicenta();
                nouaAplicare.setLicenta(result.get());
                nouaAplicare.setStudent(student.get());
                nouaAplicare.setAcceptata(false);
                nouaAplicare.setRezolvata(false);
                this.aplicareLicentaService.save(nouaAplicare);       
            }
            else{
                log.debug("Licenta NOT PRESENT or/and Student NOT PRESENT");
            }
        }
        else{
            log.debug("USER NOT PRESENT");
        }
    }

    // END NEW

    /**
     * {@code PUT  /licentas} : Updates an existing licenta.
     *
     * @param licenta the licenta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated licenta,
     * or with status {@code 400 (Bad Request)} if the licenta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the licenta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/licentas")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFESOR')")
    public ResponseEntity<Licenta> updateLicenta(@Valid @RequestBody Licenta licenta) throws URISyntaxException {
        log.debug("REST request to update Licenta : {}", licenta);
        if (licenta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<Licenta> lic = this.licentaService.findOne(licenta.getId());
        
        Licenta result = licentaService.save(licenta);
        if((lic.get().getStudentInfo() == null && licenta.getStudentInfo() != null) 
                || (lic.get().getStudentInfo() != null && licenta.getStudentInfo() != null)){
            Optional<StudentInfo> student = this.studentInfoService.findOne(licenta.getStudentInfo().getId());
            if(student.isPresent()){
                student.get().setProfesor(licenta.getProfesor());
                this.studentInfoService.save(student.get());
            } 
        }
        else if(lic.get().getStudentInfo() != null && licenta.getStudentInfo() == null){
            Optional<StudentInfo> student = this.studentInfoService.findOne(lic.get().getStudentInfo().getId());
            if(student.isPresent()){
                student.get().setProfesor(null);
                this.studentInfoService.save(student.get());
            } 
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, licenta.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /licentas} : get all the licentas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of licentas in body.
     */
    @GetMapping("/licentas")
    public ResponseEntity<List<Licenta>> getAllLicentas(Pageable pageable) {
        log.debug("REST request to get a page of Licentas");
        Page<Licenta> page = licentaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("licentas/profesor")
    @Transactional
    public ResponseEntity<List<Licenta>> getAllLicentasOfTheCURRENTProfesor(Pageable pageable) {
        log.debug("REST request to get a page of Licentas");
        Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
        if(userLoginO.isPresent()){
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            if(user.isPresent()){
                Optional<ProfesorInfo> profesor = this.profesorInfoService.findOneByUser(user.get());
                if(profesor.isPresent()){
                    Long profesorID = profesor.get().getId();
                    Page<Licenta> page = licentaService.findAllLicentasOfAProfessor(pageable,profesorID);
                    HttpHeaders headers = 
                        PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
                    return ResponseEntity.ok().headers(headers).body(page.getContent());
                }
            }
        }
        return null;
    }

    @GetMapping("/licentas/professor/{id}")
    public ResponseEntity<List<Licenta>> getAllLicentasByProfessorId(Pageable pageable, @PathVariable Long id) {

        log.debug("NEW : REST request to get a page of Licentas of a Proffesor");
        Page<Licenta> page = licentaService.findAllLicentasOfAProfessor(pageable, id);
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /licentas/:id} : get the "id" licenta.
     *
     * @param id the id of the licenta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the licenta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/licentas/{id}")
    public ResponseEntity<Licenta> getLicenta(@PathVariable Long id) {
        log.debug("REST request to get Licenta : {}", id);
        Optional<Licenta> licenta = licentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(licenta);
    }

    /**
     * {@code DELETE  /licentas/:id} : delete the "id" licenta.
     *
     * @param id the id of the licenta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/licentas/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROFESOR')")
    @Transactional
    public ResponseEntity<Void> deleteLicenta(@PathVariable Long id) {
        log.debug("REST request to delete Licenta : {}", id);

        Optional<Licenta> licenta = this.licentaService.findOne(id);
        Optional<StudentInfo> student = null;
        List<AplicareLicenta> aplicari = null;

        if(licenta.isPresent()){
            student = this.studentInfoService.findOneByLicenta(licenta.get());
            if(student.isPresent()){
                StudentInfo studentI = student.get();
                studentI.setLicenta(null);
                if(studentI.getProfesor() != null){
                    studentI.setProfesor(null);
                }
                this.studentInfoService.save(studentI);
            }
            aplicari = this.aplicareLicentaService.findAllByLicenta(licenta.get());
            for (AplicareLicenta aplicareLicenta : aplicari) {

                /**StudentInfo studentInfo = aplicareLicenta.getStudent();
                if(studentInfo != null){
                    Set<AplicareLicenta> aplicariLaLicente = studentInfo.getAplicareLics();
                    aplicariLaLicente.remove(aplicareLicenta);
                    studentInfo.setAplicareLics(aplicariLaLicente);
                    this.studentInfoService.save(studentInfo);
                }**/
                long aplicareID = aplicareLicenta.getId();
                this.aplicareLicentaService.delete(aplicareID);
            }
            
        }
        licentaService.delete(id);
        
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
