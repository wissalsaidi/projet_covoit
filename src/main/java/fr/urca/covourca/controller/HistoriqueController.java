package fr.urca.covourca.controller;

import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.service.TrajetService;
import fr.urca.covourca.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HistoriqueController {

    private final TrajetService trajetService;
    private final UtilisateurService utilisateurService;

    @GetMapping("/historique")
    public String historique(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        model.addAttribute("trajets", trajetService.historiqueUtilisateur(u));
        model.addAttribute("utilisateur", u);
        return "historique";
    }
}
