package fr.urca.covourca.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class InscriptionDto {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    // Champs optionnels pour le profil conducteur
    private String marque;
    private String modele;
    private String couleur;
    private String immatriculation;
    private Integer nombrePlaces;
}
