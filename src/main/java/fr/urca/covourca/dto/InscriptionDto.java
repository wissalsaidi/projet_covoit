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

    @NotBlank(message = "Le numéro de téléphone est requis")
    @Pattern(regexp = "^[+]?[0-9\\s\\-\\(\\)]{7,20}$", message = "Numéro de téléphone invalide (ex: +33 6 12 34 56 78 ou 0612345678)")
    private String telephone;

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
