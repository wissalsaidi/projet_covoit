package fr.urca.covourca.repository;

import fr.urca.covourca.model.DemandeCovoiturage;
import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.model.enums.StatutDemande;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface DemandeCovoiturageRepository extends JpaRepository<DemandeCovoiturage, Long> {
    List<DemandeCovoiturage> findByPassagerOrderByDateHeureDesc(Utilisateur passager);

    @EntityGraph(attributePaths = {"pointDepart", "pointArrivee"})
    Optional<DemandeCovoiturage> findByPassagerAndStatut(Utilisateur passager, StatutDemande statut);

    boolean existsByPassagerAndStatut(Utilisateur passager, StatutDemande statut);

    @Query("SELECT d FROM DemandeCovoiturage d JOIN d.pointDepart p WHERE p.ligne.id = :ligneId AND d.statut = 'EN_ATTENTE'")
    List<DemandeCovoiturage> findEnAttenteByLigneId(Long ligneId);
}
