package fr.urca.covourca.repository;

import fr.urca.covourca.model.Trajet;
import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.model.enums.StatutTrajet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    List<Trajet> findByConducteurOrderByIdDesc(Utilisateur conducteur);
    List<Trajet> findByPassagerOrderByIdDesc(Utilisateur passager);
    Optional<Trajet> findByConducteurAndStatut(Utilisateur conducteur, StatutTrajet statut);
    Optional<Trajet> findByPassagerAndStatut(Utilisateur passager, StatutTrajet statut);
}
