package fr.urca.covourca.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehiculeDto {

    @NotBlank
    private String marque;

    @NotBlank
    private String modele;

    private String couleur;

    @NotBlank
    private String immatriculation;

    @Min(1)
    private int nombrePlaces;
}
