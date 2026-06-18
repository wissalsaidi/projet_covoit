package fr.urca.covourca.controller;

import fr.urca.covourca.model.Trajet;
import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.service.NotationService;
import fr.urca.covourca.service.TrajetService;
import fr.urca.covourca.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/trajets")
@RequiredArgsConstructor
public class TrajetController {

    private final TrajetService trajetService;
    private final UtilisateurService utilisateurService;
    private final NotationService notationService;

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String detail(@AuthenticationPrincipal UserDetails userDetails,
                         @PathVariable Long id,
                         Model model) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        Trajet trajet = trajetService.findById(id);
        model.addAttribute("trajet", trajet);
        model.addAttribute("utilisateur", u);
        model.addAttribute("dejaNote", notationService.dejaNoteParUtilisateur(trajet, u));
        return "trajet/detail";
    }

    @PostMapping("/{id}/terminer")
    public String terminer(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable Long id,
                           RedirectAttributes redirectAttributes) {
        Utilisateur conducteur = utilisateurService.findByEmail(userDetails.getUsername());
        try {
            trajetService.terminerTrajet(conducteur, id);
            redirectAttributes.addFlashAttribute("success", "Trajet terminé. Vous pouvez maintenant noter.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/trajets/" + id;
    }

    @PostMapping("/{id}/annuler")
    public String annuler(@AuthenticationPrincipal UserDetails userDetails,
                          @PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        Utilisateur conducteur = utilisateurService.findByEmail(userDetails.getUsername());
        try {
            trajetService.annulerTrajet(conducteur, id);
            redirectAttributes.addFlashAttribute("success", "Trajet annulé.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/{id}/confirmer-code")
    public String confirmerCode(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable Long id,
                                @RequestParam String code,
                                RedirectAttributes redirectAttributes) {
        if (trajetService.confirmerCode(id, code)) {
            redirectAttributes.addFlashAttribute("success", "Code confirmé ! Bon voyage.");
        } else {
            redirectAttributes.addFlashAttribute("erreur", "Le code ne correspond pas.");
        }
        return "redirect:/trajets/" + id;
    }
}
