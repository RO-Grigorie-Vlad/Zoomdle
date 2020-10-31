package base.repository;

import base.domain.ProfesorInfo;
import base.domain.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProfesorInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfesorInfoRepository extends JpaRepository<ProfesorInfo, Long> {

    Optional<ProfesorInfo> findOneByUser(User user);
    
}
