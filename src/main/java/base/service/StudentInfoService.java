package base.service;

import base.domain.Licenta;
import base.domain.ProfesorInfo;
import base.domain.StudentInfo;
import base.domain.User;
import base.repository.ProfesorInfoRepository;
import base.repository.StudentInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link StudentInfo}.
 */
@Service
@Transactional
public class StudentInfoService {

    private final Logger log = LoggerFactory.getLogger(StudentInfoService.class);

    private final StudentInfoRepository studentInfoRepository;
    private final ProfesorInfoRepository profesorInfoRepository;

    public StudentInfoService(StudentInfoRepository studentInfoRepository, ProfesorInfoRepository profesorInfoRepository) {
        this.profesorInfoRepository = profesorInfoRepository;
        this.studentInfoRepository = studentInfoRepository;
    }


    public Optional<StudentInfo> findOneByUser(User user){
        return this.studentInfoRepository.findOneByUser(user);
    }

    public Optional<StudentInfo> findOneByLicenta(Licenta licenta){
        return this.studentInfoRepository.findOneByLicenta(licenta);
    }

    /**
     * Save a studentInfo.
     *
     * @param studentInfo the entity to save.
     * @return the persisted entity.
     */
    public StudentInfo save(StudentInfo studentInfo) {
        log.debug("Request to save StudentInfo : {}", studentInfo);
        return studentInfoRepository.save(studentInfo);
    }

    /**
     * Get all the studentInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentInfo> findAll(Pageable pageable) {
        log.debug("Request to get all StudentInfos");
        return studentInfoRepository.findAll(pageable);
    }


    /**
     *  Get all the studentInfos where Licenta is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<StudentInfo> findAllWhereLicentaIsNull() {
        log.debug("Request to get all studentInfos where Licenta is null");
        return StreamSupport
            .stream(studentInfoRepository.findAll().spliterator(), false)
            .filter(studentInfo -> studentInfo.getLicenta() == null)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<StudentInfo> findAllStudentsOfAProfessor( Pageable pageable, Long id) {
        log.debug("NEW Request to get all StudentInfos of Professor with id= " + id);
        
        //return studentInfoRepository.findAllByProfesor(id,pageable);

        return studentInfoRepository.findAllByProfesorID(id,pageable);
    }

    @Transactional(readOnly = true)
    public Page<StudentInfo> findAllStudentsOfAProfessor2( Pageable pageable, Long id) {
        log.debug("NEW Request2 to get all StudentInfos of Professor with id= " + id);

        Optional<ProfesorInfo> profesor = profesorInfoRepository.findById(id);
        if(profesor.isPresent()){
            return studentInfoRepository.findAllByProfesor(profesor.get(),pageable);
        }
        return null;
        

        //return studentInfoRepository.findAllByProfesorID(id,pageable);
    }

    /**
     * Get one studentInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentInfo> findOne(Long id) {
        log.debug("Request to get StudentInfo : {}", id);
        return studentInfoRepository.findById(id);
    }

    /**
     * Delete the studentInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentInfo : {}", id);
        studentInfoRepository.deleteById(id);
    }
}
