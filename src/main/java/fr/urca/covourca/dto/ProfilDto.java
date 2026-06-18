package fr.urca.covourca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProfilDto {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @Pattern(regexp = "^[+]?[0-9\\s\\-().]{7,20}$", message = "Numéro de téléphone invalide (ex: +33 6 12 34 56 78 ou 0612345678)")
    private String telephone;
}
