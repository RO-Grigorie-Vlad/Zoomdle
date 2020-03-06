package base.repository;

import base.domain.AplicareLicenta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AplicareLicenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AplicareLicentaRepository extends JpaRepository<AplicareLicenta, Long> {

}
