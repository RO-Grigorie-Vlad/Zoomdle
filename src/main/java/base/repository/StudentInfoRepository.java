package base.repository;

import base.domain.StudentInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the StudentInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {

}
