package fr.urca.covourca.service;

import fr.urca.covourca.model.*;
import fr.urca.covourca.model.enums.StatutTrajet;
import fr.urca.covourca.repository.TrajetRepository;
import fr.urca.covourca.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrajetService {

    private final TrajetRepository trajetRepository;
    private final VehiculeRepository vehiculeRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Trajet terminerTrajet(Utilisateur conducteur, Long trajetId) {
        Trajet trajet = findById(trajetId);
        verifierConducteur(trajet, conducteur);

        trajet.setStatut(StatutTrajet.TERMINE);
        trajetRepository.save(trajet);

        Vehicule v = trajet.getVehicule();
        if (v != null) {
            v.setPlacesDisponibles(v.getPlacesDisponibles() + 1);
            vehiculeRepository.save(v);
        }
        return trajet;
    }

    @Transactional
    public Trajet annulerTrajet(Utilisateur conducteur, Long trajetId) {
        Trajet trajet = findById(trajetId);
        verifierConducteur(trajet, conducteur);

        trajet.setStatut(StatutTrajet.ANNULE);
        trajetRepository.save(trajet);

        Vehicule v = trajet.getVehicule();
        if (v != null) {
            v.setPlacesDisponibles(v.getPlacesDisponibles() + 1);
            vehiculeRepository.save(v);
        }

        messagingTemplate.convertAndSendToUser(
            trajet.getPassager().getEmail(),
            "/queue/annulations",
            "Votre trajet a été annulé par le conducteur"
        );

        return trajet;
    }

    @Transactional
    public boolean confirmerCode(Long trajetId, String code) {
        Trajet trajet = findById(trajetId);
        return trajet.getIdentifiantEchange().equals(code);
    }

    public List<Trajet> historiqueUtilisateur(Utilisateur u) {
        List<Trajet> conduites = trajetRepository.findByConducteurOrderByIdDesc(u);
        List<Trajet> passager = trajetRepository.findByPassagerOrderByIdDesc(u);
        conduites.addAll(passager);
        conduites.sort((a, b) -> b.getId().compareTo(a.getId()));
        return conduites;
    }

    public Trajet findById(Long id) {
        return trajetRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Trajet introuvable"));
    }

    private void verifierConducteur(Trajet trajet, Utilisateur conducteur) {
        if (!trajet.getConducteur().getId().equals(conducteur.getId())) {
            throw new SecurityException("Accès refusé");
        }
    }
}
