package base.repository;

import base.domain.Consultatie;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Consultatie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultatieRepository extends JpaRepository<Consultatie, Long> {

}
