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
                        @RequestParam Long departId,
                        @RequestParam Long arriveeId,
                        RedirectAttributes redirectAttributes) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        try {
            demandeService.creerDemande(u, departId, arriveeId);
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
