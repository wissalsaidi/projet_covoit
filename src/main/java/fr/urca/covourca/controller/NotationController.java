package fr.urca.covourca.controller;

import fr.urca.covourca.dto.NotationDto;
import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.service.NotationService;
import fr.urca.covourca.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notations")
@RequiredArgsConstructor
public class NotationController {

    private final NotationService notationService;
    private final UtilisateurService utilisateurService;

    @PostMapping("/{trajetId}")
    public String noter(@AuthenticationPrincipal UserDetails userDetails,
                        @PathVariable Long trajetId,
                        @Valid @ModelAttribute NotationDto notationDto,
                        RedirectAttributes redirectAttributes) {
        Utilisateur u = utilisateurService.findByEmail(userDetails.getUsername());
        try {
            notationService.noter(u, trajetId, notationDto);
            redirectAttributes.addFlashAttribute("success", "Note enregistrée !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/trajets/" + trajetId;
    }
}
