package fr.urca.covourca.repository;

import fr.urca.covourca.model.Notation;
import fr.urca.covourca.model.Trajet;
import fr.urca.covourca.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface NotationRepository extends JpaRepository<Notation, Long> {
    boolean existsByTrajetAndAuteur(Trajet trajet, Utilisateur auteur);
    Optional<Notation> findByTrajetAndAuteur(Trajet trajet, Utilisateur auteur);

    @Query("SELECT AVG(n.valeur) FROM Notation n WHERE n.cible = :cible")
    Double calculerMoyenne(Utilisateur cible);
}
