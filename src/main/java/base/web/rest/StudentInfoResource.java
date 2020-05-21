package base.web.rest;

import base.domain.StudentInfo;
import base.service.StudentInfoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link base.domain.StudentInfo}.
 */
@RestController
@RequestMapping("/api")
public class StudentInfoResource {

    private final Logger log = LoggerFactory.getLogger(StudentInfoResource.class);

    private static final String ENTITY_NAME = "studentInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentInfoService studentInfoService;

    public StudentInfoResource(StudentInfoService studentInfoService) {
        this.studentInfoService = studentInfoService;
    }

    /**
     * {@code POST  /student-infos} : Create a new studentInfo.
     *
     * @param studentInfo the studentInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentInfo, or with status {@code 400 (Bad Request)} if the studentInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-infos")
    public ResponseEntity<StudentInfo> createStudentInfo(@Valid @RequestBody StudentInfo studentInfo) throws URISyntaxException {
        log.debug("REST request to save StudentInfo : {}", studentInfo);
        if (studentInfo.getId() != null) {
            throw new BadRequestAlertException("A new studentInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentInfo result = studentInfoService.save(studentInfo);
        return ResponseEntity.created(new URI("/api/student-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-infos} : Updates an existing studentInfo.
     *
     * @param studentInfo the studentInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentInfo,
     * or with status {@code 400 (Bad Request)} if the studentInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-infos")
    public ResponseEntity<StudentInfo> updateStudentInfo(@Valid @RequestBody StudentInfo studentInfo) throws URISyntaxException {
        log.debug("REST request to update StudentInfo : {}", studentInfo);
        if (studentInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudentInfo result = studentInfoService.save(studentInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /student-infos} : get all the studentInfos.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentInfos in body.
     */

     // CHANGED HERE :


    @GetMapping("/student-infos")
    //@PreAuthorize("hasAuthority('ROLE_PROFESOR')")
    public ResponseEntity<List<StudentInfo>> getAllStudentInfos(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("licenta-is-null".equals(filter)) {
            log.debug("REST request to get all StudentInfos where licenta is null");
            return new ResponseEntity<>(studentInfoService.findAllWhereLicentaIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of StudentInfos");
        Page<StudentInfo> page = studentInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/student-infos/professor/{id}")
    public ResponseEntity<List<StudentInfo>> getAllStudentInfosByProfessorId(Pageable pageable,@PathVariable Long id, @RequestParam(required = false) String filter) {
        if ("licenta-is-null".equals(filter)) {
            log.debug("REST request to get all StudentInfos where licenta is null");
            return new ResponseEntity<>(studentInfoService.findAllWhereLicentaIsNull(),
                    HttpStatus.OK);
        }
        log.debug("NEW : REST request to get a page of StudentInfos of a Proffesor");
        //Page<StudentInfo> page = studentInfoService.findAllStudentsOfAProfessor(pageable, id);
        Page<StudentInfo> page = studentInfoService.findAllStudentsOfAProfessor2(pageable, id);
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    /**
     * {@code GET  /student-infos/:id} : get the "id" studentInfo.
     *
     * @param id the id of the studentInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-infos/{id}")
    public ResponseEntity<StudentInfo> getStudentInfo(@PathVariable Long id) {
        log.debug("REST request to get StudentInfo : {}", id);
        Optional<StudentInfo> studentInfo = studentInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentInfo);
    }

    /**
     * {@code DELETE  /student-infos/:id} : delete the "id" studentInfo.
     *
     * @param id the id of the studentInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-infos/{id}")
    public ResponseEntity<Void> deleteStudentInfo(@PathVariable Long id) {
        log.debug("REST request to delete StudentInfo : {}", id);
        studentInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
