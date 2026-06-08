package fr.urca.covourca.service;

import fr.urca.covourca.model.*;
import fr.urca.covourca.model.enums.StatutDemande;
import fr.urca.covourca.model.enums.StatutTrajet;
import fr.urca.covourca.repository.*;
import fr.urca.covourca.websocket.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DemandeService {

    private final DemandeCovoiturageRepository demandeRepository;
    private final PointRendezVousRepository pointRepository;
    private final TrajetRepository trajetRepository;
    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public DemandeCovoiturage creerDemande(Utilisateur passager, Long departId, Long arriveeId) {
        if (demandeRepository.existsByPassagerAndStatut(passager, StatutDemande.EN_ATTENTE)) {
            throw new IllegalStateException("Vous avez déjà une demande en cours");
        }

        PointRendezVous depart = pointRepository.findById(departId)
            .orElseThrow(() -> new IllegalArgumentException("Point de départ introuvable"));
        PointRendezVous arrivee = pointRepository.findById(arriveeId)
            .orElseThrow(() -> new IllegalArgumentException("Point d'arrivée introuvable"));

        if (!depart.getLigne().getId().equals(arrivee.getLigne().getId())) {
            throw new IllegalArgumentException("Les points doivent être sur la même ligne");
        }

        DemandeCovoiturage demande = DemandeCovoiturage.builder()
            .passager(passager)
            .pointDepart(depart)
            .pointArrivee(arrivee)
            .statut(StatutDemande.EN_ATTENTE)
            .build();

        demande = demandeRepository.save(demande);
        notifierConducteurs(demande);
        return demande;
    }

    @Transactional
    public Trajet accepterDemande(Utilisateur conducteur, Long demandeId) {
        DemandeCovoiturage demande = demandeRepository.findById(demandeId)
            .orElseThrow(() -> new IllegalArgumentException("Demande introuvable"));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Cette demande a déjà été acceptée");
        }

        List<Vehicule> vehicules = vehiculeRepository
            .findByProprietaireAndPlacesDisponiblesGreaterThan(conducteur, 0);
        if (vehicules.isEmpty()) {
            throw new IllegalStateException("Plus de places disponibles");
        }

        Vehicule vehicule = vehicules.get(0);
        vehicule.setPlacesDisponibles(vehicule.getPlacesDisponibles() - 1);
        vehiculeRepository.save(vehicule);

        demande.setStatut(StatutDemande.ACCEPTEE);
        demandeRepository.save(demande);

        String code = String.format("%04d", new Random().nextInt(10000));

        Trajet trajet = Trajet.builder()
            .conducteur(conducteur)
            .passager(demande.getPassager())
            .demande(demande)
            .vehicule(vehicule)
            .statut(StatutTrajet.EN_COURS)
            .identifiantEchange(code)
            .build();

        Trajet saved = trajetRepository.save(trajet);

        // Notify passenger in real time that their request was accepted
        messagingTemplate.convertAndSendToUser(
            demande.getPassager().getEmail(),
            "/queue/acceptations",
            java.util.Map.of(
                "trajetId", saved.getId(),
                "conducteurNom", conducteur.getPrenom() + " " + conducteur.getNom(),
                "vehicule", vehicule.getMarque() + " " + vehicule.getModele() + " (" + vehicule.getCouleur() + ")"
            )
        );

        return saved;
    }

    @Transactional
    public void annulerDemande(Utilisateur passager, Long demandeId) {
        DemandeCovoiturage demande = demandeRepository.findById(demandeId)
            .orElseThrow(() -> new IllegalArgumentException("Demande introuvable"));

        if (!demande.getPassager().getId().equals(passager.getId())) {
            throw new SecurityException("Accès refusé");
        }
        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Seules les demandes EN_ATTENTE peuvent être annulées");
        }

        demande.setStatut(StatutDemande.ANNULEE);
        demandeRepository.save(demande);
    }

    private void notifierConducteurs(DemandeCovoiturage demande) {
        Long ligneId = demande.getPointDepart().getLigne().getId();

        List<Utilisateur> conducteurs = utilisateurRepository.findAll().stream()
            .filter(Utilisateur::isEstConducteur)
            .filter(c -> c.getLignes().stream().anyMatch(l -> l.getId().equals(ligneId)))
            .filter(c -> vehiculeRepository
                .findByProprietaireAndPlacesDisponiblesGreaterThan(c, 0).size() > 0)
            .toList();

        NotificationMessage msg = new NotificationMessage(
            demande.getId(),
            demande.getPassager().getPrenom() + " " + demande.getPassager().getNom(),
            demande.getPointDepart().getNomLieu(),
            demande.getPointArrivee().getNomLieu(),
            demande.getPassager().getNoteMoyenne()
        );

        for (Utilisateur conducteur : conducteurs) {
            messagingTemplate.convertAndSendToUser(
                conducteur.getEmail(),
                "/queue/alertes",
                msg
            );
        }
    }
}
