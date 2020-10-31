package base.web.rest;

import base.domain.ProfesorInfo;
import base.service.ProfesorInfoService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link base.domain.ProfesorInfo}.
 */
@RestController
@RequestMapping("/api")
public class ProfesorInfoResource {

    private final Logger log = LoggerFactory.getLogger(ProfesorInfoResource.class);

    private static final String ENTITY_NAME = "profesorInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfesorInfoService profesorInfoService;

    public ProfesorInfoResource(ProfesorInfoService profesorInfoService) {
        this.profesorInfoService = profesorInfoService;
    }

    /**
     * {@code POST  /profesor-infos} : Create a new profesorInfo.
     *
     * @param profesorInfo the profesorInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profesorInfo, or with status {@code 400 (Bad Request)} if the profesorInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profesor-infos")
    public ResponseEntity<ProfesorInfo> createProfesorInfo(@Valid @RequestBody ProfesorInfo profesorInfo) throws URISyntaxException {
        log.debug("REST request to save ProfesorInfo : {}", profesorInfo);
        if (profesorInfo.getId() != null) {
            throw new BadRequestAlertException("A new profesorInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfesorInfo result = profesorInfoService.save(profesorInfo);
        return ResponseEntity.created(new URI("/api/profesor-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /profesor-infos} : Updates an existing profesorInfo.
     *
     * @param profesorInfo the profesorInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profesorInfo,
     * or with status {@code 400 (Bad Request)} if the profesorInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profesorInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profesor-infos")
    public ResponseEntity<ProfesorInfo> updateProfesorInfo(@Valid @RequestBody ProfesorInfo profesorInfo) throws URISyntaxException {
        log.debug("REST request to update ProfesorInfo : {}", profesorInfo);
        if (profesorInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfesorInfo result = profesorInfoService.save(profesorInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profesorInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /profesor-infos} : get all the profesorInfos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profesorInfos in body.
     */
    @GetMapping("/profesor-infos")
    public ResponseEntity<List<ProfesorInfo>> getAllProfesorInfos(Pageable pageable) {
        log.debug("REST request to get a page of ProfesorInfos");
        Page<ProfesorInfo> page = profesorInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /profesor-infos/:id} : get the "id" profesorInfo.
     *
     * @param id the id of the profesorInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profesorInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profesor-infos/{id}")
    public ResponseEntity<ProfesorInfo> getProfesorInfo(@PathVariable Long id) {
        log.debug("REST request to get ProfesorInfo : {}", id);
        Optional<ProfesorInfo> profesorInfo = profesorInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profesorInfo);
    }

    /**
     * {@code DELETE  /profesor-infos/:id} : delete the "id" profesorInfo.
     *
     * @param id the id of the profesorInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profesor-infos/{id}")
    public ResponseEntity<Void> deleteProfesorInfo(@PathVariable Long id) {
        log.debug("REST request to delete ProfesorInfo : {}", id);
        profesorInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
