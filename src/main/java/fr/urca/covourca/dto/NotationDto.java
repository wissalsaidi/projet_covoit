package fr.urca.covourca.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class NotationDto {

    @Min(1)
    @Max(5)
    private int valeur;

    private String commentaire;
}
