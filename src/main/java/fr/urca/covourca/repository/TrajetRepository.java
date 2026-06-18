package fr.urca.covourca.repository;

import fr.urca.covourca.model.Trajet;
import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.model.enums.StatutTrajet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {

    @EntityGraph(attributePaths = {"demande", "demande.pointDepart", "demande.pointArrivee", "conducteur", "passager", "vehicule"})
    @Override
    Optional<Trajet> findById(Long id);

    @EntityGraph(attributePaths = {"demande", "demande.pointDepart", "demande.pointArrivee", "conducteur", "passager", "vehicule"})
    List<Trajet> findByConducteurOrderByIdDesc(Utilisateur conducteur);

    @EntityGraph(attributePaths = {"demande", "demande.pointDepart", "demande.pointArrivee", "conducteur", "passager", "vehicule"})
    List<Trajet> findByPassagerOrderByIdDesc(Utilisateur passager);

    Optional<Trajet> findByConducteurAndStatut(Utilisateur conducteur, StatutTrajet statut);
    Optional<Trajet> findByPassagerAndStatut(Utilisateur passager, StatutTrajet statut);

    @EntityGraph(attributePaths = {"demande", "demande.pointDepart", "demande.pointArrivee", "conducteur", "passager", "vehicule"})
    Optional<Trajet> findByConducteurAndStatutIn(Utilisateur conducteur, Collection<StatutTrajet> statuts);

    @EntityGraph(attributePaths = {"demande", "demande.pointDepart", "demande.pointArrivee", "conducteur", "passager", "vehicule"})
    Optional<Trajet> findByPassagerAndStatutIn(Utilisateur passager, Collection<StatutTrajet> statuts);
}
