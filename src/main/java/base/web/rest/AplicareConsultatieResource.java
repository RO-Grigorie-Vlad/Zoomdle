package base.web.rest;

import base.domain.AplicareConsultatie;
import base.domain.Consultatie;
import base.domain.ProfesorInfo;
import base.domain.StudentInfo;
import base.domain.User;
import base.security.AuthoritiesConstants;
import base.security.SecurityUtils;
import base.service.AplicareConsultatieService;
import base.service.ConsultatieService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link base.domain.AplicareConsultatie}.
 */
@RestController
@RequestMapping("/api")
public class AplicareConsultatieResource {

    private final Logger log = LoggerFactory.getLogger(AplicareConsultatieResource.class);

    private static final String ENTITY_NAME = "aplicareConsultatie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AplicareConsultatieService aplicareConsultatieService;
    private final StudentInfoService studentInfoService;
    private final UserService userService;
    private final ProfesorInfoService profesorInfoService;
    public final ConsultatieService consultatieService;

    public AplicareConsultatieResource(AplicareConsultatieService aplicareConsultatieService, StudentInfoService studentInfoService, 
                                        UserService userService,ProfesorInfoService profesorInfoService,
                                        ConsultatieService consultatieService) {
        this.aplicareConsultatieService = aplicareConsultatieService;
        this.studentInfoService = studentInfoService;
        this.userService = userService;
        this.profesorInfoService = profesorInfoService;
        this.consultatieService = consultatieService;
    }

    /**
     * {@code POST  /aplicare-consultaties} : Create a new aplicareConsultatie.
     *
     * @param aplicareConsultatie the aplicareConsultatie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aplicareConsultatie, or with status {@code 400 (Bad Request)} if the aplicareConsultatie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aplicare-consultaties")
    public ResponseEntity<AplicareConsultatie> createAplicareConsultatie(@RequestBody AplicareConsultatie aplicareConsultatie) throws URISyntaxException {
        log.debug("REST request to save AplicareConsultatie : {}", aplicareConsultatie);
        if (aplicareConsultatie.getId() != null) {
            throw new BadRequestAlertException("A new aplicareConsultatie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AplicareConsultatie result = aplicareConsultatieService.save(aplicareConsultatie);
        return ResponseEntity.created(new URI("/api/aplicare-consultaties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/aplicare-consultaties/raspunde")
    @Transactional
    @PreAuthorize("hasRole('ROLE_PROFESOR')")
    public void raspundeLaAplicatie(@RequestBody RaspunsAplicatie pair) throws URISyntaxException {
        log.debug("REST request to RESPOND to a AplicareLicenta");
        Long aplicatieConsultatieID = (long) pair.getAplicareLicentaID();
        Boolean raspuns = pair.getRaspuns();
        log.debug("Raspunsul este : " + raspuns);

        Optional<AplicareConsultatie> aplicare = this.aplicareConsultatieService.findOne(aplicatieConsultatieID);
        if(aplicare.isPresent()){
            AplicareConsultatie aplicareConsultatie = aplicare.get();
            aplicareConsultatie.setAcceptata(raspuns);
            aplicareConsultatie.setRezolvata(true);
            this.aplicareConsultatieService.save(aplicareConsultatie);

            if(raspuns == true){
                Consultatie consultatie = aplicareConsultatie.getConsultatie();
                List<AplicareConsultatie> aplicari = this.aplicareConsultatieService.findAllByConsultatie(consultatie);
                for (AplicareConsultatie aplicareConsultatie2 : aplicari) {
                    if(aplicareConsultatie2.getId() != aplicareConsultatie.getId()){
                        aplicareConsultatie2.setAcceptata(false);
                        aplicareConsultatie2.setRezolvata(true);
                        this.aplicareConsultatieService.save(aplicareConsultatie2);
                    }
                }
                consultatie.setStudent(aplicareConsultatie.getStudent());
                consultatie.setAcceptata(true);
                this.consultatieService.save(consultatie);
            }
        }
    }

    @GetMapping("/aplicare-consultaties/student")
    @Transactional
    public List<Long> verificaAplicari(Pageable pageable) {
        log.debug("AplicareLicentaResource /aplicare-licentas/student : verificaAplicari");
        Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
        if(userLoginO.isPresent()){
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            if(user.isPresent()){
                Optional<StudentInfo> student = this.studentInfoService.findOneByUser(user.get());
                if(student.isPresent()){
                    Page<AplicareConsultatie> page = this.aplicareConsultatieService.findAllByStudent(student.get(), pageable);
                    if(!page.isEmpty()){
                        List<AplicareConsultatie> listaAplicari = page.getContent();
                        List<Long> rezultat = new ArrayList<Long>();
                        for (AplicareConsultatie aplicareConsultatie : listaAplicari) {
                            rezultat.add(aplicareConsultatie.getConsultatie().getId());    
                        }
                        return rezultat;    
                    }         
                }
            } 
        }
        
        return new ArrayList<Long>(); 
    }

    //Accepta/Refuza aplicatia



    /**
     * {@code PUT  /aplicare-consultaties} : Updates an existing aplicareConsultatie.
     *
     * @param aplicareConsultatie the aplicareConsultatie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aplicareConsultatie,
     * or with status {@code 400 (Bad Request)} if the aplicareConsultatie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aplicareConsultatie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aplicare-consultaties")
    public ResponseEntity<AplicareConsultatie> updateAplicareConsultatie(@RequestBody AplicareConsultatie aplicareConsultatie) throws URISyntaxException {
        log.debug("REST request to update AplicareConsultatie : {}", aplicareConsultatie);
        if (aplicareConsultatie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AplicareConsultatie result = aplicareConsultatieService.save(aplicareConsultatie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aplicareConsultatie.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /aplicare-consultaties} : get all the aplicareConsultaties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aplicareConsultaties in body.
     */
    @GetMapping("/aplicare-consultaties")
    @Transactional
    public ResponseEntity<List<AplicareConsultatie>> getAllAplicareConsultaties(Pageable pageable) {

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.STUDENT)) {
            log.debug("STUDENT REST request to get a page of AplicareConsultaties");

            Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            Optional<StudentInfo> student = this.studentInfoService.findOneByUser(user.get());

            Page<AplicareConsultatie> page = this.aplicareConsultatieService.findAllByStudent( student.get(), pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
        else if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PROFESOR)){
            log.debug("PROFESOR REST request to get a page of AplicareConsultaties");
            Optional<String> userLoginO = SecurityUtils.getCurrentUserLogin();
            Optional<User> user =  this.userService.findOneByLogin(userLoginO.get());
            Optional<ProfesorInfo> profesor = this.profesorInfoService.findOneByUser(user.get());

            Page<AplicareConsultatie> pageAplicariLicenta = this.aplicareConsultatieService.findAllbyProfesor(profesor.get().getId(), pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageAplicariLicenta);
            return ResponseEntity.ok().headers(headers).body(pageAplicariLicenta.getContent());
               
        }
        log.debug("REST request to get a page of All AplicareConsultaties");
        Page<AplicareConsultatie> page = this.aplicareConsultatieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aplicare-consultaties/:id} : get the "id" aplicareConsultatie.
     *
     * @param id the id of the aplicareConsultatie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aplicareConsultatie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aplicare-consultaties/{id}")
    public ResponseEntity<AplicareConsultatie> getAplicareConsultatie(@PathVariable Long id) {
        log.debug("REST request to get AplicareConsultatie : {}", id);
        Optional<AplicareConsultatie> aplicareConsultatie = aplicareConsultatieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aplicareConsultatie);
    }

    /**
     * {@code DELETE  /aplicare-consultaties/:id} : delete the "id" aplicareConsultatie.
     *
     * @param id the id of the aplicareConsultatie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aplicare-consultaties/{id}")
    public ResponseEntity<Void> deleteAplicareConsultatie(@PathVariable Long id) {
        log.debug("REST request to delete AplicareConsultatie : {}", id);
        aplicareConsultatieService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
