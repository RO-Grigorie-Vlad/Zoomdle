package base.web.rest;

import base.domain.Consultatie;
import base.service.ConsultatieService;
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
 * REST controller for managing {@link base.domain.Consultatie}.
 */
@RestController
@RequestMapping("/api")
public class ConsultatieResource {

    private final Logger log = LoggerFactory.getLogger(ConsultatieResource.class);

    private static final String ENTITY_NAME = "consultatie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultatieService consultatieService;

    public ConsultatieResource(ConsultatieService consultatieService) {
        this.consultatieService = consultatieService;
    }

    /**
     * {@code POST  /consultaties} : Create a new consultatie.
     *
     * @param consultatie the consultatie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultatie, or with status {@code 400 (Bad Request)} if the consultatie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consultaties")
    public ResponseEntity<Consultatie> createConsultatie(@Valid @RequestBody Consultatie consultatie) throws URISyntaxException {
        log.debug("REST request to save Consultatie : {}", consultatie);
        if (consultatie.getId() != null) {
            throw new BadRequestAlertException("A new consultatie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Consultatie result = consultatieService.save(consultatie);
        return ResponseEntity.created(new URI("/api/consultaties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<Consultatie> updateConsultatie(@Valid @RequestBody Consultatie consultatie) throws URISyntaxException {
        log.debug("REST request to update Consultatie : {}", consultatie);
        if (consultatie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
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
    public ResponseEntity<List<Consultatie>> getAllConsultaties(Pageable pageable) {
        log.debug("REST request to get a page of Consultaties");
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
    public ResponseEntity<Void> deleteConsultatie(@PathVariable Long id) {
        log.debug("REST request to delete Consultatie : {}", id);
        consultatieService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
