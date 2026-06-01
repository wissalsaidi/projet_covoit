package fr.urca.covourca.repository;

import fr.urca.covourca.model.Ligne;
import fr.urca.covourca.model.PointRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PointRendezVousRepository extends JpaRepository<PointRendezVous, Long> {
    List<PointRendezVous> findByLigneOrderByOrdrePassage(Ligne ligne);
}
