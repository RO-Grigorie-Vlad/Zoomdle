package base.service;

import base.domain.AplicareConsultatie;
import base.repository.AplicareConsultatieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AplicareConsultatie}.
 */
@Service
@Transactional
public class AplicareConsultatieService {

    private final Logger log = LoggerFactory.getLogger(AplicareConsultatieService.class);

    private final AplicareConsultatieRepository aplicareConsultatieRepository;

    public AplicareConsultatieService(AplicareConsultatieRepository aplicareConsultatieRepository) {
        this.aplicareConsultatieRepository = aplicareConsultatieRepository;
    }

    /**
     * Save a aplicareConsultatie.
     *
     * @param aplicareConsultatie the entity to save.
     * @return the persisted entity.
     */
    public AplicareConsultatie save(AplicareConsultatie aplicareConsultatie) {
        log.debug("Request to save AplicareConsultatie : {}", aplicareConsultatie);
        return aplicareConsultatieRepository.save(aplicareConsultatie);
    }

    /**
     * Get all the aplicareConsultaties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AplicareConsultatie> findAll(Pageable pageable) {
        log.debug("Request to get all AplicareConsultaties");
        return aplicareConsultatieRepository.findAll(pageable);
    }

    /**
     * Get one aplicareConsultatie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AplicareConsultatie> findOne(Long id) {
        log.debug("Request to get AplicareConsultatie : {}", id);
        return aplicareConsultatieRepository.findById(id);
    }

    /**
     * Delete the aplicareConsultatie by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AplicareConsultatie : {}", id);
        aplicareConsultatieRepository.deleteById(id);
    }
}
