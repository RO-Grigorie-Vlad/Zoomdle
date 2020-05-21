package base.repository;

import base.domain.Consultatie;
import base.domain.ProfesorInfo;
import base.domain.StudentInfo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Consultatie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultatieRepository extends JpaRepository<Consultatie, Long> {


    @Query("SELECT c FROM Consultatie c")
    List<Consultatie> findAll2();

    List<Consultatie> findAllByProfesor(ProfesorInfo profesor);

    @Query("SELECT c FROM Consultatie c WHERE c.profesor.id = :id")
    Page<Consultatie> findAllByProfesorPage(@Param("id") Long id, Pageable pagable);

    

    List<Consultatie> findAllByStudent(StudentInfo student);
}
