package fr.urca.covourca.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "points_rendez_vous")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointRendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligne_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Ligne ligne;

    @Column(name = "nom_lieu", nullable = false)
    private String nomLieu;

    private double latitude;
    private double longitude;

    @Column(name = "ordre_passage", nullable = false)
    private int ordrePassage;
}
