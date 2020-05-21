package base.repository;

import base.domain.AplicareConsultatie;
import base.domain.Consultatie;
import base.domain.StudentInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data  repository for the AplicareConsultatie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AplicareConsultatieRepository extends JpaRepository<AplicareConsultatie, Long> {
    
    Page<AplicareConsultatie> findAllByStudent(StudentInfo student, Pageable pageable);
    List<AplicareConsultatie> findAllByConsultatie(Consultatie consultatie);

    @Query("SELECT s FROM AplicareConsultatie s WHERE s.student.id = :givenStudentID")
    List<AplicareConsultatie> findAllByStudent2(@Param("givenStudentID") Long givenStudentID);
    
    @Query("SELECT s FROM AplicareConsultatie s WHERE s.consultatie.profesor.id = :id")
    Page<AplicareConsultatie> findAllbyProfesor(@Param("id") Long id, Pageable pageable);
}
