package base.repository;

import base.domain.AplicareConsultatie;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AplicareConsultatie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AplicareConsultatieRepository extends JpaRepository<AplicareConsultatie, Long> {

}
