package base.repository;

import base.domain.StudentInfo;
import base.domain.User;
import base.domain.AplicareLicenta;
import base.domain.Licenta;
import base.domain.ProfesorInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the StudentInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {

    @Query("SELECT s FROM StudentInfo s WHERE s.profesor.id = :id")
    Page<StudentInfo> findAllByProfesorID(@Param("id") Long id, Pageable pageable);

    Page<StudentInfo> findAllByProfesor(ProfesorInfo profesor, Pageable pageable);
    
    Optional<StudentInfo> findOneByUser(User user);

    Optional<StudentInfo> findOneByLicenta(Licenta licenta);

}
