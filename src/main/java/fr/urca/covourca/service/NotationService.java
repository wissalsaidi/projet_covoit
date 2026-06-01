package fr.urca.covourca.service;

import fr.urca.covourca.dto.NotationDto;
import fr.urca.covourca.model.*;
import fr.urca.covourca.model.enums.StatutTrajet;
import fr.urca.covourca.repository.NotationRepository;
import fr.urca.covourca.repository.TrajetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotationService {

    private final NotationRepository notationRepository;
    private final TrajetRepository trajetRepository;
    private final UtilisateurService utilisateurService;

    @Transactional
    public Notation noter(Utilisateur auteur, Long trajetId, NotationDto dto) {
        Trajet trajet = trajetRepository.findById(trajetId)
            .orElseThrow(() -> new IllegalArgumentException("Trajet introuvable"));

        if (trajet.getStatut() != StatutTrajet.TERMINE) {
            throw new IllegalStateException("La notation n'est possible qu'après la fin du trajet");
        }
        if (notationRepository.existsByTrajetAndAuteur(trajet, auteur)) {
            throw new IllegalStateException("Vous avez déjà noté ce trajet");
        }

        Utilisateur cible = auteur.getId().equals(trajet.getConducteur().getId())
            ? trajet.getPassager()
            : trajet.getConducteur();

        Notation notation = Notation.builder()
            .trajet(trajet)
            .auteur(auteur)
            .cible(cible)
            .valeur(dto.getValeur())
            .commentaire(dto.getCommentaire())
            .build();

        notationRepository.save(notation);
        utilisateurService.mettreAJourNoteMoyenne(cible);
        return notation;
    }

    public boolean dejaNoteParUtilisateur(Trajet trajet, Utilisateur auteur) {
        return notationRepository.existsByTrajetAndAuteur(trajet, auteur);
    }
}
