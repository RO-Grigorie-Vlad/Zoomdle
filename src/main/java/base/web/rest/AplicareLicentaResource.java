package base.web.rest;

import base.domain.AplicareLicenta;
import base.service.AplicareLicentaService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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

    public AplicareLicentaResource(AplicareLicentaService aplicareLicentaService) {
        this.aplicareLicentaService = aplicareLicentaService;
    }

    /**
     * {@code POST  /aplicare-licentas} : Create a new aplicareLicenta.
     *
     * @param aplicareLicenta the aplicareLicenta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aplicareLicenta, or with status {@code 400 (Bad Request)} if the aplicareLicenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aplicare-licentas")
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
    public ResponseEntity<AplicareLicenta> updateAplicareLicenta(@RequestBody AplicareLicenta aplicareLicenta) throws URISyntaxException {
        log.debug("REST request to update AplicareLicenta : {}", aplicareLicenta);
        if (aplicareLicenta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
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
    public ResponseEntity<List<AplicareLicenta>> getAllAplicareLicentas(Pageable pageable) {
        log.debug("REST request to get a page of AplicareLicentas");
        Page<AplicareLicenta> page = aplicareLicentaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
    public ResponseEntity<Void> deleteAplicareLicenta(@PathVariable Long id) {
        log.debug("REST request to delete AplicareLicenta : {}", id);
        aplicareLicentaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
