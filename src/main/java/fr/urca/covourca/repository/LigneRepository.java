package fr.urca.covourca.repository;

import fr.urca.covourca.model.Ligne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneRepository extends JpaRepository<Ligne, Long> {
    boolean existsByNom(String nom);
}
