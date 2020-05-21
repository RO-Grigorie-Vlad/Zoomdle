package base.repository;

import base.domain.Licenta;
import base.domain.ProfesorInfo;
import base.domain.StudentInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
/**
 * Spring Data  repository for the Licenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LicentaRepository extends JpaRepository<Licenta, Long> {

    
    /**Gaseste toate licentele de un profesor by profesorID
     * 2 variante : una cu @Query, celalta folosind metoda findAllByProfesor (comes by default)
     * 
     * 
     */
    @Query("SELECT s FROM Licenta s WHERE s.profesor.id = :id")
    Page<Licenta> findAllByProfesorID(@Param("id") Long id, Pageable pageable);

    List<Licenta> findAllByProfesor(ProfesorInfo profesor);
    Optional<Licenta> findOneByStudentInfo(StudentInfo student);

}
