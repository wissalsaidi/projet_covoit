package fr.urca.covourca.repository;

import fr.urca.covourca.model.Ligne;
import fr.urca.covourca.model.PointRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PointRendezVousRepository extends JpaRepository<PointRendezVous, Long> {
    List<PointRendezVous> findByLigneOrderByOrdrePassage(Ligne ligne);
    List<PointRendezVous> findByLigneIdOrderByOrdrePassage(Long ligneId);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM PointRendezVous p JOIN FETCH p.ligne ORDER BY p.ligne.id, p.ordrePassage")
    List<PointRendezVous> findAllWithLigne();
}
