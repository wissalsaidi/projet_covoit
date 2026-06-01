package fr.urca.covourca.repository;

import fr.urca.covourca.model.Vehicule;
import fr.urca.covourca.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    List<Vehicule> findByProprietaire(Utilisateur proprietaire);
    List<Vehicule> findByProprietaireAndPlacesDisponiblesGreaterThan(Utilisateur proprietaire, int places);
}
