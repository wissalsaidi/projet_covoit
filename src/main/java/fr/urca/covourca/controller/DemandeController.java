package fr.urca.covourca.controller;

import fr.urca.covourca.model.Trajet;
import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.service.DemandeService;
import fr.urca.covourca.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/demandes")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService demandeService;
    private final UtilisateurService utilisateurService;

    @PostMapping("/nouvelle")
    public String creer(@AuthenticationPrincipal UserDetails userDetails,
                        @RequestParam(required = false) Long departId,
                        @RequestParam(required = false) Long arriveeId,
                        @RequestParam(required = false) String departAdresse,
                        @RequestParam(required = false) Double departLatitude,
                        @RequestParam(required = false) Double departLongitude,
                        @RequestParam(required = false) String arriveeAdresse,
                        @RequestParam(required = false) Double arriveeLatitude,
                        @RequestParam(required = false) Double arriveeLongitude,
                        RedirectAttributes redirectAttributes) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        try {
            if (departId != null && arriveeId != null) {
                demandeService.creerDemande(u, departId, arriveeId);
            } else if (departAdresse != null && arriveeAdresse != null) {
                demandeService.creerDemandeLibre(u, departAdresse, departLatitude, departLongitude,
                    arriveeAdresse, arriveeLatitude, arriveeLongitude);
            } else {
                throw new IllegalArgumentException("Veuillez sélectionner un départ et une destination");
            }
            redirectAttributes.addFlashAttribute("success", "Demande envoyée ! En attente d'un conducteur.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/{id}/annuler")
    public String annuler(@AuthenticationPrincipal UserDetails userDetails,
                          @PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        try {
            demandeService.annulerDemande(u, id);
            redirectAttributes.addFlashAttribute("success", "Demande annulée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/{id}/accepter")
    public String accepter(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable Long id,
                           RedirectAttributes redirectAttributes) {
        Utilisateur conducteur = utilisateurService.findByEmail(userDetails.getUsername());
        try {
            Trajet trajet = demandeService.accepterDemande(conducteur, id);
            return "redirect:/trajets/" + trajet.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/dashboard";
        }
    }
}
