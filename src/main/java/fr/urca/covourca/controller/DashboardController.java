package fr.urca.covourca.controller;

import fr.urca.covourca.model.*;
import fr.urca.covourca.model.enums.StatutDemande;
import fr.urca.covourca.model.enums.StatutTrajet;
import fr.urca.covourca.repository.*;
import fr.urca.covourca.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UtilisateurService utilisateurService;
    private final DemandeCovoiturageRepository demandeRepository;
    private final TrajetRepository trajetRepository;
    private final LigneRepository ligneRepository;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        model.addAttribute("utilisateur", u);
        // Safe pre-computation: triggers lazy load here (OSIV session open) instead of mid-render
        boolean conducteurSansLignes = u.isEstConducteur() && u.getLignes().isEmpty();
        model.addAttribute("conducteurSansLignes", conducteurSansLignes);

        // Demande en cours du passager
        Optional<DemandeCovoiturage> demandeEnCours =
            demandeRepository.findByPassagerAndStatut(u, StatutDemande.EN_ATTENTE);
        model.addAttribute("demandeEnCours", demandeEnCours.orElse(null));

        // Trajet en cours (passager ou conducteur)
        Optional<Trajet> trajetEnCoursPassager =
            trajetRepository.findByPassagerAndStatut(u, StatutTrajet.EN_COURS);
        Optional<Trajet> trajetEnCoursConducteur =
            trajetRepository.findByConducteurAndStatut(u, StatutTrajet.EN_COURS);
        model.addAttribute("trajetEnCoursPassager", trajetEnCoursPassager.orElse(null));
        model.addAttribute("trajetEnCoursConducteur", trajetEnCoursConducteur.orElse(null));

        // Derniers trajets
        List<Trajet> derniersTrajets = new ArrayList<>(trajetRepository.findByPassagerOrderByIdDesc(u));
        derniersTrajets.addAll(trajetRepository.findByConducteurOrderByIdDesc(u));
        derniersTrajets.sort((a, b) -> b.getId().compareTo(a.getId()));
        model.addAttribute("derniersTrajets", derniersTrajets.stream().limit(5).toList());

        // Lignes disponibles pour la demande passager
        model.addAttribute("lignes", ligneRepository.findAll());

        return "dashboard/index";
    }
}
