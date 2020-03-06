package base.repository;

import base.domain.Licenta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Licenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LicentaRepository extends JpaRepository<Licenta, Long> {

}
