package base.service;

import base.domain.Licenta;
import base.repository.LicentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Licenta}.
 */
@Service
@Transactional
public class LicentaService {

    private final Logger log = LoggerFactory.getLogger(LicentaService.class);

    private final LicentaRepository licentaRepository;

    public LicentaService(LicentaRepository licentaRepository) {
        this.licentaRepository = licentaRepository;
    }

    /**
     * Save a licenta.
     *
     * @param licenta the entity to save.
     * @return the persisted entity.
     */
    public Licenta save(Licenta licenta) {
        log.debug("Request to save Licenta : {}", licenta);
        return licentaRepository.save(licenta);
    }

    /**
     * Get all the licentas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Licenta> findAll(Pageable pageable) {
        log.debug("Request to get all Licentas");
        return licentaRepository.findAll(pageable);
    }

    /**
     * Get one licenta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Licenta> findOne(Long id) {
        log.debug("Request to get Licenta : {}", id);
        return licentaRepository.findById(id);
    }

    /**
     * Delete the licenta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Licenta : {}", id);
        licentaRepository.deleteById(id);
    }
}
