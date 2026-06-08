package fr.urca.covourca.controller;

import fr.urca.covourca.dto.VehiculeDto;
import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.repository.LigneRepository;
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

import java.util.List;

@Controller
@RequestMapping("/profil")
@RequiredArgsConstructor
public class ProfilController {

    private final UtilisateurService utilisateurService;
    private final VehiculeRepository vehiculeRepository;
    private final LigneRepository ligneRepository;

    @GetMapping
    public String profil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        int nbLignes = u.getLignes().size(); // OSIV session open here — safe lazy load
        model.addAttribute("utilisateur", u);
        model.addAttribute("conducteurAvecLignes", u.isEstConducteur() && nbLignes > 0);
        model.addAttribute("conducteurSansLignes", u.isEstConducteur() && nbLignes == 0);
        model.addAttribute("vehicules", vehiculeRepository.findByProprietaire(u));
        model.addAttribute("vehiculeDto", new VehiculeDto());
        model.addAttribute("toutesLignes", ligneRepository.findAll());
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

    @PostMapping("/infos")
    public String updateInfos(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam String nom,
                              @RequestParam String prenom,
                              @RequestParam(required = false) String telephone,
                              RedirectAttributes redirectAttributes) {
        if (nom == null || nom.isBlank() || prenom == null || prenom.isBlank()) {
            redirectAttributes.addFlashAttribute("erreur", "Le nom et le prénom sont obligatoires.");
            return "redirect:/profil";
        }
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        utilisateurService.updateInfos(u, nom.trim(), prenom.trim(),
            telephone != null && !telephone.isBlank() ? telephone.trim() : null);
        redirectAttributes.addFlashAttribute("success", "Informations mises à jour.");
        return "redirect:/profil";
    }

    @PostMapping("/lignes/update")
    public String updateLignes(@AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam(required = false) List<Long> ligneIds,
                               RedirectAttributes redirectAttributes) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        utilisateurService.updateLignes(u, ligneIds != null ? ligneIds : List.of());
        redirectAttributes.addFlashAttribute("success", "Lignes de covoiturage mises à jour.");
        return "redirect:/profil";
    }
}
