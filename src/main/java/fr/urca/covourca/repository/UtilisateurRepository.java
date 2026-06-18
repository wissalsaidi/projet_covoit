package fr.urca.covourca.repository;

import fr.urca.covourca.model.Utilisateur;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @EntityGraph(attributePaths = {"lignes"})
    Optional<Utilisateur> findByEmail(String email);

    boolean existsByEmail(String email);
}
