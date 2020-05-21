package base.service;

import base.domain.Consultatie;
import base.domain.ProfesorInfo;
import base.domain.StudentInfo;
import base.repository.ConsultatieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Consultatie}.
 */
@Service
@Transactional
public class ConsultatieService {

    private final Logger log = LoggerFactory.getLogger(ConsultatieService.class);

    private final ConsultatieRepository consultatieRepository;

    public ConsultatieService(ConsultatieRepository consultatieRepository) {
        this.consultatieRepository = consultatieRepository;
    }


    public List<Consultatie> findAll2(){
        return this.consultatieRepository.findAll2();
    }

    public List<Consultatie> findAllByProfesor(ProfesorInfo profesor){
        return this.consultatieRepository.findAllByProfesor(profesor);
    }

    public List<Consultatie> findAllByStudent(StudentInfo student){
        return this.consultatieRepository.findAllByStudent(student);
    }

    public Page<Consultatie> findAllByProfesorPage(Long id, Pageable pagable){
        return this.consultatieRepository.findAllByProfesorPage(id, pagable);
    }

    /**
     * Save a consultatie.
     *
     * @param consultatie the entity to save.
     * @return the persisted entity.
     */
    public Consultatie save(Consultatie consultatie) {
        log.debug("Request to save Consultatie : {}", consultatie);
        return consultatieRepository.save(consultatie);
    }

    /**
     * Get all the consultaties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Consultatie> findAll(Pageable pageable) {
        log.debug("Request to get all Consultaties");
        return consultatieRepository.findAll(pageable);
    }

    /**
     * Get one consultatie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Consultatie> findOne(Long id) {
        log.debug("Request to get Consultatie : {}", id);
        return consultatieRepository.findById(id);
    }

    /**
     * Delete the consultatie by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Consultatie : {}", id);
        consultatieRepository.deleteById(id);
    }
}
