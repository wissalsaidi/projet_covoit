package fr.urca.covourca.service;

import fr.urca.covourca.dto.InscriptionDto;
import fr.urca.covourca.dto.VehiculeDto;
import fr.urca.covourca.model.Ligne;
import fr.urca.covourca.model.Utilisateur;
import fr.urca.covourca.model.Vehicule;
import fr.urca.covourca.repository.LigneRepository;
import fr.urca.covourca.repository.NotationRepository;
import fr.urca.covourca.repository.UtilisateurRepository;
import fr.urca.covourca.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final NotationRepository notationRepository;
    private final LigneRepository ligneRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Utilisateur inscrire(InscriptionDto dto) {
        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà enregistré");
        }

        Utilisateur u = Utilisateur.builder()
            .nom(dto.getNom())
            .prenom(dto.getPrenom())
            .email(dto.getEmail())
            .telephone(dto.getTelephone())
            .motDePasse(passwordEncoder.encode(dto.getMotDePasse()))
            .estConducteur(false)
            .build();

        u = utilisateurRepository.save(u);

        if (dto.getImmatriculation() != null && !dto.getImmatriculation().isBlank()) {
            ajouterVehicule(u, buildVehiculeDto(dto));
        }

        return u;
    }

    @Transactional
    public Vehicule ajouterVehicule(Utilisateur u, VehiculeDto dto) {
        Vehicule v = Vehicule.builder()
            .proprietaire(u)
            .marque(dto.getMarque())
            .modele(dto.getModele())
            .couleur(dto.getCouleur())
            .immatriculation(dto.getImmatriculation())
            .nombrePlaces(dto.getNombrePlaces())
            .placesDisponibles(dto.getNombrePlaces())
            .build();
        vehiculeRepository.save(v);

        u.setEstConducteur(true);
        utilisateurRepository.save(u);
        return v;
    }

    @Transactional
    public void mettreAJourNoteMoyenne(Utilisateur u) {
        Double moyenne = notationRepository.calculerMoyenne(u);
        u.setNoteMoyenne(moyenne);
        utilisateurRepository.save(u);
    }

    @Transactional
    public void updateLignes(Utilisateur u, List<Long> ligneIds) {
        List<Ligne> lignes = ligneIds.isEmpty() ? List.of() : ligneRepository.findAllById(ligneIds);
        u.setLignes(new java.util.ArrayList<>(lignes));
        utilisateurRepository.save(u);
    }

    @Transactional
    public void updateInfos(Utilisateur u, String nom, String prenom, String telephone) {
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setTelephone(telephone);
        utilisateurRepository.save(u);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
    }

    private VehiculeDto buildVehiculeDto(InscriptionDto dto) {
        VehiculeDto v = new VehiculeDto();
        v.setMarque(dto.getMarque());
        v.setModele(dto.getModele());
        v.setCouleur(dto.getCouleur());
        v.setImmatriculation(dto.getImmatriculation());
        v.setNombrePlaces(dto.getNombrePlaces() != null ? dto.getNombrePlaces() : 1);
        return v;
    }
}
