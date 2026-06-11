package fr.urca.covourca.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UtilisateurService utilisateurService;
    private final DemandeCovoiturageRepository demandeRepository;
    private final TrajetRepository trajetRepository;
    private final LigneRepository ligneRepository;
    private final PointRendezVousRepository pointRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        model.addAttribute("utilisateur", u);
        boolean conducteurSansLignes = u.isEstConducteur() && u.getLignes().isEmpty();
        model.addAttribute("conducteurSansLignes", conducteurSansLignes);

        Optional<DemandeCovoiturage> demandeEnCours =
            demandeRepository.findByPassagerAndStatut(u, StatutDemande.EN_ATTENTE);
        model.addAttribute("demandeEnCours", demandeEnCours.orElse(null));

        Optional<Trajet> trajetEnCoursPassager =
            trajetRepository.findByPassagerAndStatut(u, StatutTrajet.EN_COURS);
        Optional<Trajet> trajetEnCoursConducteur =
            trajetRepository.findByConducteurAndStatut(u, StatutTrajet.EN_COURS);
        model.addAttribute("trajetEnCoursPassager", trajetEnCoursPassager.orElse(null));
        model.addAttribute("trajetEnCoursConducteur", trajetEnCoursConducteur.orElse(null));

        List<Trajet> derniersTrajets = new ArrayList<>(trajetRepository.findByPassagerOrderByIdDesc(u));
        derniersTrajets.addAll(trajetRepository.findByConducteurOrderByIdDesc(u));
        derniersTrajets.sort((a, b) -> b.getId().compareTo(a.getId()));
        model.addAttribute("derniersTrajets", derniersTrajets.stream().limit(5).toList());

        model.addAttribute("lignes", ligneRepository.findAll());

        // Points pour la carte Leaflet (injectés directement, pas de fetch /api/points)
        List<PointRendezVous> allPoints = pointRepository.findAllWithLigne();
        List<Map<String, Object>> pts = allPoints.stream().map(p -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id",       p.getId());
            m.put("nom",      p.getNomLieu());
            m.put("lat",      p.getLatitude());
            m.put("lng",      p.getLongitude());
            m.put("ordre",    p.getOrdrePassage());
            m.put("ligneId",  p.getLigne().getId());
            m.put("ligneNom", p.getLigne().getNom());
            return m;
        }).toList();
        try {
            model.addAttribute("pointsJson", objectMapper.writeValueAsString(pts));
        } catch (JsonProcessingException e) {
            model.addAttribute("pointsJson", "[]");
        }

        return "dashboard/index";
    }
}
