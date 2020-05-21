package base.service;

import base.domain.ProfesorInfo;
import base.domain.User;
import base.repository.ProfesorInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProfesorInfo}.
 */
@Service
@Transactional
public class ProfesorInfoService {

    private final Logger log = LoggerFactory.getLogger(ProfesorInfoService.class);

    private final ProfesorInfoRepository profesorInfoRepository;

    public ProfesorInfoService(ProfesorInfoRepository profesorInfoRepository) {
        this.profesorInfoRepository = profesorInfoRepository;
    }

    /**
     * Save a profesorInfo.
     *
     * @param profesorInfo the entity to save.
     * @return the persisted entity.
     */
    public ProfesorInfo save(ProfesorInfo profesorInfo) {
        log.debug("Request to save ProfesorInfo : {}", profesorInfo);
        return profesorInfoRepository.save(profesorInfo);
    }

    /**
     * Get all the profesorInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfesorInfo> findAll(Pageable pageable) {
        log.debug("Request to get all ProfesorInfos");
        return profesorInfoRepository.findAll(pageable);
    }

    /**
     * Get one profesorInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfesorInfo> findOne(Long id) {
        log.debug("Request to get ProfesorInfo : {}", id);
        return profesorInfoRepository.findById(id);
    }

    public Optional<ProfesorInfo> findOneByUser(User user){
        return this.profesorInfoRepository.findOneByUser(user);
    }

    /**
     * Delete the profesorInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProfesorInfo : {}", id);
        profesorInfoRepository.deleteById(id);
    }
}
