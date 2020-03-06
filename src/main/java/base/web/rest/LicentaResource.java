package base.web.rest;

import base.domain.Licenta;
import base.service.LicentaService;
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

    public LicentaResource(LicentaService licentaService) {
        this.licentaService = licentaService;
    }

    /**
     * {@code POST  /licentas} : Create a new licenta.
     *
     * @param licenta the licenta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new licenta, or with status {@code 400 (Bad Request)} if the licenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/licentas")
    public ResponseEntity<Licenta> createLicenta(@Valid @RequestBody Licenta licenta) throws URISyntaxException {
        log.debug("REST request to save Licenta : {}", licenta);
        if (licenta.getId() != null) {
            throw new BadRequestAlertException("A new licenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Licenta result = licentaService.save(licenta);
        return ResponseEntity.created(new URI("/api/licentas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

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
    public ResponseEntity<Licenta> updateLicenta(@Valid @RequestBody Licenta licenta) throws URISyntaxException {
        log.debug("REST request to update Licenta : {}", licenta);
        if (licenta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Licenta result = licentaService.save(licenta);
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
    public ResponseEntity<Void> deleteLicenta(@PathVariable Long id) {
        log.debug("REST request to delete Licenta : {}", id);
        licentaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
