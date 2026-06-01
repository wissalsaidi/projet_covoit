package fr.urca.covourca.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Utilisateur proprietaire;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    private String couleur;

    @Column(nullable = false, unique = true)
    private String immatriculation;

    @Column(name = "nombre_places", nullable = false)
    private int nombrePlaces;

    @Column(name = "places_disponibles", nullable = false)
    private int placesDisponibles;
}
