package fr.urca.covourca.controller;

import fr.urca.covourca.dto.InscriptionDto;
import fr.urca.covourca.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/")
    public String accueil() {
        return "accueil";
    }

    @GetMapping("/inscription")
    public String inscriptionForm(Model model) {
        model.addAttribute("inscriptionDto", new InscriptionDto());
        return "auth/inscription";
    }

    @PostMapping("/inscription")
    public String inscrire(@Valid @ModelAttribute InscriptionDto inscriptionDto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/inscription";
        }
        try {
            utilisateurService.inscrire(inscriptionDto);
            redirectAttributes.addFlashAttribute("success", "Compte créé ! Vous pouvez vous connecter.");
            return "redirect:/connexion";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erreur", e.getMessage());
            return "auth/inscription";
        }
    }

    @GetMapping("/connexion")
    public String connexionForm() {
        return "auth/connexion";
    }

    @GetMapping("/confidentialite")
    public String confidentialite() {
        return "confidentialite";
    }

    @GetMapping("/conditions")
    public String conditions() {
        return "conditions";
    }

    @GetMapping("/mot-de-passe-oublie")
    public String motDePasseOublieForm() {
        return "auth/mot-de-passe-oublie";
    }

    @PostMapping("/mot-de-passe-oublie")
    public String motDePasseOublie(@RequestParam String email,
                                   RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success",
            "Si un compte existe avec cet email, vous recevrez un lien de réinitialisation.");
        return "redirect:/mot-de-passe-oublie";
    }
}
