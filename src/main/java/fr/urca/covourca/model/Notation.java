package fr.urca.covourca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "notations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"trajet_id", "auteur_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Trajet trajet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Utilisateur auteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cible_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Utilisateur cible;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int valeur;

    private String commentaire;
}
