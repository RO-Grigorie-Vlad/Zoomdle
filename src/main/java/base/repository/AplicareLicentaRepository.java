package base.repository;

import base.domain.AplicareLicenta;
import base.domain.Licenta;
import base.domain.StudentInfo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AplicareLicenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AplicareLicentaRepository extends JpaRepository<AplicareLicenta, Long> {

    List<AplicareLicenta> findAllByLicenta(Licenta licenta);
    
    Page<AplicareLicenta> findAllByStudent(StudentInfo student, Pageable pageable);
    
    @Query("SELECT s FROM AplicareLicenta s WHERE s.student.id = :id")
    List<AplicareLicenta> findAllByStudent2(@Param("id") Long id);
    
    @Query("SELECT s.id FROM AplicareLicenta s WHERE s.student.id = :id")
    List<Long> findAllIDByStudent(@Param("id") Long id);

    @Query("SELECT s FROM AplicareLicenta s WHERE s.licenta.profesor.id = :id")
    Page<AplicareLicenta> findAllbyProfesor(@Param("id") Long id, Pageable pageable);
}
