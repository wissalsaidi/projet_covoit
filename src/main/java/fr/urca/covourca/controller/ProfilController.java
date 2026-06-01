package fr.urca.covourca.controller;

import fr.urca.covourca.dto.VehiculeDto;
import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.repository.VehiculeRepository;
import fr.urca.covourca.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profil")
@RequiredArgsConstructor
public class ProfilController {

    private final UtilisateurService utilisateurService;
    private final VehiculeRepository vehiculeRepository;

    @GetMapping
    public String profil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        model.addAttribute("utilisateur", u);
        model.addAttribute("vehicules", vehiculeRepository.findByProprietaire(u));
        model.addAttribute("vehiculeDto", new VehiculeDto());
        return "profil";
    }

    @PostMapping("/vehicule/ajouter")
    public String ajouterVehicule(@AuthenticationPrincipal UserDetails userDetails,
                                  @Valid @ModelAttribute VehiculeDto vehiculeDto,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("erreur", "Formulaire invalide");
            return "redirect:/profil";
        }
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        utilisateurService.ajouterVehicule(u, vehiculeDto);
        redirectAttributes.addFlashAttribute("success", "Véhicule ajouté. Vous êtes maintenant conducteur !");
        return "redirect:/profil";
    }
}
